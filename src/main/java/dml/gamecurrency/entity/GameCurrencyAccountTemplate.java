package dml.gamecurrency.entity;

public interface GameCurrencyAccountTemplate<T extends GameCurrencyAccount> {
    T createNew();
}
