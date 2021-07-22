package pl.janekosa.nn.homework.fetcher;


public interface CurrencyRateFetcher {

    ExchangeRates getExchangeRates(String currencyCode);
    default ExchangeRates getUSDExchangeRates() {
        return getExchangeRates("USD");
    }
}
