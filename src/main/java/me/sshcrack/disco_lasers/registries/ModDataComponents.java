package me.sshcrack.disco_lasers.registries;

import me.sshcrack.disco_lasers.blocks.modes.LaserMode;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.function.UnaryOperator;

import static me.sshcrack.disco_lasers.DiscoLasers.ref;

public class ModDataComponents {
    public static final ComponentType<LaserMode> LASER_MODE = register("laser_mode", builder -> builder
            .codec(LaserMode.LASER_CODEC)
            .packetCodec(PacketCodecs.codec(LaserMode.LASER_CODEC)));


    public static <T> ComponentType<T> register(String path, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, ref(path), builderOperator.apply(ComponentType.builder()).build());
    }

    public static void initialize() {
    }
}
