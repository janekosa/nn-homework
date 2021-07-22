package pl.janekosa.nn.homework.fetcher.nbp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import pl.janekosa.nn.homework.fetcher.ExchangeRates;

import java.math.BigDecimal;


@Data
public class NbpCurrencyRateEntity {
    @JsonProperty("bid")
    private BigDecimal bidPrice;
    @JsonProperty("ask")
    private BigDecimal askPrice;

    ExchangeRates toExchangeRates() {
        return new ExchangeRates(bidPrice, askPrice);
    }
}
