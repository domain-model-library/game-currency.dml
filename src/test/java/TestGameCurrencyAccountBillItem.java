import dml.gamecurrency.entity.GameCurrencyAccountBillItemBase;

public class TestGameCurrencyAccountBillItem extends GameCurrencyAccountBillItemBase {
    private long id;

    public TestGameCurrencyAccountBillItem(long id) {
        this.id = id;
    }

    @Override
    public void setId(Object id) {
        this.id = (long) id;
    }
}
