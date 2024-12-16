package me.sshcrack.disco_lasers.blocks;

import me.sshcrack.disco_lasers.blocks.modes.LaserMode;
import me.sshcrack.disco_lasers.blocks.modes.SpreadMode;
import me.sshcrack.disco_lasers.registries.ModBlockEntityTypes;
import me.sshcrack.disco_lasers.registries.ModNetworking;
import me.sshcrack.disco_lasers.screen.SingleLaserScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LaserBlockEntity extends BlockEntity implements NamedScreenHandlerFactory {
    public Object renderer;

    protected int currentIndex;
    protected float distance;
    protected List<LaserMode> modes;


    public static LaserMode createFallback() {
        //return new RandomMode(45 * MathHelper.RADIANS_PER_DEGREE, 10, 2f, 50, createRainbowColors(50));
        return new SpreadMode();
    }

    public LaserBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.LASER_BLOCK_ENTITY_TYPE, pos, state);
    }


    public static void tick(World world, BlockPos pos, BlockState state, LaserBlockEntity blockEntity) {

    }

    @Nullable
    public LaserMode getCurrentLaserMode() {
        if (modes == null || currentIndex < 0 || currentIndex >= modes.size()) {
            return null;
        }

        return modes.get(currentIndex);
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        if (world != null && !world.isClient) {
            this.markDirty();
            ModNetworking.LASER_CONTROL_CHANNEL.serverHandle(this).send(new ModNetworking.LaserDistanceSet(pos, distance));
        }

        this.distance = distance;
    }

    public List<LaserMode> getLaserModes() {
        return modes;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int index) {
        if (world != null && !world.isClient) {
            this.markDirty();
            ModNetworking.LASER_CONTROL_CHANNEL.serverHandle(this).send(new ModNetworking.LaserControlPacket(pos, index));
        }

        this.currentIndex = index;
    }

    public void setLaserModes(List<LaserMode> modes) {
        this.modes = modes;
    }

    public void removeLaserMode(LaserMode mode) {
        this.modes.remove(mode);
        if (currentIndex >= modes.size()) {
            currentIndex = modes.size() - 1;
        }
    }

    public void addLaserMode(LaserMode mode) {
        this.modes.add(mode);
    }

    @Override
    public Text getDisplayName() {
        return Text.empty();
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new SingleLaserScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(world, pos));
    }


    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        if (nbt.contains("laser_mode")) {
            modes = LaserMode.LASER_CODEC.listOf().decode(NbtOps.INSTANCE, nbt.get("laser_mode")).getOrThrow().getFirst();
        }

        if (nbt.contains("laser_distance")) {
            distance = nbt.getFloat("laser_distance");
        } else {
            distance = 10;
        }

        if (nbt.contains("laser_index"))
            currentIndex = nbt.getInt("laser_index");
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        if (modes == null)
            modes = new ArrayList<>(List.of(createFallback()));
        if(distance == 0)
            distance = 10;

        nbt.put("laser_mode", LaserMode.LASER_CODEC.listOf().encodeStart(NbtOps.INSTANCE, modes).getOrThrow());
        nbt.putInt("laser_index", currentIndex);
        nbt.putFloat("laser_distance", distance);
    }


    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        var compound = new NbtCompound();
        writeNbt(compound, registryLookup);

        return compound;
    }
}
