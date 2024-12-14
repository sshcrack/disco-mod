package me.sshcrack.disco_lasers.registries;

import me.sshcrack.disco_lasers.DiscoLasers;
import me.sshcrack.disco_lasers.screen.SingleLaserScreenHandler;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;

public class ModHandledScreens {
    public static final ScreenHandlerType<SingleLaserScreenHandler> SINGLE_LASER_HANDLE_TYPE = Registry.register(
            Registries.SCREEN_HANDLER,
            DiscoLasers.ref("single_laser_handler"),
            new ScreenHandlerType<>(SingleLaserScreenHandler::new, FeatureFlags.VANILLA_FEATURES)
    );

    public static void initialize() {

    }
}
