package me.sshcrack.disco_lasers.util.color;

import com.mojang.serialization.Codec;
import me.sshcrack.disco_lasers.ModLaserColor;

public interface LaserColor {
    Codec<LaserColor> CODEC = ModLaserColor.REGISTRY.getSerializableCodec();

    int getARGB();
    void tick(float worldTick, float delta);
}
