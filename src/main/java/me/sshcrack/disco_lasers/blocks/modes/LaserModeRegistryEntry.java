package me.sshcrack.disco_lasers.blocks.modes;

import com.mojang.serialization.MapCodec;

public record LaserModeRegistryEntry<T extends LaserMode>(Class<T> modeClass, MapCodec<T> codec) {
}
