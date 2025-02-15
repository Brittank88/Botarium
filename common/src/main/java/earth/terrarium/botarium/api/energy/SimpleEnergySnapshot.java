package earth.terrarium.botarium.api.energy;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SimpleEnergySnapshot implements EnergySnapshot {
    private final long energy;

    public SimpleEnergySnapshot(EnergyContainer container) {
        this.energy = container.getStoredEnergy();
    }

    @Override public void loadSnapshot(EnergyContainer container) {
        container.setEnergy(energy);
    }
}