import dml.common.repository.TestCommonRepository;
import dml.common.repository.TestCommonSingletonRepository;
import dml.gamecurrency.repository.*;
import dml.gamecurrency.service.UserInitiateMoneyService;
import dml.gamecurrency.service.repositoryset.UserInitiateMoneyServiceRepositorySet;
import dml.id.entity.LongIdGenerator;
import dml.id.entity.UUIDGenerator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserInitiateMoneyTest {

    @Test
    public void test() {
        long maxSegmentExecutionTime = 1000;
        long maxTimeToTaskReady = 1000;
        long currentTime = 0;
        //创建任务
        boolean createTaskSuccess = UserInitiateMoneyService.createUserInitiateMoneyTask(userInitiateMoneyServiceRepositorySet,
                "task1", 1);
        assertTrue(createTaskSuccess);

        //添加UserId到任务
        List userIdList = new ArrayList();
        userIdList.add("1");
        userIdList.add("2");
        UserInitiateMoneyService.addAllUserIdToUserInitiateMoneyTask(userInitiateMoneyServiceRepositorySet,
                "task1", 2, userIdList);

        //执行任务(为用户1)
        UserInitiateMoneyService.executeUserInitiateMoneyTask(userInitiateMoneyServiceRepositorySet,
                "task1", currentTime, maxSegmentExecutionTime, maxTimeToTaskReady,
                new TestGameCurrencyAccount(), new TestGameCurrencyAccountBillItem(gameCurrencyAccountBillItemIdGenerator++),
                "gold", "1000");

        //检测任务是否完成
        boolean completed = UserInitiateMoneyService.isUserInitiateMoneyTaskCompleted(userInitiateMoneyServiceRepositorySet,
                "task1");
        assertFalse(completed);

        //执行任务(为用户2)
        UserInitiateMoneyService.executeUserInitiateMoneyTask(userInitiateMoneyServiceRepositorySet,
                "task1", currentTime, maxSegmentExecutionTime, maxTimeToTaskReady,
                new TestGameCurrencyAccount(), new TestGameCurrencyAccountBillItem(gameCurrencyAccountBillItemIdGenerator++),
                "gold", "1000");

        //执行任务(为了触发任务结束)
        UserInitiateMoneyService.executeUserInitiateMoneyTask(userInitiateMoneyServiceRepositorySet,
                "task1", currentTime, maxSegmentExecutionTime, maxTimeToTaskReady,
                new TestGameCurrencyAccount(), new TestGameCurrencyAccountBillItem(gameCurrencyAccountBillItemIdGenerator++),
                "gold", "1000");

        //检测任务是否完成
        completed = UserInitiateMoneyService.isUserInitiateMoneyTaskCompleted(userInitiateMoneyServiceRepositorySet,
                "task1");
        assertTrue(completed);
    }

    UserInitiateMoneyServiceRepositorySet userInitiateMoneyServiceRepositorySet = new UserInitiateMoneyServiceRepositorySet() {
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
        public UserInitiateMoneyTaskRepository getUserInitiateMoneyTaskRepository() {
            return userInitiateMoneyTaskRepository;
        }

        @Override
        public UserInitiateMoneyTaskSegmentRepository getUserInitiateMoneyTaskSegmentRepository() {
            return userInitiateMoneyTaskSegmentRepository;
        }

        @Override
        public UserInitiateMoneyTaskSegmentIDGeneratorRepository getUserInitiateMoneyTaskSegmentIDGeneratorRepository() {
            return userInitiateMoneyTaskSegmentIDGeneratorRepository;
        }

    };

    GameCurrencyAccountRepository gameCurrencyAccountRepository = TestCommonRepository.instance(GameCurrencyAccountRepository.class);
    GameCurrencyAccountIdGeneratorRepository gameCurrencyAccountIdGeneratorRepository =
            TestCommonSingletonRepository.instance(GameCurrencyAccountIdGeneratorRepository.class, new LongIdGenerator(1));
    GameUserCurrencyAccountsRepository gameUserCurrencyAccountsRepository = TestCommonRepository.instance(GameUserCurrencyAccountsRepository.class);
    GameCurrencyAccountBillItemRepository gameCurrencyAccountBillItemRepository = TestCommonRepository.instance(GameCurrencyAccountBillItemRepository.class);
    long gameCurrencyAccountBillItemIdGenerator = 1L;
    UserInitiateMoneyTaskRepository userInitiateMoneyTaskRepository = TestCommonRepository.instance(UserInitiateMoneyTaskRepository.class);
    UserInitiateMoneyTaskSegmentRepository userInitiateMoneyTaskSegmentRepository = TestCommonRepository.instance(UserInitiateMoneyTaskSegmentRepository.class);
    UserInitiateMoneyTaskSegmentIDGeneratorRepository userInitiateMoneyTaskSegmentIDGeneratorRepository = TestCommonSingletonRepository.instance(UserInitiateMoneyTaskSegmentIDGeneratorRepository.class,
            new UUIDGenerator());

}
