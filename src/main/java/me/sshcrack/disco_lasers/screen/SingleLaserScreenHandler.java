package me.sshcrack.disco_lasers.screen;

import io.wispforest.endec.Endec;
import io.wispforest.owo.serialization.format.nbt.NbtEndec;
import me.sshcrack.disco_lasers.blocks.LaserBlockEntity;
import me.sshcrack.disco_lasers.blocks.modes.LaserMode;
import me.sshcrack.disco_lasers.registries.ModHandledScreens;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtOps;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SingleLaserScreenHandler extends ScreenHandler {
    public static final Endec<LaserMode> LASER_MODE_ENDEC = NbtEndec.ELEMENT.xmap(
            nbt -> LaserMode.LASER_CODEC.decode(NbtOps.INSTANCE, nbt).getOrThrow().getFirst(),
            e -> LaserMode.LASER_CODEC.encodeStart(NbtOps.INSTANCE, e).getOrThrow()
    );

    private final ScreenHandlerContext context;
    private final List<Consumer<LaserMode>> initialModeListeners = new ArrayList<>();

    private LaserMode laserMode;

    public SingleLaserScreenHandler(int syncId, PlayerInventory inventory) {
        this(syncId, inventory, ScreenHandlerContext.EMPTY);
    }

    private @Nullable LaserBlockEntity getBlockEntity() {
        return context.get((world, pos) -> {
            var entity = world.getBlockEntity(pos);
            if (!(entity instanceof LaserBlockEntity laser))
                return null;
            return laser;
        }).orElse(null);
    }

    public SingleLaserScreenHandler(int syncId, PlayerInventory inventory, ScreenHandlerContext context) {
        super(ModHandledScreens.SINGLE_LASER_HANDLE_TYPE, syncId);
        this.context = context;

        this.endecBuilder().register(LASER_MODE_ENDEC, LaserMode.class);
        this.addServerboundMessage(SetBlockEntityLaserMode.class, this::handleSetBlockEntity);
        this.addClientboundMessage(SetInitialMode.class, msg -> {
            laserMode = msg.mode;
            initialModeListeners.forEach(listener -> listener.accept(msg.mode));
        });
    }

    public void sendInitialPacket() {
        var blockEntity = getBlockEntity();
        if (blockEntity != null)
            sendMessage(new SetInitialMode(blockEntity.getLaserMode()));
    }

    public void registerInitialModeListener(Consumer<LaserMode> listener, boolean notifyIfAlreadySet) {
        initialModeListeners.add(listener);

        if (notifyIfAlreadySet && laserMode != null)
            listener.accept(laserMode);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    /**
     * Packet to be sent when the client opens the screen
     */
    public record SetInitialMode(LaserMode mode) {

    }

    private void handleSetBlockEntity(SetBlockEntityLaserMode msg) {
        var blockEntity = getBlockEntity();
        if (blockEntity == null)
            return;

        blockEntity.setLaserMode(msg.mode);
        this.context.run((world, pos) -> {
            ((ServerWorld) world).getChunkManager().markForUpdate(pos);
        });
    }

    /**
     * Packet to be sent when the client changes the mode
     */
    public record SetBlockEntityLaserMode(LaserMode mode) {

    }
}