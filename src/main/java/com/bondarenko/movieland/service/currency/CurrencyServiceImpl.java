package com.bondarenko.movieland.service.currency;

import com.bondarenko.movieland.entity.CurrencyType;
import com.bondarenko.movieland.exception.CurrencyExchangeException;
import com.bondarenko.movieland.service.currency.dto.CurrencyExchangeDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {
    @Value("${exchange.api.url}")
    private String exchangeApiUrl;
    private final RestClient restClient;

    private final Map<String, BigDecimal> currencyCache = new ConcurrentHashMap<>();

    @Override
    public BigDecimal convertCurrency(BigDecimal price, CurrencyType currency) {
        if (currency == null || currency == CurrencyType.UAH) {
            log.info("Default currency is UAH");
            return price;
        }

        BigDecimal exchangeRate = currencyCache.get(currency.name());
        if (exchangeRate == null) {
            log.error("Currency not found: {}", currency);
            throw new CurrencyExchangeException("Currency not found: " + currency);
        }

        BigDecimal converted = price.multiply(exchangeRate);
        log.info("Converted {} {} to {} UAH with exchange rate {}", price, currency, converted, exchangeRate);
        return converted;
    }

    @Scheduled(cron = "${exchange.scheduled.cron}")
    void scheduledUpdateCurrencyCache() {
        updateCurrencyCache();
    }

    @PostConstruct
    void updateCurrencyCache() {
        log.info("Initializing currency cache...");
        //rest template, web client, rest client
        CurrencyExchangeDto[] response = restClient
                .get()
                .uri(exchangeApiUrl)
                .retrieve()
                .body(CurrencyExchangeDto[].class);

        CurrencyExchangeDto[] currencyExchanges = Optional.ofNullable(response)
                .orElseThrow(() -> {
                    log.warn("Currency API response was empty. Currency cache not updated.");
                    return new RuntimeException("Currency API returned null body.");
                });

        Arrays.stream(currencyExchanges)
                .forEach(dto -> currencyCache.put(dto.getCode(), dto.getRate()));
        log.info("Currency cache updated successfully");
    }
}