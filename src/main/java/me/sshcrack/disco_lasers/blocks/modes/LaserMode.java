package me.sshcrack.disco_lasers.blocks.modes;

import com.mojang.serialization.Codec;
import me.sshcrack.disco_lasers.registries.ModLaserModes;
import me.sshcrack.disco_lasers.screen.UiManageable;
import me.sshcrack.disco_lasers.util.color.LaserColor;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public abstract class LaserMode implements Cloneable {
    public static Codec<LaserMode> LASER_CODEC = ModLaserModes.REGISTRY.getSerializableCodec();

    public abstract Text getDisplayName();

    protected List<LaserColor> colors;

    public LaserMode(List<LaserColor> colors) {
        this.colors = new ArrayList<>(colors);
    }

    public List<LaserColor> getColors() {
        return colors;
    }

    public void setColors(List<LaserColor> colors) {
        this.colors = colors;
    }

    public void addColor(LaserColor color) {
        this.colors.add(color);
    }

    @Override
    public abstract LaserMode clone();

    public abstract UiManageable<? extends LaserMode> getUI();
}
