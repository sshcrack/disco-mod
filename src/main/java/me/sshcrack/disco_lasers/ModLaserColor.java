package me.sshcrack.disco_lasers;

import me.sshcrack.disco_lasers.util.GeneralRegistry;
import me.sshcrack.disco_lasers.util.GeneralRegistryEntry;
import me.sshcrack.disco_lasers.util.color.LaserColor;
import me.sshcrack.disco_lasers.util.color.RainbowColor;
import me.sshcrack.disco_lasers.util.color.StaticColor;

public class ModLaserColor {
    public static final GeneralRegistry<LaserColor> REGISTRY = new GeneralRegistry<>();

    public static void initialize() {
        REGISTRY.register(DiscoLasersVoice.ref("static_color"), new GeneralRegistryEntry<>(StaticColor.class, StaticColor.CODEC));
        REGISTRY.register(DiscoLasersVoice.ref("rainbow_color"), new GeneralRegistryEntry<>(RainbowColor.class, RainbowColor.CODEC));
    }
}
