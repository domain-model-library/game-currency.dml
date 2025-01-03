package dml.gamecurrency.service.repositoryset;

import dml.gamecurrency.repository.*;
import dml.largescaletaskmanagement.repository.LargeScaleTaskSegmentIDGeneratorRepository;

public interface UserInitiateMoneyServiceRepositorySet {

    GameCurrencyAccountRepository getGameCurrencyAccountRepository();

    GameUserCurrencyAccountsRepository getGameUserCurrencyAccountsRepository();

    GameCurrencyAccountBillItemRepository getGameCurrencyAccountBillItemRepository();

    UserInitiateMoneyTaskRepository getUserInitiateMoneyTaskRepository();

    UserInitiateMoneyTaskSegmentRepository getUserInitiateMoneyTaskSegmentRepository();

    LargeScaleTaskSegmentIDGeneratorRepository getUserInitiateMoneyTaskSegmentIDGeneratorRepository();
}
