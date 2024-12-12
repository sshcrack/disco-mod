package me.sshcrack.disco_lasers.renderer;

import me.sshcrack.disco_lasers.blocks.LaserBlockEntity;
import me.sshcrack.disco_lasers.blocks.modes.LaserMode;
import me.sshcrack.disco_lasers.renderer.modes.registry.LaserModeRendererRegistry;
import me.sshcrack.disco_lasers.renderer.modes.registry.LaserModeRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

public class LaserBlockEntityRenderer implements BlockEntityRenderer<LaserBlockEntity> {
    private LaserModeRenderer<?> renderer;

    public LaserBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }


    public void render(LaserBlockEntity blockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
        var mode = blockEntity.getLaserMode();
        if (renderer == null || !renderer.underlyingModeClass.isInstance(mode))
            renderer = LaserModeRendererRegistry.createRenderer(mode);

        //noinspection unchecked
        ((LaserModeRenderer<LaserMode>) renderer).render(mode, blockEntity, tickDelta, matrixStack, vertexConsumerProvider, light, overlay);
    }

    @Override
    public int getRenderDistance() {
        return 256;
    }
}
