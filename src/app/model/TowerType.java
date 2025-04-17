package app.model;

import java.util.function.Supplier;

public enum TowerType {
    DART(DartTower::new),
    ICE(IceTower::new),
    TACK(TackTower::new);

    private final Supplier<DefenceTower> constructor;

    TowerType(Supplier<DefenceTower>constructor) {
        this.constructor = constructor;
    }

    public DefenceTower create() {
        return constructor.get();
    }
}
