package me.sshcrack.disco_lasers;

import me.sshcrack.disco_lasers.registries.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscoLasers implements ModInitializer {
    public static final String MOD_ID = "disco_lasers";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModDataComponentTypes.initialize();
        ModLaserColor.initialize();
        ModLaserModes.initialize();
        ModHandledScreens.initialize();

        ModBlockEntityTypes.initialize();
        ModBlocks.initialize();
        ModItems.initialize();
    }

    public static Identifier ref(String path) {
        return Identifier.of(MOD_ID, path);
    }
}