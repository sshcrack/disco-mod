package me.sshcrack.disco_lasers;

import me.sshcrack.disco_lasers.blocks.LaserBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModBlockEntityTypes {
    public static <T extends BlockEntityType<?>> T register(String path, T type) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, path, type);
    }

    public static final BlockEntityType<LaserBlockEntity> LASER_BLOCK_ENTITY_TYPE = register(
            "laser_block",
            BlockEntityType.Builder.create(LaserBlockEntity::new, ModBlocks.LASER_BLOCK).build()
    );

    public static void initialize() {

    }
}
