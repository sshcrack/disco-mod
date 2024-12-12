package me.sshcrack.disco_lasers.blocks;

import com.mojang.serialization.MapCodec;
import me.sshcrack.disco_lasers.ModBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class LaserBlock extends BlockWithEntity {
    public LaserBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends LaserBlock> getCodec() {
        return createCodec(LaserBlock::new);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new LaserBlockEntity(pos, state);
    }


    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, ModBlockEntityTypes.LASER_BLOCK_ENTITY_TYPE, LaserBlockEntity::tick);
    }
}
