package earth.terrarium.botarium.forge.extensions;

import earth.terrarium.botarium.api.registry.RegistryHelpers;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;

import javax.annotation.ParametersAreNonnullByDefault;

@ClassExtension(RegistryHelpers.class)
@ParametersAreNonnullByDefault
public class RegistryHelpersImpl {

    @ImplementsBaseElement
    public static <T extends AbstractContainerMenu> MenuType<T> createMenuType(RegistryHelpers.MenuFactory<T> factory) {
        return IForgeMenuType.create(factory::create);
    }

    @ImplementsBaseElement
    public static <E extends BlockEntity> BlockEntityType<E> createBlockEntityType(RegistryHelpers.BlockEntityFactory<E> factory, Block... blocks) {
        // noinspection ConstantConditions
        return BlockEntityType.Builder.of(factory::create, blocks).build(null);
    }
}
