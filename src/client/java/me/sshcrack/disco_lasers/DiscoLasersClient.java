package me.sshcrack.disco_lasers;

import me.sshcrack.disco_lasers.blocks.modes.RandomMode;
import me.sshcrack.disco_lasers.blocks.modes.SpreadMode;
import me.sshcrack.disco_lasers.registries.ModBlockEntityTypes;
import me.sshcrack.disco_lasers.registries.ModHandledScreens;
import me.sshcrack.disco_lasers.renderer.LaserBlockEntityRenderer;
import me.sshcrack.disco_lasers.renderer.modes.RandomModeRenderer;
import me.sshcrack.disco_lasers.renderer.modes.SpreadModeRenderer;
import me.sshcrack.disco_lasers.renderer.modes.registry.LaserModeRendererRegistry;
import me.sshcrack.disco_lasers.ui.SingleLaserScreen;
import me.sshcrack.disco_lasers.ui.initializers.ManageableRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class DiscoLasersClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(ModBlockEntityTypes.LASER_BLOCK_ENTITY_TYPE, LaserBlockEntityRenderer::new);

        LaserModeRendererRegistry.register(SpreadMode.class, SpreadModeRenderer::new);
        LaserModeRendererRegistry.register(RandomMode.class, RandomModeRenderer::new);

        ManageableRegistry.initialize();

        HandledScreens.register(ModHandledScreens.SINGLE_LASER_HANDLE_TYPE, SingleLaserScreen::new);
    }
}