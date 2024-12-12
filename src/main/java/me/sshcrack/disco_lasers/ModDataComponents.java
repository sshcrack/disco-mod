package me.sshcrack.disco_lasers;

import com.anthonyhilyard.prism.util.ConfigHelper;
import com.anthonyhilyard.prism.util.IColor;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Lifecycle;
import me.sshcrack.disco_lasers.blocks.modes.LaserMode;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.Optional;
import java.util.function.UnaryOperator;

import static me.sshcrack.disco_lasers.DiscoLasersVoice.ref;

public class ModDataComponents {
    public static DataResult<IColor> parseColor(String s) {
        var color = ConfigHelper.parseColor(s);

        if (color == null)
            return new DataResult.Error<>(() -> "Invalid color: " + s, Optional.empty(), Lifecycle.stable());

        return new DataResult.Success<>(color, Lifecycle.stable());
    }

    public static final Codec<IColor> DYNAMIC_COLOR_CODEC = Codec.STRING.comapFlatMap(e -> new DataResult.Success<>(ConfigHelper.parseColor(e), Lifecycle.stable()), IColor::toString);

    public static final ComponentType<LaserMode> LASER_MODE = register("laser_mode", builder -> builder
            .codec(LaserMode.LASER_CODEC)
            .packetCodec(PacketCodecs.codec(LaserMode.LASER_CODEC)));


    public static <T> ComponentType<T> register(String path, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, ref(path), builderOperator.apply(ComponentType.builder()).build());
    }

    public static void initialize() {
    }
}
