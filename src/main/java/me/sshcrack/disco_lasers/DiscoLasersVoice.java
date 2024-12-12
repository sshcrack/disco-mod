package me.sshcrack.disco_lasers;

import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscoLasersVoice implements ModInitializer {
    public static final String MOD_ID = "disco_lasers";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModLaserModes.initialize();
        ModDataComponents.initialize();
        ModBlockEntityTypes.initialize();
        ModBlocks.initialize();
    }

    public static Identifier ref(String path) {
        return Identifier.of(MOD_ID, path);
    }
}