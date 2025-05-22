package com.bondarenko.movieland.service.converter;

import com.bondarenko.movieland.exception.CurrencyExchangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

class CurrencyConverterITest {

    @Mock
    private CurrencyConverter currencyConverter;

    @InjectMocks
    private CurrencyConverter converter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testConvertCurrency_WhenCurrencyNotFound_ShouldThrowCurrencyExchangeException() {
        // prepare
        String currency = "USD";
        BigDecimal price = new BigDecimal("100");

        // when
        doThrow(new CurrencyExchangeException("Currency not found: " + currency))
                .when(currencyConverter).convertCurrency(price, currency);

        // then
        assertThrows(CurrencyExchangeException.class, () -> currencyConverter.convertCurrency(price, currency));
    }

    @Test
    public void testUpdateCurrencyCache_ShouldUpdateCacheSuccessfully() {
        // prepare
        doNothing().when(currencyConverter).updateCurrencyCache();

        // when
        currencyConverter.updateCurrencyCache();

        // then
        verify(currencyConverter, times(1)).updateCurrencyCache();
    }

    @Test
    public void testScheduledUpdateCurrencyCache_ShouldUpdateCacheOnSchedule() throws InterruptedException {
        // prepare
        doNothing().when(currencyConverter).scheduledUpdateCurrencyCache();

        // when
        currencyConverter.scheduledUpdateCurrencyCache();

        // then
        verify(currencyConverter, times(1)).scheduledUpdateCurrencyCache();
    }
}