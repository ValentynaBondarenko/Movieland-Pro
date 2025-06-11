package com.bondarenko.movieland.service.currency;

import com.bondarenko.movieland.exception.CurrencyExchangeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
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
    public BigDecimal convertCurrency(BigDecimal price, String currency) {
        if (currency == null) {
            log.info("Default currency is UAH");
            return price;
        }

        BigDecimal exchangeRate = currencyCache.get(currency);
        if (exchangeRate == null) {
            log.error("Currency not found: {}", currency);
            throw new CurrencyExchangeException("Currency not found: " + currency);
        }

        log.info("Converted {} {} to {} UAH with exchange rate {}", price, currency, price.multiply(exchangeRate), exchangeRate);
        return price.multiply(exchangeRate);
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