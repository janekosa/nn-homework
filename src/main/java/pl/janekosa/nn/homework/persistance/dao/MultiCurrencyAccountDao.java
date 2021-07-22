package pl.janekosa.nn.homework.persistance.dao;



import org.springframework.data.jpa.repository.JpaRepository;
import pl.janekosa.nn.homework.persistance.model.MultiCurrencyAccount;

import java.util.UUID;

public interface MultiCurrencyAccountDao extends JpaRepository<MultiCurrencyAccount, UUID> {
}
