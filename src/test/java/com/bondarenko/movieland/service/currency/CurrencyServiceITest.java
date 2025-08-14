package com.bondarenko.movieland.service.currency;

import com.bondarenko.movieland.entity.CurrencyType;
import com.bondarenko.movieland.exception.CurrencyExchangeException;
import com.bondarenko.movieland.service.AbstractITest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
class CurrencyServiceITest extends AbstractITest {

    @Autowired
    private CurrencyService currencyService;

    @BeforeEach
    void initCache() {
        ((CurrencyServiceImpl) currencyService).updateCurrencyCache();
    }

    @Test
    void testConvertCurrency_WithRealApi_ShouldReturnNonNullRate() {
        BigDecimal amount = new BigDecimal("100");
        BigDecimal result = currencyService.convertCurrency(amount, CurrencyType.USD);
        assertNotNull(result);
        assertTrue(result.compareTo(BigDecimal.ZERO) > 0, "Converted amount should be greater than zero");
    }

    @Test
    void testConvertCurrency_NullCurrency_ShouldReturnSameAmount() {
        BigDecimal amount = new BigDecimal("50");
        BigDecimal result = currencyService.convertCurrency(amount, null);
        assertEquals(amount, result, "Amount should remain the same if currency is null");
    }

    @Test
    void testConvertCurrency_InvalidCurrency_ShouldThrowException() {
        BigDecimal amount = new BigDecimal("100");
        assertThrows(CurrencyExchangeException.class, () ->
                currencyService.convertCurrency(amount, CurrencyType.from("XYZ"))
        );
    }

    @Test
    void testConvertCurrency_CacheContainsCurrency_ShouldReturnCorrectValue() {
        BigDecimal amount = new BigDecimal("100");

        BigDecimal usdConverted = currencyService.convertCurrency(amount, CurrencyType.USD);
        BigDecimal eurConverted = currencyService.convertCurrency(amount, CurrencyType.EUR);

        assertNotNull(usdConverted);
        assertNotNull(eurConverted);
        assertTrue(usdConverted.compareTo(BigDecimal.ZERO) > 0);
        assertTrue(eurConverted.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void testConvertCurrency_SameCurrency_ShouldReturnSameAmount() {
        BigDecimal amount = new BigDecimal("100");
        BigDecimal result = currencyService.convertCurrency(amount, CurrencyType.UAH);
        assertEquals(amount, result, "Amount should remain the same for UAH");
    }

    @Test
    void testConvertCurrency_ConvertToJPY_ShouldReturnNonNullValue() {
        BigDecimal amount = new BigDecimal("12300");
        BigDecimal result = currencyService.convertCurrency(amount, CurrencyType.JPY);

        assertNotNull(result, "Converted amount for JPY should not be null");
        assertTrue(result.compareTo(BigDecimal.ZERO) > 0, "Converted amount for JPY should be greater than zero");
    }

}