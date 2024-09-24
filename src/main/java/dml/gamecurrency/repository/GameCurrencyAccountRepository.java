package dml.gamecurrency.repository;

import dml.common.repository.CommonRepository;
import dml.gamecurrency.entity.GameCurrencyAccount;

public interface GameCurrencyAccountRepository<E extends GameCurrencyAccount, ID> extends CommonRepository<E, ID> {
}
