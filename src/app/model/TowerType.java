package app.model;

import java.util.function.Supplier;

public enum TowerType {
    DART(DartTower::new),
    ICE(IceTower::new),
    TACK(TackTower::new);

    private final Supplier<DeffenceTower> constructor;

    TowerType(Supplier<DeffenceTower>constructor) {
        this.constructor = constructor;
    }

    public DeffenceTower create() {
        return constructor.get();
    }
}
