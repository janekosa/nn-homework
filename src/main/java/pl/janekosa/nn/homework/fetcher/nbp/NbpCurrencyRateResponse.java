package pl.janekosa.nn.homework.fetcher.nbp;

import lombok.Data;

import java.util.List;

@Data
public class NbpCurrencyRateResponse {
    private List<NbpCurrencyRateEntity> rates;
}
