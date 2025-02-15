package earth.terrarium.botarium.fabric.fluid;

import earth.terrarium.botarium.api.fluid.FluidHolder;
import earth.terrarium.botarium.api.fluid.PlatformFluidHandler;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
@ParametersAreNonnullByDefault
public class FabricFluidHandler implements PlatformFluidHandler {

    private final Storage<FluidVariant> storage;

    public FabricFluidHandler(Storage<FluidVariant> storage) {
        this.storage = storage;
    }

    @Override public long insertFluid(FluidHolder fluid, boolean simulate) {
        try (Transaction transaction = Transaction.openOuter()) {
            FabricFluidHolder fabricFluidHolder = FabricFluidHolder.of(fluid);
            long inserted = storage.insert(fabricFluidHolder.toVariant(), fabricFluidHolder.getAmount(), transaction);
            if (!simulate) transaction.commit();
            return inserted;
        }
    }

    @Override public FluidHolder extractFluid(FluidHolder fluid, boolean simulate) {
        try (Transaction transaction = Transaction.openOuter()) {
            FabricFluidHolder fabricFluidHolder = FabricFluidHolder.of(fluid);
            long extracted = storage.extract(fabricFluidHolder.toVariant(), fabricFluidHolder.getAmount(), transaction);
            if (!simulate) transaction.commit();
            return extracted == 0 ? FabricFluidHolder.of(fabricFluidHolder.toVariant(), extracted) : FabricFluidHolder.EMPTY;
        }
    }

    @Override public int getTankAmount() {
        int size = 0;
        while (storage.iterator().hasNext()) size++;
        return size;
    }

    @Override public FluidHolder getFluidInTank(int tank) {
        List<FluidHolder> fluids = new ArrayList<>();
        storage.iterator().forEachRemaining(variant -> fluids.add(FabricFluidHolder.of(variant.getResource(), variant.getAmount())));
        return fluids.get(tank);
    }
}
