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
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
public class CurrencyConverter {
    @Value("${exchange.api.url}")
    private String exchangeApiUrl;
    @Value("${exchange.cache.timeout}")
    private String timeout;

    private final CopyOnWriteArrayList<CurrencyExchange> currencyCache = new CopyOnWriteArrayList<>();
    ExecutorService executor = Executors.newSingleThreadExecutor();

    @PostConstruct
    private void initialize() {
        updateCurrencyCache();
    }

    public BigDecimal convertCurrency(BigDecimal price, String currency) {
        if (currency == null) {
            log.info("Default currency is UAH");
            return price;
        }
        Optional<BigDecimal> exchangeRate = getExchangeRate(currency);

        exchangeRate.ifPresent(rate ->
                log.info(String.format("Converted %s %s with exchange rate %s", currency, price, rate))
        );
        return exchangeRate.map(price::multiply)
                .orElseThrow(() -> new CurrencyExchangeException("Currency not found: " + currency));

    }

    private Optional<BigDecimal> getExchangeRate(String currency) {
        return currencyCache.stream()
                .filter(currencyExchange -> currency.equalsIgnoreCase(currencyExchange.getCode()))
                .map(CurrencyExchange::getRate)
                .findFirst();
    }

    //if cache will be update long time
    @Scheduled(cron = "${exchange.scheduled}", zone = "${exchange.scheduled.zone}")
    public void scheduledUpdateCurrencyCache() {
        Future<?> updateTime = executor.submit(this::updateCurrencyCache);
        try {
            updateTime.get(30, TimeUnit.MINUTES);
        } catch (TimeoutException e) {
            updateTime.cancel(true);
            log.error("Currency cache update timed out", e);
            throw new RuntimeException("Currency cache update timed out", e);
        } catch (ExecutionException | InterruptedException e) {
            log.error("Failed to update currency cache", e);
            throw new RuntimeException("Failed to update currency cache", e);
        }
    }

    private void updateCurrencyCache() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            CurrencyExchange[] currencyExchanges = objectMapper.readValue(new URL(exchangeApiUrl), CurrencyExchange[].class);
            currencyCache.clear();
            currencyCache.addAll(Arrays.asList(currencyExchanges));
            log.info("Currency cache updated");
        } catch (IOException e) {
            throw new RuntimeException("Can`t read url " + exchangeApiUrl + " and get currency rate", e);
        }
    }
}
