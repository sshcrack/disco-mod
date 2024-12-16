package me.sshcrack.disco_lasers.registries;

import io.wispforest.owo.network.OwoNetChannel;
import me.sshcrack.disco_lasers.DiscoLasers;
import net.minecraft.util.math.BlockPos;

public class ModNetworking {
    public static final OwoNetChannel LASER_CONTROL_CHANNEL = OwoNetChannel.create(DiscoLasers.ref("laser_control"));

    public record LaserControlPacket(BlockPos pos, int index) {

    }

    public static void initialize() {
    }
}
