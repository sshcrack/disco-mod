package me.sshcrack.disco_lasers;

import me.sshcrack.disco_lasers.blocks.modes.LaserMode;
import me.sshcrack.disco_lasers.blocks.modes.LaserModeRegistryEntry;
import me.sshcrack.disco_lasers.blocks.modes.SpreadMode;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class ModLaserModes {
    public static HashMap<Identifier, LaserModeRegistryEntry<?>> MODES = new HashMap<>();

    public static void registerMode(Identifier identifier, LaserModeRegistryEntry<?> registryEntry) {
        MODES.put(identifier, registryEntry);
    }

    @Nullable
    public static <T extends LaserMode> Identifier getId(T mode) {
        for (var entry : MODES.entrySet()) {
            if (!entry.getValue().modeClass().isInstance(mode))
                continue;

            return entry.getKey();
        }

        return null;
    }

    public static void initialize() {
        registerMode(DiscoLasersVoice.ref("spread_mode"), new LaserModeRegistryEntry<>(SpreadMode.class, SpreadMode.CODEC));
    }
}
