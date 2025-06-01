package com.bondarenko.movieland.service.currency;

import java.math.BigDecimal;

public interface CurrencyService {

    BigDecimal convertCurrency(BigDecimal price, String currency);
}
