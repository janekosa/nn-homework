package pl.janekosa.nn.homework.fetcher.nbp;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NbpCurrencyRateFetcherTestFixtures {
    private static final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    @SneakyThrows
    static ResponseEntity<NbpCurrencyRateResponse> correctApiAnswer() {
        ResponseEntity<NbpCurrencyRateResponse> response = mock(ResponseEntity.class);
        when(response.getBody()).thenReturn(objectMapper.readValue("{\n" +
                "    \"table\": \"C\",\n" +
                "    \"currency\": \"dolar amerykański\",\n" +
                "    \"code\": \"USD\",\n" +
                "    \"rates\": [\n" +
                "        {\n" +
                "            \"no\": \"140/C/NBP/2021\",\n" +
                "            \"effectiveDate\": \"2021-07-22\",\n" +
                "            \"bid\": 3.8557,\n" +
                "            \"ask\": 3.9335\n" +
                "        }\n" +
                "    ]\n" +
                "}", NbpCurrencyRateResponse.class));
        return response;
    }

    @SneakyThrows
    static ResponseEntity<NbpCurrencyRateResponse> apiAnswerWithoutRates() {
        ResponseEntity<NbpCurrencyRateResponse> response = mock(ResponseEntity.class);
        when(response.getBody()).thenReturn(objectMapper.readValue("{\n" +
                "    \"table\": \"C\",\n" +
                "    \"currency\": \"dolar amerykański\",\n" +
                "    \"code\": \"USD\",\n" +
                "    \"rates\": [\n" +
                "    ]\n" +
                "}", NbpCurrencyRateResponse.class));
        return response;
    }

    static HttpClientErrorException currencyNotFoundException() {
        HttpClientErrorException exception = mock(HttpClientErrorException.class);
        when(exception.getStatusCode()).thenReturn(HttpStatus.NOT_FOUND);
        when(exception.getResponseBodyAsString()).thenReturn("404 NotFound - Not Found - Brak danych");
        return exception;
    }

}
