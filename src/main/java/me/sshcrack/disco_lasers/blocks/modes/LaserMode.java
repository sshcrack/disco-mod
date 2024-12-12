package me.sshcrack.disco_lasers.blocks.modes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import me.sshcrack.disco_lasers.ModLaserModes;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class LaserMode {
    public static Codec<LaserMode> LASER_CODEC = Identifier.CODEC.dispatch(ModLaserModes::getId, type -> ModLaserModes.MODES.get(type).codec());

    public abstract Text getDisplayName();
}
