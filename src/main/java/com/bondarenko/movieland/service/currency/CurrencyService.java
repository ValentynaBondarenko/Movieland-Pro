package com.bondarenko.movieland.service.currency;

import com.bondarenko.movieland.entity.CurrencyType;

import java.math.BigDecimal;

public interface CurrencyService {
    //convertFromUSD
    BigDecimal convertCurrency(BigDecimal price, CurrencyType currencyType);

    // //convertFromUSD
    //    default BigDecimal convertCurrency(BigDecimal price, CurrencyType currencyType) {
    //        return convert(price, CurrencyType.USD, currencyType);
    //    }

    //BigDecimal convert(BigDecimal price, CurrencyType currency, CurrencyType baseCurrency);
}
