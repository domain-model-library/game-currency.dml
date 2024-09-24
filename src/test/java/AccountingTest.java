import dml.common.repository.TestCommonRepository;
import dml.common.repository.TestCommonSingletonRepository;
import dml.gamecurrency.entity.GameCurrencyAccount;
import dml.gamecurrency.repository.*;
import dml.gamecurrency.service.GameCurrencyAccountingService;
import dml.gamecurrency.service.repositoryset.GameCurrencyAccountingServiceRepositorySet;
import dml.gamecurrency.service.result.DepositResult;
import dml.gamecurrency.service.result.WithdrawResult;
import dml.id.entity.LongIdGenerator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AccountingTest {

    @Test
    public void test() {
        long userId = 1;
        //创建一个用户的金币账户
        GameCurrencyAccount goldAccount = GameCurrencyAccountingService.createAccount(gameCurrencyAccountingServiceRepositorySet,
                userId, "gold", new TestGameCurrencyAccount());

        //充1000金币
        DepositResult depositResult = GameCurrencyAccountingService.deposit(gameCurrencyAccountingServiceRepositorySet,
                goldAccount.getId(), "1000", new TestGameCurrencyAccountBillItem());
        assertEquals("1000", depositResult.getBillItem().getTransactionAmount());
        assertEquals("1000", depositResult.getAccount().getBalance());
        assertEquals(1, depositResult.getBillItem().getAccountTransactionNumber());

        //花500金币
        WithdrawResult withdrawResult = GameCurrencyAccountingService.withdraw(gameCurrencyAccountingServiceRepositorySet,
                userId, "gold", "500", new TestGameCurrencyAccountBillItem());
        assertEquals("500", withdrawResult.getBillItem().getTransactionAmount());
        assertEquals("500", withdrawResult.getAccount().getBalance());
        assertEquals(2, withdrawResult.getBillItem().getAccountTransactionNumber());
    }

    GameCurrencyAccountRepository gameCurrencyAccountRepository = TestCommonRepository.instance(GameCurrencyAccountRepository.class);
    GameCurrencyAccountIdGeneratorRepository gameCurrencyAccountIdGeneratorRepository =
            TestCommonSingletonRepository.instance(GameCurrencyAccountIdGeneratorRepository.class, new LongIdGenerator(1) {
            });
    GameUserCurrencyAccountsRepository gameUserCurrencyAccountsRepository = TestCommonRepository.instance(GameUserCurrencyAccountsRepository.class);
    GameCurrencyAccountBillItemRepository gameCurrencyAccountBillItemRepository = TestCommonRepository.instance(GameCurrencyAccountBillItemRepository.class);
    GameCurrencyAccountBillItemIdGeneratorRepository gameCurrencyAccountBillItemIdGeneratorRepository =
            TestCommonSingletonRepository.instance(GameCurrencyAccountBillItemIdGeneratorRepository.class, new LongIdGenerator(1) {
            });

    GameCurrencyAccountingServiceRepositorySet gameCurrencyAccountingServiceRepositorySet = new GameCurrencyAccountingServiceRepositorySet() {
        @Override
        public GameCurrencyAccountRepository getGameCurrencyAccountRepository() {
            return gameCurrencyAccountRepository;
        }

        @Override
        public GameCurrencyAccountIdGeneratorRepository getGameCurrencyAccountIdGeneratorRepository() {
            return gameCurrencyAccountIdGeneratorRepository;
        }

        @Override
        public GameUserCurrencyAccountsRepository getGameUserCurrencyAccountsRepository() {
            return gameUserCurrencyAccountsRepository;
        }

        @Override
        public GameCurrencyAccountBillItemRepository getGameCurrencyAccountBillItemRepository() {
            return gameCurrencyAccountBillItemRepository;
        }

        @Override
        public GameCurrencyAccountBillItemIdGeneratorRepository getGameCurrencyAccountBillItemIdGeneratorRepository() {
            return gameCurrencyAccountBillItemIdGeneratorRepository;
        }
    };
}
