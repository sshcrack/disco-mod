package me.sshcrack.disco_lasers.util.color;

import com.mojang.serialization.Codec;
import me.sshcrack.disco_lasers.registries.ModLaserColor;
import me.sshcrack.disco_lasers.screen.UiManageable;
import net.minecraft.text.Text;

public abstract class LaserColor implements Cloneable {
    public static Codec<LaserColor> CODEC = ModLaserColor.REGISTRY.getSerializableCodec();

    public abstract int getARGB();

    public abstract void tick(float worldTick, float delta);

    public abstract Text getDisplayName();

    public abstract UiManageable<? extends LaserColor> getUI();

    @Override
    public abstract LaserColor clone();
}
