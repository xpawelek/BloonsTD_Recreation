package app.model;

import java.util.function.Supplier;

public enum TowerType {
    DART(DartTower::new);

    private final Supplier<DartTower> constructor;

    TowerType(Supplier<DartTower>constructor) {
        this.constructor = constructor;
    }

    public DeffenceTower create() {
        return constructor.get();
    }
}
