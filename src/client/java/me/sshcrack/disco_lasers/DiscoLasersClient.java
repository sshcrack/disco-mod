package me.sshcrack.disco_lasers;

import me.sshcrack.disco_lasers.blocks.LaserBlockEntity;
import me.sshcrack.disco_lasers.blocks.modes.OffMode;
import me.sshcrack.disco_lasers.blocks.modes.RandomMode;
import me.sshcrack.disco_lasers.blocks.modes.SpreadMode;
import me.sshcrack.disco_lasers.registries.ModBlockEntityTypes;
import me.sshcrack.disco_lasers.registries.ModHandledScreens;
import me.sshcrack.disco_lasers.registries.ModItems;
import me.sshcrack.disco_lasers.registries.ModNetworking;
import me.sshcrack.disco_lasers.renderer.LaserBlockEntityRenderer;
import me.sshcrack.disco_lasers.renderer.modes.OffModeRenderer;
import me.sshcrack.disco_lasers.renderer.modes.RandomModeRenderer;
import me.sshcrack.disco_lasers.renderer.modes.SpreadModeRenderer;
import me.sshcrack.disco_lasers.renderer.modes.registry.LaserModeRendererRegistry;
import me.sshcrack.disco_lasers.ui.SingleLaserScreen;
import me.sshcrack.disco_lasers.ui.initializers.ManageableRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.util.ActionResult;

public class DiscoLasersClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(ModBlockEntityTypes.LASER_BLOCK_ENTITY_TYPE, LaserBlockEntityRenderer::new);

        LaserModeRendererRegistry.register(SpreadMode.class, SpreadModeRenderer::new);
        LaserModeRendererRegistry.register(RandomMode.class, RandomModeRenderer::new);
        LaserModeRendererRegistry.register(OffMode.class, OffModeRenderer::new);

        ManageableRegistry.initialize();

        HandledScreens.register(ModHandledScreens.SINGLE_LASER_HANDLE_TYPE, SingleLaserScreen::new);

        ModNetworking.LASER_CONTROL_CHANNEL.registerClientbound(ModNetworking.LaserControlPacket.class, (message, access) -> {
            var entity = access.netHandler().getWorld().getBlockEntity(message.pos());
            if (entity instanceof LaserBlockEntity laser) {
                laser.setCurrentIndex(message.index());
            }
        });
        ModNetworking.LASER_CONTROL_CHANNEL.registerClientbound(ModNetworking.LaserDistanceSet.class, (message, access) -> {
            var entity = access.netHandler().getWorld().getBlockEntity(message.pos());
            if (entity instanceof LaserBlockEntity laser) {
                laser.setDistance(message.distance());
            }
        });

        DoAttackCallback.EVENT.register(((player, world, stack) -> {
            if (!stack.isOf(ModItems.LASER_CONTROLLER_ITEM))
                return ActionResult.PASS;

            ModNetworking.LASER_CONTROL_CHANNEL.clientHandle().send(new ModNetworking.AllLasersOff());
            return ActionResult.CONSUME;
        }));
    }
}