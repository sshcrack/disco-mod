package me.sshcrack.disco_lasers.blocks;

import me.sshcrack.disco_lasers.ModBlockEntityTypes;
import me.sshcrack.disco_lasers.ModDataComponents;
import me.sshcrack.disco_lasers.blocks.modes.LaserMode;
import me.sshcrack.disco_lasers.blocks.modes.RandomMode;
import me.sshcrack.disco_lasers.blocks.modes.SpreadMode;
import me.sshcrack.disco_lasers.util.color.LaserColor;
import me.sshcrack.disco_lasers.util.color.RainbowColor;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class LaserBlockEntity extends BlockEntity {
    private static List<LaserColor> createRainbowColors(int amount) {
        var colors = new LaserColor[amount];
        for (int i = 0; i < amount; i++) {
            colors[i] = new RainbowColor((float) i / amount, 5f);
        }
        return List.of(colors);
    }

    public static LaserMode createFallback() {
        //return new RandomMode(45 * MathHelper.RADIANS_PER_DEGREE, 10, 2f, 50, createRainbowColors(50));
        return new SpreadMode(createRainbowColors(100), 180 * MathHelper.RADIANS_PER_DEGREE, 40 * MathHelper.RADIANS_PER_DEGREE, 1);
    }

    public LaserMode mode;

    public LaserBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.LASER_BLOCK_ENTITY_TYPE, pos, state);
    }

    @Override
    protected void readComponents(ComponentsAccess components) {
        super.readComponents(components);
        mode = components.getOrDefault(ModDataComponents.LASER_MODE, createFallback());
    }

    @Override
    protected void addComponents(ComponentMap.Builder builder) {
        super.addComponents(builder);
        builder.add(ModDataComponents.LASER_MODE, mode);
    }

    @Override
    public void removeFromCopiedStackNbt(NbtCompound nbt) {
        nbt.remove("laser_mode");
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }


    public static void tick(World world, BlockPos pos, BlockState state, LaserBlockEntity blockEntity) {

    }

    public LaserMode getLaserMode() {
        if (mode == null)
            mode = createFallback();

        return mode;
    }
}
