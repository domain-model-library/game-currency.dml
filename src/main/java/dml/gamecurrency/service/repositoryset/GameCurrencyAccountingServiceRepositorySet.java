package dml.gamecurrency.service.repositoryset;

import dml.gamecurrency.repository.*;

public interface GameCurrencyAccountingServiceRepositorySet {
    GameCurrencyAccountRepository getGameCurrencyAccountRepository();

    GameCurrencyAccountIdGeneratorRepository getGameCurrencyAccountIdGeneratorRepository();

    GameUserCurrencyAccountsRepository getGameUserCurrencyAccountsRepository();

    GameCurrencyAccountBillItemRepository getGameCurrencyAccountBillItemRepository();

    GameCurrencyAccountBillItemIdGeneratorRepository getGameCurrencyAccountBillItemIdGeneratorRepository();
}
