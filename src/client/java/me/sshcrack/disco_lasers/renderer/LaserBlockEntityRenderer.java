package me.sshcrack.disco_lasers.renderer;

import me.sshcrack.disco_lasers.blocks.LaserBlock;
import me.sshcrack.disco_lasers.blocks.LaserBlockEntity;
import me.sshcrack.disco_lasers.blocks.modes.LaserMode;
import me.sshcrack.disco_lasers.renderer.modes.registry.LaserModeRenderer;
import me.sshcrack.disco_lasers.renderer.modes.registry.LaserModeRendererRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.RotationPropertyHelper;

public class LaserBlockEntityRenderer implements BlockEntityRenderer<LaserBlockEntity> {
    private LaserModeRenderer<?> renderer;

    public LaserBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }


    public void render(LaserBlockEntity blockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
        var mode = blockEntity.getLaserMode();
        if (renderer == null || !renderer.underlyingModeClass.isInstance(mode))
            renderer = LaserModeRendererRegistry.createRenderer(mode);

        matrixStack.push();
        BlockState blockState = blockEntity.getCachedState();

        int rotationProp = blockState.get(LaserBlock.ROTATION);
        float rotDeg = RotationPropertyHelper.toDegrees(rotationProp);

        matrixStack.translate(0.5, 0.5, 0.5);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotDeg));

        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));


        if (!blockEntity.getLaserMode().getColors().isEmpty()) {
            //noinspection unchecked
            ((LaserModeRenderer<LaserMode>) renderer).render(mode, blockEntity, tickDelta, matrixStack, vertexConsumerProvider, light, overlay);
        }


        matrixStack.pop();
    }

    @Override
    public int getRenderDistance() {
        return 256;
    }
}
