package me.sshcrack.disco_lasers;

import me.sshcrack.disco_lasers.blocks.LaserBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModBlocks {
    public static <T extends Block> T register(String name, T block, boolean shouldRegisterItem) {
        var id = DiscoLasersVoice.ref(name);

        // Sometimes, you may not want to register an item for the block.
        // Eg: if it's a technical block like `minecraft:air` or `minecraft:end_gateway`
        if (shouldRegisterItem) {
            BlockItem blockItem = new BlockItem(block, new Item.Settings());
            Registry.register(Registries.ITEM, id, blockItem);
        }

        return Registry.register(Registries.BLOCK, id, block);
    }


    public static final LaserBlock LASER_BLOCK = register("laser_block", new LaserBlock(AbstractBlock.Settings.create()), true);

    public static void initialize() {

    }
}
