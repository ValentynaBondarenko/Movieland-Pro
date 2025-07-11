package com.bondarenko.movieland.entity;

import com.bondarenko.movieland.exception.CurrencyExchangeException;
import lombok.AllArgsConstructor;

//@AllArgsConstructor
public enum CurrencyType {
    AED,
    AUD,
    AZN,
    BDT,
    BGN,
    CAD,
    CHF,
    CNY,
    CZK,
    DKK,
    DZD,
    EGP,
    EUR,
    GBP,
    GEL,
    HKD,
    HUF,
    IDR,
    ILS,
    INR,
    JPY,
    KRW,
    KZT,
    LBP,
    MDL,
    MXN,
    MYR,
    NOK,
    NZD,
    PLN,
    RON,
    RSD,
    SAR,
    SEK,
    SGD,
    THB,
    TND,
    TRY,
    UAH,
    USD,
    VND,
    XDR,
    ZAR;
  //  private final String type;
//
//    private CurrencyType(String type) {
//        this.type = type;
//    }

    public static CurrencyType from(String value) {
        try {
            return value == null ? UAH : CurrencyType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new CurrencyExchangeException("Unsupported currency: " + value);
        }
    }
}
