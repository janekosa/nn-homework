package pl.janekosa.nn.homework.fetcher.nbp;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.janekosa.nn.homework.exception.BadRequestException;
import pl.janekosa.nn.homework.exception.TechnicalException;
import pl.janekosa.nn.homework.fetcher.CurrencyRateFetcher;
import pl.janekosa.nn.homework.fetcher.ExchangeRates;

@RequiredArgsConstructor
@Component
public class NbpCurrencyRateFetcher implements CurrencyRateFetcher {

    private static final String CURRENCY_EXCHANGE_URL = "http://api.nbp.pl/api/exchangerates/rates/C/%s/today";
    private static final String NO_SUCH_CURRENCY = "404 NotFound - Not Found - Brak danych";
    private final RestTemplate restTemplate;

    @Override
    public ExchangeRates getExchangeRates(String currencyCode) {
        final String requestUrl = String.format(CURRENCY_EXCHANGE_URL, currencyCode);
        ResponseEntity<NbpCurrencyRateResponse> responseEntity;
        try {
            responseEntity = restTemplate.getForEntity(requestUrl, NbpCurrencyRateResponse.class);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode().value() == 404 && NO_SUCH_CURRENCY.equals(ex.getResponseBodyAsString())) {
                throw new BadRequestException(String.format("No such currency %s", currencyCode));
            }
            throw ex;
        }
        return responseEntity.getBody()
                .getRates().stream().findFirst()
                .orElseThrow(() -> new TechnicalException("Exchange rates not found, try again later"))
                .toExchangeRates();

    }




}
