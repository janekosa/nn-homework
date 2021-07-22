package pl.janekosa.nn.homework.fetcher;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public class ExchangeRates {
    private final BigDecimal bidPrice;
    private final BigDecimal askPrice;
}
