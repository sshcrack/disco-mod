package me.sshcrack.disco_lasers.util;

import com.mojang.serialization.MapCodec;
import me.sshcrack.disco_lasers.blocks.modes.LaserMode;

public record GeneralRegistryEntry<T>(Class<T> instanceClass, MapCodec<T> codec) {
}
