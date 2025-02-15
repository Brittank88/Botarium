package earth.terrarium.botarium.fabric;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.api.energy.EnergyBlock;
import earth.terrarium.botarium.api.energy.EnergyItem;
import earth.terrarium.botarium.api.energy.StatefulEnergyContainer;
import earth.terrarium.botarium.api.fluid.FluidHoldingBlock;
import earth.terrarium.botarium.api.fluid.FluidHoldingItem;
import earth.terrarium.botarium.api.fluid.UpdatingFluidContainer;
import earth.terrarium.botarium.api.item.ItemContainerBlock;
import earth.terrarium.botarium.fabric.energy.FabricBlockEnergyStorage;
import earth.terrarium.botarium.fabric.energy.FabricItemEnergyStorage;
import earth.terrarium.botarium.fabric.fluid.FabricBlockFluidContainer;
import earth.terrarium.botarium.fabric.fluid.FabricItemFluidContainer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.impl.transfer.item.InventoryStorageImpl;
import team.reborn.energy.api.EnergyStorage;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("UnstableApiUsage")
@ParametersAreNonnullByDefault
public class BotariumFabric implements ModInitializer {

    @Override public void onInitialize() {
        Botarium.init();
        EnergyStorage.SIDED.registerFallback((world, pos, state, blockEntity, context) -> {
            if (blockEntity instanceof EnergyBlock energyCapable) {
                StatefulEnergyContainer container = energyCapable.getEnergyStorage().getContainer(context);
                return container == null ? null : new FabricBlockEnergyStorage(container);
            }
            return null;
        });
        EnergyStorage.ITEM.registerFallback((itemStack, context) ->
            itemStack.getItem() instanceof EnergyItem energyCapable ? new FabricItemEnergyStorage(context, energyCapable.getEnergyStorage(itemStack)) : null
        );
        FluidStorage.SIDED.registerFallback((world, pos, state, blockEntity, context) -> {
            if (blockEntity instanceof FluidHoldingBlock fluidHoldingBlock) {
                UpdatingFluidContainer container = fluidHoldingBlock.getFluidContainer().getContainer(context);
                return container == null ? null : new FabricBlockFluidContainer(container);
            }
            return null;
        });
        FluidStorage.ITEM.registerFallback((itemStack, context) ->
            itemStack.getItem() instanceof FluidHoldingItem fluidHoldingBlock ? new FabricItemFluidContainer(context, fluidHoldingBlock.getFluidContainer(itemStack)) : null
        );
        ItemStorage.SIDED.registerFallback((world, pos, state, blockEntity, context) ->
                blockEntity instanceof ItemContainerBlock energyContainer ? InventoryStorageImpl.of(energyContainer.getContainer(), context) : null
        );
    }
}