package pl.janekosa.nn.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.janekosa.nn.homework.exception.BadRequestException;
import pl.janekosa.nn.homework.exception.NotFoundException;
import pl.janekosa.nn.homework.fetcher.CurrencyRateFetcher;
import pl.janekosa.nn.homework.fetcher.ExchangeRates;
import pl.janekosa.nn.homework.persistance.dao.MultiCurrencyAccountDao;
import pl.janekosa.nn.homework.persistance.model.CurrencySubAccount;
import pl.janekosa.nn.homework.persistance.model.MultiCurrencyAccount;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountOperationsService {

    private final MultiCurrencyAccountDao accountDao;
    private final CurrencyRateFetcher fetcher;

    public List<MultiCurrencyAccount> getAllAccounts() {
        return accountDao.findAll();
    }

    public MultiCurrencyAccount getAccountById(UUID accountId) {
        return accountDao.findById(accountId).orElseThrow(() -> new NotFoundException("Could not find account with id " + accountId));
    }

    public MultiCurrencyAccount createAccount(String accountOwner, BigDecimal plnBalance) {
        MultiCurrencyAccount account = new MultiCurrencyAccount();
        account.setOwner(accountOwner);
        account.setPlnBalance(plnBalance.setScale(2, RoundingMode.HALF_UP));
        return accountDao.saveAndFlush(account);
    }

    public MultiCurrencyAccount buyCurrency(UUID accountId, String currencyCode, BigDecimal amount) {
        MultiCurrencyAccount account = getAccountById(accountId);
        final ExchangeRates exchangeRates = fetcher.getExchangeRates(currencyCode);
        BigDecimal plnAmount = amount.multiply(exchangeRates.getAskPrice()).setScale(2, RoundingMode.HALF_UP);
        if (account.getPlnBalance().compareTo(plnAmount) < 0) {
            throw new BadRequestException(String.format("Buying %s %s is not possible, it would cost %s PLN but account balance is %s PLN", amount, currencyCode, plnAmount, account.getPlnBalance()));
        }
        CurrencySubAccount subAccount = account.getCurrencyAccounts().stream().filter(a -> a.getCurrencyCode().equals(currencyCode)).findFirst().orElseGet(() -> new CurrencySubAccount(currencyCode));
        account.getCurrencyAccounts().add(subAccount);
        subAccount.setBalance(subAccount.getBalance().add(amount));
        account.setPlnBalance(account.getPlnBalance().subtract(plnAmount));
        return accountDao.saveAndFlush(account);
    }

    public MultiCurrencyAccount sellCurrency(UUID accountId, String currencyCode, BigDecimal amount) {
        MultiCurrencyAccount account = getAccountById(accountId);
        final ExchangeRates exchangeRates = fetcher.getExchangeRates(currencyCode);
        BigDecimal plnAmount = amount.multiply(exchangeRates.getBidPrice()).setScale(2, RoundingMode.HALF_UP);
        CurrencySubAccount subAccount = account.getCurrencyAccounts().stream().filter(a -> a.getCurrencyCode().equals(currencyCode)).findFirst().orElseGet(() -> new CurrencySubAccount(currencyCode));
        account.getCurrencyAccounts().add(subAccount);
        if (subAccount.getBalance().compareTo(amount) < 0) {
            throw new BadRequestException(String.format("Selling %s %s is not possible, only %s %s are available", amount, currencyCode, subAccount.getBalance(), currencyCode));
        }
        subAccount.setBalance(subAccount.getBalance().subtract(amount));
        account.setPlnBalance(account.getPlnBalance().add(plnAmount));
        return accountDao.saveAndFlush(account);
    }
}
