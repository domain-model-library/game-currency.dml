package dml.gamecurrency.entity;

public interface GameCurrencyAccountBillItemTemplate<T extends GameCurrencyAccountBillItem> {
    T createNew();
}
