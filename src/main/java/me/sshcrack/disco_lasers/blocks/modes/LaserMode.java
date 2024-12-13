package me.sshcrack.disco_lasers.blocks.modes;

import com.mojang.serialization.Codec;
import me.sshcrack.disco_lasers.ModLaserModes;
import net.minecraft.text.Text;

public abstract class LaserMode {
    public static Codec<LaserMode> LASER_CODEC = ModLaserModes.REGISTRY.getSerializableCodec();

    public abstract Text getDisplayName();
}
