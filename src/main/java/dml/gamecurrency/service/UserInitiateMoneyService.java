package dml.gamecurrency.service;

import dml.gamecurrency.entity.GameCurrencyAccount;
import dml.gamecurrency.entity.GameCurrencyAccountBillItem;
import dml.gamecurrency.entity.UserInitiateMoneyTask;
import dml.gamecurrency.entity.UserInitiateMoneyTaskSegment;
import dml.gamecurrency.repository.*;
import dml.gamecurrency.service.repositoryset.GameCurrencyAccountingServiceRepositorySet;
import dml.gamecurrency.service.repositoryset.UserInitiateMoneyServiceRepositorySet;
import dml.largescaletaskmanagement.repository.LargeScaleTaskRepository;
import dml.largescaletaskmanagement.repository.LargeScaleTaskSegmentIDGeneratorRepository;
import dml.largescaletaskmanagement.repository.LargeScaleTaskSegmentRepository;
import dml.largescaletaskmanagement.service.LargeScaleTaskService;
import dml.largescaletaskmanagement.service.repositoryset.LargeScaleTaskServiceRepositorySet;
import dml.largescaletaskmanagement.service.result.TakeTaskSegmentToExecuteResult;

import java.util.List;

public class UserInitiateMoneyService {

    /**
     * 如果任务没有完成还需要继续执行，返回true
     */
    public static boolean executeUserInitiateMoneyTask(UserInitiateMoneyServiceRepositorySet repositorySet,
                                                       String taskName, long currentTime,
                                                       long maxSegmentExecutionTime, long maxTimeToTaskReady, int userBatchSize,
                                                       List<Object> userIdList, GameCurrencyAccount newAccount, GameCurrencyAccountBillItem newGameCurrencyAccountBillItem,
                                                       String currency, String amount) {
        UserInitiateMoneyTaskRepository userInitiateMoneyTaskRepository = repositorySet.getUserInitiateMoneyTaskRepository();

        UserInitiateMoneyTask task = userInitiateMoneyTaskRepository.find(taskName);
        if (task.isEmpty()) {
            return false;
        }
        if (task == null) {
            task = (UserInitiateMoneyTask) LargeScaleTaskService.createTask(getLargeScaleTaskServiceRepositorySet(repositorySet),
                    taskName, new UserInitiateMoneyTask(), currentTime);
            if (task != null) {
                if (userIdList.isEmpty()) {
                    return false;
                }
                //分批次
                int size = userIdList.size();
                int batchCount = size / userBatchSize;
                if (size % userBatchSize != 0) {
                    batchCount++;
                }
                for (int i = 0; i < batchCount; i++) {
                    int start = i * userBatchSize;
                    int end = Math.min((i + 1) * userBatchSize, size);
                    List<Object> subList = userIdList.subList(start, end);
                    UserInitiateMoneyTaskSegment segment = new UserInitiateMoneyTaskSegment();
                    segment.setUserIdList(subList);
                    LargeScaleTaskService.addTaskSegment(getLargeScaleTaskServiceRepositorySet(repositorySet),
                            taskName, segment);
                }
                LargeScaleTaskService.setTaskReadyToProcess(getLargeScaleTaskServiceRepositorySet(repositorySet),
                        taskName);
            }
            return true;
        }

        TakeTaskSegmentToExecuteResult takeSegmentResult = LargeScaleTaskService.takeTaskSegmentToExecute(
                getLargeScaleTaskServiceRepositorySet(repositorySet),
                taskName, currentTime, maxSegmentExecutionTime, maxTimeToTaskReady);
        UserInitiateMoneyTaskSegment segment = (UserInitiateMoneyTaskSegment) takeSegmentResult.getTaskSegment();
        if (segment == null) {
            return false;
        }
        List<Object> segmentUserIdList = segment.getUserIdList();
        for (Object userId : segmentUserIdList) {
            GameCurrencyAccount account = GameCurrencyAccountingService.
                    getOrCreateAccount(getGameCurrencyAccountingServiceRepositorySet(repositorySet),
                            userId, currency, newAccount);
            GameCurrencyAccountingService.deposit(getGameCurrencyAccountingServiceRepositorySet(repositorySet),
                    account.getId(), amount, newGameCurrencyAccountBillItem);
        }
        LargeScaleTaskService.completeTaskSegment(getLargeScaleTaskServiceRepositorySet(repositorySet),
                segment.getId());
        return true;

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

            @Override
            public GameCurrencyAccountBillItemIdGeneratorRepository getGameCurrencyAccountBillItemIdGeneratorRepository() {
                return userInitiateMoneyServiceRepositorySet.getGameCurrencyAccountBillItemIdGeneratorRepository();
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

            @Override
            public LargeScaleTaskSegmentIDGeneratorRepository getLargeScaleTaskSegmentIDGeneratorRepository() {
                return userInitiateMoneyServiceRepositorySet.getUserInitiateMoneyTaskSegmentIDGeneratorRepository();
            }
        };
    }
}
