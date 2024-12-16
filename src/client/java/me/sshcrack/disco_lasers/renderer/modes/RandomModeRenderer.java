package me.sshcrack.disco_lasers.renderer.modes;

import me.sshcrack.disco_lasers.blocks.LaserBlockEntity;
import me.sshcrack.disco_lasers.blocks.modes.RandomMode;
import me.sshcrack.disco_lasers.renderer.modes.registry.LaserModeRenderer;
import me.sshcrack.disco_lasers.util.color.LaserColor;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import org.joml.Quaternionf;

public class RandomModeRenderer extends LaserModeRenderer<RandomMode> {
    private final Random random = Random.create();
    private LaserInfo[] lasers = new LaserInfo[]{};


    public record LaserInfo(LaserColor color, float rotX, float rotY, float rotZ, long creationTime,
                            float deltaOffset) {
    }

    private LaserInfo generateRandom(RandomMode mode, long worldTime, float maxAngle) {
        var colors = mode.getColors();

        var color = colors.get(random.nextInt(colors.size()));
        return new LaserInfo(
                color,
                random.nextFloat() * maxAngle - maxAngle / 2f,
                random.nextFloat() * maxAngle - maxAngle / 2f,
                random.nextFloat() * maxAngle - maxAngle / 2f,
                worldTime, random.nextFloat()
        );
    }

    @Override
    public void render(RandomMode mode, LaserBlockEntity blockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
        long worldTime = blockEntity.getWorld().getTime();
        int amount = mode.getLaserAmount();
        var maxY = blockEntity.getDistance();

        if (lasers.length != amount)
            lasers = new LaserInfo[amount];

        var maxAngle = mode.getMaxAngle();
        var maxAge = mode.getLaserAge();
        for (int i = 0; i < amount; i++) {
            if (lasers[i] == null)
                lasers[i] = generateRandom(mode, worldTime, maxAngle);

            var laser = lasers[i];
            var time = (worldTime + tickDelta) - laser.creationTime;
            if (time > maxAge) {
                lasers[i] = generateRandom(mode, worldTime, maxAngle);
                laser = lasers[i];
            }

            laser.color.tick(worldTime, tickDelta);
            matrixStack.push();

            var deltaAge = 0;//time / maxAge;
            var deltaX = laser.rotX * deltaAge + laser.rotX / 2;
            var deltaY = laser.rotY * deltaAge * laser.deltaOffset + laser.rotY / 2;
            var deltaZ = laser.rotZ * deltaAge * -laser.deltaOffset + laser.rotZ / 2;
            matrixStack.multiply(new Quaternionf().rotateXYZ(deltaX, deltaY, deltaZ));
            renderDefaultBeam(matrixStack, vertexConsumerProvider, tickDelta, worldTime, 0, maxY, laser.color.getARGB());
            matrixStack.pop();
        }
    }
}
