package me.sshcrack.disco_lasers.util.color;

import com.mojang.serialization.Codec;
import me.sshcrack.disco_lasers.registries.ModLaserColor;
import net.minecraft.text.Text;

public interface LaserColor {
    Codec<LaserColor> CODEC = ModLaserColor.REGISTRY.getSerializableCodec();

    int getARGB();

    void tick(float worldTick, float delta);

    Text getDisplayName();
}
