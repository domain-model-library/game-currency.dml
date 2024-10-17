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

    public List getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List userIdList) {
        this.userIdList = userIdList;
    }
}
