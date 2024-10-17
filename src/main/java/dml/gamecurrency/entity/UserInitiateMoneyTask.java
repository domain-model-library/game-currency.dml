package dml.gamecurrency.entity;

import dml.largescaletaskmanagement.entity.LargeScaleTaskBase;

public class UserInitiateMoneyTask extends LargeScaleTaskBase {
    private String taskName;

    @Override
    public void setName(String name) {
        this.taskName = name;
    }
}
