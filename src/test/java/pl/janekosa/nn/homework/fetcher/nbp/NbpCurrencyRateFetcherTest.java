package pl.janekosa.nn.homework.fetcher.nbp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.janekosa.nn.homework.exception.BadRequestException;
import pl.janekosa.nn.homework.exception.TechnicalException;
import pl.janekosa.nn.homework.fetcher.ExchangeRates;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NbpCurrencyRateFetcherTest {

    NbpCurrencyRateFetcher objectUnderTest;
    RestTemplate restTemplateMock;


    @BeforeEach
    void setup() {
        restTemplateMock = mock(RestTemplate.class);
        objectUnderTest = new NbpCurrencyRateFetcher(restTemplateMock);
    }

    @Test
    void shouldReturnExchangeRatesIfApiResponds() {
        ResponseEntity<NbpCurrencyRateResponse> correctApiAnswer = NbpCurrencyRateFetcherTestFixtures.correctApiAnswer();
        when(restTemplateMock.getForEntity("http://api.nbp.pl/api/exchangerates/rates/C/USD/today", NbpCurrencyRateResponse.class))
                .thenReturn(correctApiAnswer);

        ExchangeRates exchangeRates = objectUnderTest.getExchangeRates("USD");

        assertThat(exchangeRates).hasFieldOrPropertyWithValue("bidPrice", BigDecimal.valueOf(3.8557));
        assertThat(exchangeRates).hasFieldOrPropertyWithValue("askPrice", BigDecimal.valueOf(3.9335));
    }

    @Test
    void shouldThrowNotFoundExceptionIfGivenBadCurrencyCode() {
        HttpClientErrorException exception = NbpCurrencyRateFetcherTestFixtures.currencyNotFoundException();
        when(restTemplateMock.getForEntity("http://api.nbp.pl/api/exchangerates/rates/C/XXX/today", NbpCurrencyRateResponse.class))
                .thenThrow(exception);
        assertThatThrownBy(() -> objectUnderTest.getExchangeRates("XXX"))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("No such currency XXX");
    }

    @Test
    void shouldThrowTechnicalExceptionIfRatesNotReturnedByApi() {
        ResponseEntity<NbpCurrencyRateResponse> incorrectApiAnswer = NbpCurrencyRateFetcherTestFixtures.apiAnswerWithoutRates();
        when(restTemplateMock.getForEntity("http://api.nbp.pl/api/exchangerates/rates/C/USD/today", NbpCurrencyRateResponse.class))
                .thenReturn(incorrectApiAnswer);

        assertThatThrownBy(() -> objectUnderTest.getExchangeRates("USD"))
                .isInstanceOf(TechnicalException.class)
                .hasMessage("Exchange rates not found, try again later");
    }





}
