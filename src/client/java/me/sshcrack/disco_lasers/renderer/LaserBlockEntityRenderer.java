package me.sshcrack.disco_lasers.renderer;

import me.sshcrack.disco_lasers.blocks.LaserBlock;
import me.sshcrack.disco_lasers.blocks.LaserBlockEntity;
import me.sshcrack.disco_lasers.blocks.modes.LaserMode;
import me.sshcrack.disco_lasers.renderer.modes.registry.LaserModeRenderer;
import me.sshcrack.disco_lasers.renderer.modes.registry.LaserModeRendererRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.RotationPropertyHelper;

public class LaserBlockEntityRenderer implements BlockEntityRenderer<LaserBlockEntity> {
    private final BlockEntityRendererFactory.Context ctx;

    public LaserBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.ctx = ctx;
    }


    public void render(LaserBlockEntity blockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
        BlockState blockState = blockEntity.getCachedState();

        int rotationProp = blockState.get(LaserBlock.ROTATION);
        float rotDeg = RotationPropertyHelper.toDegrees(rotationProp);

        matrixStack.push();

        matrixStack.translate(0.5, 0, 0.5);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180 + rotDeg));
        matrixStack.translate(-0.5, 0, -0.5);

        var layer = RenderLayers.getBlockLayer(blockState);
        ctx.getRenderManager().renderBlock(
                blockState,
                blockEntity.getPos(),
                blockEntity.getWorld(),
                matrixStack,
                vertexConsumerProvider.getBuffer(layer),
                false,
                blockEntity.getWorld().random);

        matrixStack.translate(0.5, 0.25, 1.25f / 16f);
        matrixStack.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(90));

        var mode = blockEntity.getCurrentLaserMode();
        var renderer = blockEntity.renderer;
        if (mode != null) {
            if (renderer == null || (renderer instanceof LaserModeRenderer<?> r && !r.underlyingModeClass.isInstance(mode)))
                blockEntity.renderer = LaserModeRendererRegistry.createRenderer(mode);

            renderer = blockEntity.renderer;
            if (!mode.getColors().isEmpty() && renderer instanceof LaserModeRenderer<?> r) {
                //noinspection unchecked
                ((LaserModeRenderer<LaserMode>) r).render(mode, blockEntity, tickDelta, matrixStack, vertexConsumerProvider, light, overlay);
            }
        }


        matrixStack.pop();
    }

    @Override
    public int getRenderDistance() {
        return 256;
    }
}
