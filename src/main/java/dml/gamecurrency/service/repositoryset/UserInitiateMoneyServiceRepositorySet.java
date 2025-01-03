package dml.gamecurrency.service.repositoryset;

import dml.gamecurrency.repository.*;

public interface UserInitiateMoneyServiceRepositorySet {

    GameCurrencyAccountRepository getGameCurrencyAccountRepository();

    GameCurrencyAccountIdGeneratorRepository getGameCurrencyAccountIdGeneratorRepository();

    GameUserCurrencyAccountsRepository getGameUserCurrencyAccountsRepository();

    GameCurrencyAccountBillItemRepository getGameCurrencyAccountBillItemRepository();

    GameCurrencyAccountBillItemIdGeneratorRepository getGameCurrencyAccountBillItemIdGeneratorRepository();

    UserInitiateMoneyTaskRepository getUserInitiateMoneyTaskRepository();

    UserInitiateMoneyTaskSegmentRepository getUserInitiateMoneyTaskSegmentRepository();

    UserInitiateMoneyTaskSegmentIDGeneratorRepository getUserInitiateMoneyTaskSegmentIDGeneratorRepository();
}
