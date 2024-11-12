package dml.gamecurrency.entity;

import dml.largescaletaskmanagement.entity.LargeScaleTaskSegmentBase;

import java.util.List;

public class UserInitiateMoneyTaskSegment extends LargeScaleTaskSegmentBase {
    private String id;
    private List userIdList;

    @Override
    public void setId(Object id) {
        this.id = (String) id;
    }

    @Override
    public Object getId() {
        return id;
    }

    public Object getOneUserId() {
        if (userIdList == null || userIdList.isEmpty()) {
            return null;
        }
        return userIdList.get(0);
    }

    public void executedForUser(Object userId) {
        userIdList.remove(userId);
        if (userIdList.isEmpty()) {
            setCompleted();
        } else {
            resetToProcess();
        }
    }

    public List getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List userIdList) {
        this.userIdList = userIdList;
    }
}
