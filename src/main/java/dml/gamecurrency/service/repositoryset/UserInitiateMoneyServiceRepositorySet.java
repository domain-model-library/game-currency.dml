package dml.gamecurrency.service.repositoryset;

import dml.gamecurrency.repository.*;
import dml.largescaletaskmanagement.repository.LargeScaleTaskSegmentIDGeneratorRepository;

public interface UserInitiateMoneyServiceRepositorySet {

    GameCurrencyAccountRepository getGameCurrencyAccountRepository();

    GameCurrencyAccountIdGeneratorRepository getGameCurrencyAccountIdGeneratorRepository();

    GameUserCurrencyAccountsRepository getGameUserCurrencyAccountsRepository();

    GameCurrencyAccountBillItemRepository getGameCurrencyAccountBillItemRepository();

    GameCurrencyAccountBillItemIdGeneratorRepository getGameCurrencyAccountBillItemIdGeneratorRepository();

    UserInitiateMoneyTaskRepository getUserInitiateMoneyTaskRepository();

    UserInitiateMoneyTaskSegmentRepository getUserInitiateMoneyTaskSegmentRepository();

    LargeScaleTaskSegmentIDGeneratorRepository getUserInitiateMoneyTaskSegmentIDGeneratorRepository();
}
