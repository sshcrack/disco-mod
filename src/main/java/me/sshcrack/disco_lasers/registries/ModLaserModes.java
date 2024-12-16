package me.sshcrack.disco_lasers.registries;

import me.sshcrack.disco_lasers.DiscoLasers;
import me.sshcrack.disco_lasers.blocks.modes.LaserMode;
import me.sshcrack.disco_lasers.blocks.modes.OffMode;
import me.sshcrack.disco_lasers.blocks.modes.RandomMode;
import me.sshcrack.disco_lasers.blocks.modes.SpreadMode;
import me.sshcrack.disco_lasers.util.GeneralRegistry;
import me.sshcrack.disco_lasers.util.GeneralRegistryEntry;

public class ModLaserModes {
    public static final GeneralRegistry<LaserMode> REGISTRY = new GeneralRegistry<>();

    public static void initialize() {
        REGISTRY.register(DiscoLasers.ref("spread_mode"), new GeneralRegistryEntry<>(SpreadMode.class, SpreadMode.CODEC), SpreadMode::new);
        REGISTRY.register(DiscoLasers.ref("random_mode"), new GeneralRegistryEntry<>(RandomMode.class, RandomMode.CODEC), RandomMode::new);
        REGISTRY.register(DiscoLasers.ref("off_mode"), new GeneralRegistryEntry<>(OffMode.class, OffMode.CODEC), OffMode::new);
    }
}
