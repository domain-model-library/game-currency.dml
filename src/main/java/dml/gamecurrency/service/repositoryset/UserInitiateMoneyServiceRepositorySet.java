package dml.gamecurrency.service.repositoryset;

import dml.gamecurrency.repository.*;

public interface UserInitiateMoneyServiceRepositorySet {

    GameCurrencyAccountRepository getGameCurrencyAccountRepository();

    GameUserCurrencyAccountsRepository getGameUserCurrencyAccountsRepository();

    GameCurrencyAccountBillItemRepository getGameCurrencyAccountBillItemRepository();

    UserInitiateMoneyTaskRepository getUserInitiateMoneyTaskRepository();

    UserInitiateMoneyTaskSegmentRepository getUserInitiateMoneyTaskSegmentRepository();

    UserInitiateMoneyTaskSegmentIDGeneratorRepository getUserInitiateMoneyTaskSegmentIDGeneratorRepository();
}
