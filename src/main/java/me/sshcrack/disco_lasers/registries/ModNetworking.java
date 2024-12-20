package me.sshcrack.disco_lasers.registries;

import io.wispforest.owo.network.OwoNetChannel;
import me.sshcrack.disco_lasers.DiscoLasers;
import me.sshcrack.disco_lasers.blocks.LaserBlockEntity;
import me.sshcrack.disco_lasers.blocks.modes.OffMode;
import me.sshcrack.disco_lasers.items.LaserControllerItem;
import net.minecraft.util.math.BlockPos;

public class ModNetworking {
    public static final OwoNetChannel LASER_CONTROL_CHANNEL = OwoNetChannel.create(DiscoLasers.ref("laser_control"));

    public record LaserControlPacket(BlockPos pos, int index) {

    }

    public record LaserDistanceSet(BlockPos pos, float distance) {

    }

    public record AllLasersOff() {

    }

    public static void initialize() {
        LASER_CONTROL_CHANNEL.registerClientboundDeferred(ModNetworking.LaserControlPacket.class);
        LASER_CONTROL_CHANNEL.registerClientboundDeferred(ModNetworking.LaserDistanceSet.class);
        LASER_CONTROL_CHANNEL.registerServerbound(ModNetworking.AllLasersOff.class, (packet, ctx) -> {
            var stack = ctx.player().getMainHandStack();
            if (!(stack.getItem() instanceof LaserControllerItem control))
                return;

            var lasers = control.getLinkedLasers(stack);
            for (BlockPos laser : lasers) {
                var blockEntity = ctx.player().getWorld().getBlockEntity(laser);
                if (!(blockEntity instanceof LaserBlockEntity laserBlockEntity))
                    continue;

                var offIndex = -1;
                var list = laserBlockEntity.getLaserModes();
                for (int i = 0; i < list.size(); i++) {
                    var mode = list.get(i);

                    if (!(mode instanceof OffMode))
                        continue;

                    offIndex = i;
                }

                if (offIndex != -1) {
                    laserBlockEntity.setCurrentIndex(offIndex);
                }
            }
        });
    }
}
