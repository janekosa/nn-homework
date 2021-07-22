package pl.janekosa.nn.homework.api;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CurrencyExchangeRequest {
    private String currencyCode;
    private BigDecimal amount;
}
