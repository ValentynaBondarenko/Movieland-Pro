package com.bondarenko.movieland.service.converter;

import com.bondarenko.movieland.exception.CurrencyExchangeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class CurrencyConverter {

    @Value("${exchange.api.url}")
    private String exchangeApiUrl;

    @Value("${exchange.scheduled.cron}")
    private String scheduledCron;

    private Map<String, BigDecimal> currencyCache = new ConcurrentHashMap<>();

    @PostConstruct
    public void initialize() {
        updateCurrencyCache();
    }

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
    public void scheduledUpdateCurrencyCache() {
        updateCurrencyCache();
    }

    void updateCurrencyCache() {
        try {
            CurrencyExchange[] currencyExchanges = new ObjectMapper().readValue(new URL(exchangeApiUrl), CurrencyExchange[].class);
            currencyCache.clear();
            for (CurrencyExchange exchange : currencyExchanges) {
                currencyCache.put(exchange.getCode(), exchange.getRate());
            }
            log.info("Currency cache updated");
        } catch (IOException e) {
            log.error("Failed to update currency cache", e);
            throw new RuntimeException("Can't update currency cache. The cache will remain unchanged", e);
        }
    }
}