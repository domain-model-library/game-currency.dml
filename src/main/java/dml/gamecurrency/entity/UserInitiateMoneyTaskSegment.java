package dml.gamecurrency.entity;

import dml.largescaletaskmanagement.entity.LargeScaleTaskSegmentBase;

import java.util.List;

public class UserInitiateMoneyTaskSegment extends LargeScaleTaskSegmentBase {
    private String id;
    private List<Object> userIdList;

    @Override
    public void setId(Object id) {
        this.id = (String) id;
    }

    @Override
    public Object getId() {
        return id;
    }

    public List<Object> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<Object> userIdList) {
        this.userIdList = userIdList;
    }
}
