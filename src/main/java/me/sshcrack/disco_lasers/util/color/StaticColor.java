package me.sshcrack.disco_lasers.util.color;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

public class StaticColor implements LaserColor {
    public static final MapCodec<StaticColor> CODEC = Codec.INT.xmap(StaticColor::new, StaticColor::getARGB).fieldOf("color");
    private final int color;

    public StaticColor(int color) {
        this.color = color;
    }

    @Override
    public int getARGB() {
        return color;
    }

    @Override
    public void tick(float worldTick, float delta) {}
}
