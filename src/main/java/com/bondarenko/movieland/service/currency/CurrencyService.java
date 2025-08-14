package com.bondarenko.movieland.service.currency;

import com.bondarenko.movieland.entity.CurrencyType;

import java.math.BigDecimal;

public interface CurrencyService {
    BigDecimal convertCurrency(BigDecimal price, CurrencyType currencyType);
}
