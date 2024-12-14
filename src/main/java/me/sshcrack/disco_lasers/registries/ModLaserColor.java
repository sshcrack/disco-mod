package me.sshcrack.disco_lasers.registries;

import me.sshcrack.disco_lasers.DiscoLasers;
import me.sshcrack.disco_lasers.util.GeneralRegistry;
import me.sshcrack.disco_lasers.util.GeneralRegistryEntry;
import me.sshcrack.disco_lasers.util.color.LaserColor;
import me.sshcrack.disco_lasers.util.color.RainbowColor;
import me.sshcrack.disco_lasers.util.color.StaticColor;

public class ModLaserColor {
    public static final GeneralRegistry<LaserColor> REGISTRY = new GeneralRegistry<>();

    public static void initialize() {
        REGISTRY.register(DiscoLasers.ref("static_color"), new GeneralRegistryEntry<>(StaticColor.class, StaticColor.CODEC), () -> new StaticColor(0xFF0000));
        REGISTRY.register(DiscoLasers.ref("rainbow_color"), new GeneralRegistryEntry<>(RainbowColor.class, RainbowColor.CODEC), RainbowColor::new);
    }
}
