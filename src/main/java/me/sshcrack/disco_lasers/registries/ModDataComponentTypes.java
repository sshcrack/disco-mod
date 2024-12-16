package me.sshcrack.disco_lasers.registries;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.sshcrack.disco_lasers.DiscoLasers;
import me.sshcrack.disco_lasers.blocks.modes.LaserMode;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class ModDataComponentTypes {
    public static final ComponentType<List<BlockPos>> LINKED_LASERS = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            DiscoLasers.ref("linked_lasers"),
            ComponentType.<List<BlockPos>>builder().codec(BlockPos.CODEC.listOf()).build()
    );

    public record LaserData(List<LaserMode> mode, int index, float distance) {
    }

    public static final MapCodec<LaserData> LASER_DATA_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            LaserMode.LASER_CODEC.listOf().fieldOf("modes").forGetter(LaserData::mode),
            Codec.INT.fieldOf("index").forGetter(LaserData::index),
            Codec.FLOAT.fieldOf("distance").forGetter(LaserData::distance)
    ).apply(instance, LaserData::new));


    public static final ComponentType<LaserData> LASER_DATA = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            DiscoLasers.ref("laser_data"),
            ComponentType.<LaserData>builder().codec(LASER_DATA_CODEC.codec()).build()
    );

    public static void initialize() {

    }
}
