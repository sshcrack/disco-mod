package me.sshcrack.disco_lasers.renderer.modes;

import me.sshcrack.disco_lasers.blocks.LaserBlockEntity;
import me.sshcrack.disco_lasers.blocks.modes.SpreadMode;
import me.sshcrack.disco_lasers.renderer.modes.registry.LaserModeRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class SpreadModeRenderer extends LaserModeRenderer<SpreadMode> {
    @Override
    public void render(SpreadMode mode, LaserBlockEntity blockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
        long worldTime = blockEntity.getWorld().getTime();
        var maxY = blockEntity.getDistance();
        int k = 0;


        float deltaRotation = (MathHelper.floorMod(worldTime * mode.getTiltSpeed(), 40) + tickDelta * mode.getTiltSpeed()) / (40.0f / 2) - 1.0f;
        float radDelta = MathHelper.sin(deltaRotation * MathHelper.PI);

        matrixStack.push();
        matrixStack.multiply(RotationAxis.NEGATIVE_Z.rotation(-(mode.getSpreadAngle() / 2f)));
        matrixStack.push();

        var lasers = mode.getColors();
        var amount = mode.getLaserMultiplier();
        var total = lasers.size() * amount;
        for (int i = 0; i < total; i++) {
            var spacing = RotationAxis.NEGATIVE_Z.rotation((float) i / ((float) (total - 1)) * mode.getSpreadAngle());

            matrixStack.push();
            var laserColor = lasers.get(i / amount);
            laserColor.tick(worldTime, tickDelta);

            matrixStack.multiply(spacing);
            matrixStack.multiply(RotationAxis.NEGATIVE_X.rotation(radDelta * mode.getTiltAngle()));
            renderDefaultBeam(matrixStack, vertexConsumerProvider, tickDelta, worldTime, k, maxY, laserColor.getARGB());
            matrixStack.pop();
        }

        matrixStack.pop();
        matrixStack.pop();
    }
}
