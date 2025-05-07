package com.bondarenko.movieland.service.converter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CurrencyExchange {
    @JsonProperty("cc")
    private String code;

    @JsonProperty("rate")
    private BigDecimal rate;
    @JsonProperty("r030")
    private int r030;

    @JsonProperty("txt")
    private String txt;

    @JsonProperty("exchangedate")
    private String exchangeDate;

}
