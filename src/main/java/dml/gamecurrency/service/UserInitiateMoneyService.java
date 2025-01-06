package dml.gamecurrency.service;

import dml.gamecurrency.entity.GameCurrencyAccount;
import dml.gamecurrency.entity.GameCurrencyAccountBillItem;
import dml.gamecurrency.entity.UserInitiateMoneyTask;
import dml.gamecurrency.entity.UserInitiateMoneyTaskSegment;
import dml.gamecurrency.repository.*;
import dml.gamecurrency.service.repositoryset.GameCurrencyAccountingServiceRepositorySet;
import dml.gamecurrency.service.repositoryset.UserInitiateMoneyServiceRepositorySet;
import dml.largescaletaskmanagement.repository.LargeScaleTaskRepository;
import dml.largescaletaskmanagement.repository.LargeScaleTaskSegmentRepository;
import dml.largescaletaskmanagement.service.LargeScaleTaskService;
import dml.largescaletaskmanagement.service.repositoryset.LargeScaleTaskServiceRepositorySet;
import dml.largescaletaskmanagement.service.result.TakeTaskSegmentToExecuteResult;

import java.util.List;

public class UserInitiateMoneyService {

    public static boolean isUserInitiateMoneyTaskCompleted(UserInitiateMoneyServiceRepositorySet repositorySet,
                                                           String taskName) {
        UserInitiateMoneyTaskRepository userInitiateMoneyTaskRepository = repositorySet.getUserInitiateMoneyTaskRepository();
        UserInitiateMoneyTask task = userInitiateMoneyTaskRepository.find(taskName);
        if (task == null) {
            return false;
        }
        return task.isCompleted();
    }

    /**
     * 创建任务
     *
     * @return 如果任务已经存在，返回false
     */
    public static boolean createUserInitiateMoneyTask(UserInitiateMoneyServiceRepositorySet repositorySet,
                                                      String taskName, long currentTime) {
        UserInitiateMoneyTaskRepository userInitiateMoneyTaskRepository = repositorySet.getUserInitiateMoneyTaskRepository();

        UserInitiateMoneyTask task = userInitiateMoneyTaskRepository.find(taskName);
        if (task == null) {
            task = (UserInitiateMoneyTask) LargeScaleTaskService.createTask(getLargeScaleTaskServiceRepositorySet(repositorySet),
                    taskName, new UserInitiateMoneyTask(), currentTime);
            return task != null;
        }
        return false;
    }

    public static void addAllUserIdToUserInitiateMoneyTask(UserInitiateMoneyServiceRepositorySet repositorySet,
                                                           String taskName, int userBatchSize, List userIdList) {
        UserInitiateMoneyTaskSegmentIDGeneratorRepository userInitiateMoneyTaskSegmentIDGeneratorRepository =
                repositorySet.getUserInitiateMoneyTaskSegmentIDGeneratorRepository();
        //分批次
        int size = userIdList.size();
        int batchCount = size / userBatchSize;
        if (size % userBatchSize != 0) {
            batchCount++;
        }
        for (int i = 0; i < batchCount; i++) {
            int start = i * userBatchSize;
            int end = Math.min((i + 1) * userBatchSize, size);
            List subList = userIdList.subList(start, end);
            UserInitiateMoneyTaskSegment segment = new UserInitiateMoneyTaskSegment();
            segment.setId(userInitiateMoneyTaskSegmentIDGeneratorRepository.take().generateId());
            segment.setUserIdList(subList);
            LargeScaleTaskService.addTaskSegment(getLargeScaleTaskServiceRepositorySet(repositorySet),
                    taskName, segment);
        }
        LargeScaleTaskService.setTaskReadyToProcess(getLargeScaleTaskServiceRepositorySet(repositorySet),
                taskName);
    }

    public static Object executeUserInitiateMoneyTask(UserInitiateMoneyServiceRepositorySet repositorySet,
                                                      String taskName, long currentTime,
                                                      long maxSegmentExecutionTime, long maxTimeToTaskReady,
                                                      GameCurrencyAccount newAccount,
                                                      GameCurrencyAccountBillItem newAccountBillItem,
                                                      String currency, String amount) {

        TakeTaskSegmentToExecuteResult takeSegmentResult = LargeScaleTaskService.takeTaskSegmentToExecute(
                getLargeScaleTaskServiceRepositorySet(repositorySet),
                taskName, currentTime, maxSegmentExecutionTime, maxTimeToTaskReady);
        UserInitiateMoneyTaskSegment segment = (UserInitiateMoneyTaskSegment) takeSegmentResult.getTaskSegment();
        if (segment == null) {
            return null;
        }
        Object userId = segment.getOneUserId();//一次只处理一个用户，避免锁多个用户
        GameCurrencyAccount account = GameCurrencyAccountingService.
                getOrCreateAccount(getGameCurrencyAccountingServiceRepositorySet(repositorySet),
                        userId, currency, newAccount);
        GameCurrencyAccountingService.deposit(getGameCurrencyAccountingServiceRepositorySet(repositorySet),
                account.getId(), amount, newAccountBillItem);
        segment.executedForUser(userId);
        return userId;
    }

    private static GameCurrencyAccountingServiceRepositorySet getGameCurrencyAccountingServiceRepositorySet(
            UserInitiateMoneyServiceRepositorySet userInitiateMoneyServiceRepositorySet) {
        return new GameCurrencyAccountingServiceRepositorySet() {

            @Override
            public GameCurrencyAccountRepository getGameCurrencyAccountRepository() {
                return userInitiateMoneyServiceRepositorySet.getGameCurrencyAccountRepository();
            }

            @Override
            public GameCurrencyAccountIdGeneratorRepository getGameCurrencyAccountIdGeneratorRepository() {
                return userInitiateMoneyServiceRepositorySet.getGameCurrencyAccountIdGeneratorRepository();
            }

            @Override
            public GameUserCurrencyAccountsRepository getGameUserCurrencyAccountsRepository() {
                return userInitiateMoneyServiceRepositorySet.getGameUserCurrencyAccountsRepository();
            }

            @Override
            public GameCurrencyAccountBillItemRepository getGameCurrencyAccountBillItemRepository() {
                return userInitiateMoneyServiceRepositorySet.getGameCurrencyAccountBillItemRepository();
            }

        };
    }

    private static LargeScaleTaskServiceRepositorySet getLargeScaleTaskServiceRepositorySet(
            UserInitiateMoneyServiceRepositorySet userInitiateMoneyServiceRepositorySet) {
        return new LargeScaleTaskServiceRepositorySet() {
            @Override
            public LargeScaleTaskRepository getLargeScaleTaskRepository() {
                return userInitiateMoneyServiceRepositorySet.getUserInitiateMoneyTaskRepository();
            }

            @Override
            public LargeScaleTaskSegmentRepository getLargeScaleTaskSegmentRepository() {
                return userInitiateMoneyServiceRepositorySet.getUserInitiateMoneyTaskSegmentRepository();
            }
        };
    }
}
