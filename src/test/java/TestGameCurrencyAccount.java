import dml.gamecurrency.entity.GameCurrencyAccountBase;

public class TestGameCurrencyAccount extends GameCurrencyAccountBase {
    private long id;

    @Override
    public void setId(Object id) {
        this.id = (long) id;
    }

    @Override
    public Object getId() {
        return id;
    }
}
