package me.sshcrack.disco_lasers;

import me.sshcrack.disco_lasers.blocks.modes.LaserMode;
import me.sshcrack.disco_lasers.blocks.modes.RandomMode;
import me.sshcrack.disco_lasers.blocks.modes.SpreadMode;
import me.sshcrack.disco_lasers.util.GeneralRegistry;
import me.sshcrack.disco_lasers.util.GeneralRegistryEntry;

public class ModLaserModes {
    public static final GeneralRegistry<LaserMode> REGISTRY = new GeneralRegistry<>();

    public static void initialize() {
        REGISTRY.register(DiscoLasersVoice.ref("spread_mode"), new GeneralRegistryEntry<>(SpreadMode.class, SpreadMode.CODEC));
        REGISTRY.register(DiscoLasersVoice.ref("random_mode"), new GeneralRegistryEntry<>(RandomMode.class, RandomMode.CODEC));
    }
}
