package me.sshcrack.disco_lasers.renderer.modes;

import me.sshcrack.disco_lasers.blocks.LaserBlockEntity;
import me.sshcrack.disco_lasers.blocks.modes.SpreadMode;
import me.sshcrack.disco_lasers.renderer.modes.registry.LaserModeRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;

public class SpreadModeRenderer extends LaserModeRenderer<SpreadMode> {
    @Override
    public void render(SpreadMode mode, LaserBlockEntity blockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
        long worldTime = blockEntity.getWorld().getTime();
        int k = 0;


        float deltaRotation = (MathHelper.floorMod(worldTime, 40) + tickDelta) / (40.0f / 2) - 1.0f;
        float radDelta = MathHelper.sin(deltaRotation * MathHelper.PI);
        var axisAngle = new AxisAngle4f(radDelta * mode.getTiltAngle(), 1, -1f, 0f);

        var rotation = new Quaternionf().rotationAxis(axisAngle);

        matrixStack.push();
        matrixStack.multiply(RotationAxis.NEGATIVE_Z.rotation(-(mode.getSpreadAngle() / 2f)));
        matrixStack.multiply(rotation);
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
            renderDefaultBeam(matrixStack, vertexConsumerProvider, tickDelta, worldTime, k, 10, laserColor.getARGB());
            matrixStack.pop();
        }

        matrixStack.pop();
        matrixStack.pop();
    }
}
