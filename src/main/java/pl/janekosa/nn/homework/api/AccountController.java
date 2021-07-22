package pl.janekosa.nn.homework.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.janekosa.nn.homework.persistance.model.MultiCurrencyAccount;
import pl.janekosa.nn.homework.service.AccountOperationsService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountOperationsService accountOperationsService;

    @GetMapping
    public List<MultiCurrencyAccount> getAccounts() {
        return accountOperationsService.getAllAccounts();
    }

    @GetMapping("/{accountId}")
    public MultiCurrencyAccount getAccountById(@PathVariable("accountId") UUID accountId) {
        return accountOperationsService.getAccountById(accountId);
    }

    @PostMapping
    public MultiCurrencyAccount createAccount(@RequestBody CreateAccountRequest request) {
        return accountOperationsService.createAccount(request.getAccountOwner(), request.getInitialPlnBalance());
    }

    @PostMapping("/buy/{accountId}")
    public MultiCurrencyAccount buyCurrency(@PathVariable("accountId") UUID accountId, @RequestBody CurrencyExchangeRequest request) {
        return accountOperationsService.buyCurrency(accountId, request.getCurrencyCode(), request.getAmount());
    }

    @PostMapping("/sell/{accountId}")
    public MultiCurrencyAccount sellCurrency(@PathVariable("accountId") UUID accountId, @RequestBody CurrencyExchangeRequest request) {
        return accountOperationsService.sellCurrency(accountId, request.getCurrencyCode(), request.getAmount());
    }

}
