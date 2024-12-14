package me.sshcrack.disco_lasers.screen;

import io.wispforest.owo.client.screens.SyncedProperty;
import me.sshcrack.disco_lasers.blocks.LaserBlockEntity;
import me.sshcrack.disco_lasers.blocks.modes.LaserMode;
import me.sshcrack.disco_lasers.registries.ModHandledScreens;
import me.sshcrack.disco_lasers.util.color.LaserColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SingleLaserScreenHandler extends ScreenHandler {
    private static final char[] VOWELS = {'a', 'e', 'i', 'o', 'u'};
    private static final char[] CONSONANTS = {'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'y', 'z'};

    private final ScreenHandlerContext context;
    private boolean blockEntityExists = true;

    public final SyncedProperty<LaserMode> laserMode;

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

        this.laserMode = this.createProperty(LaserMode.class, null);
        var blockEntity = getBlockEntity();
        if (blockEntity != null) {
            this.laserMode.set(blockEntity.getLaserMode());
        }

        addEventListeners();
    }

    private void addEventListeners() {
        if (context == ScreenHandlerContext.EMPTY)
            return;

        laserMode.observe(mode -> {
            context.run((world, pos) -> {
                var entity = world.getBlockEntity(pos);
                if (!(entity instanceof LaserBlockEntity laser)) {
                    blockEntityExists = false;
                    return;
                }

                laser.setLaserMode(mode);
            });
        });
    }


    // made originally by det hoonter tm
    private static String generateEpicName() {
        var sb = new StringBuilder();

        for (int i = 0; i < 4; ++i) {
            var consonant = CONSONANTS[ThreadLocalRandom.current().nextInt(CONSONANTS.length)];

            if (i == 0) consonant = Character.toUpperCase(consonant);

            sb.append(consonant);
            sb.append(VOWELS[ThreadLocalRandom.current().nextInt(VOWELS.length)]);
        }

        return sb.toString();
    }


    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return blockEntityExists;
    }

    public record LaserColorList(List<LaserColor> colors) {
    }

    public record EpicMessage(int number) {
    }

    public record MaldMessage(int number) {
    }
}