package pl.janekosa.nn.homework.persistance.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
public class CurrencySubAccount {

    public CurrencySubAccount() {
    }

    public CurrencySubAccount(String currencyCode) {
        this.currencyCode = currencyCode;
        this.balance = BigDecimal.ZERO;
    }



    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private BigDecimal balance;

    private String currencyCode;

}
