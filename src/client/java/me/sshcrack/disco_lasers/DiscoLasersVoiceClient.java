package me.sshcrack.disco_lasers;

import me.sshcrack.disco_lasers.blocks.modes.SpreadMode;
import me.sshcrack.disco_lasers.renderer.LaserBlockEntityRenderer;
import me.sshcrack.disco_lasers.renderer.modes.SpreadModeRenderer;
import me.sshcrack.disco_lasers.renderer.modes.registry.LaserModeRendererRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class DiscoLasersVoiceClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(ModBlockEntityTypes.LASER_BLOCK_ENTITY_TYPE, LaserBlockEntityRenderer::new);

        LaserModeRendererRegistry.register(SpreadMode.class, SpreadModeRenderer::new);
    }
}