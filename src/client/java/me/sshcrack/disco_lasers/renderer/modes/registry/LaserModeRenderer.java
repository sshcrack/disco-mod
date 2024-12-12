package me.sshcrack.disco_lasers.renderer.modes.registry;

import me.sshcrack.disco_lasers.blocks.LaserBlockEntity;
import me.sshcrack.disco_lasers.blocks.modes.LaserMode;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

import static me.sshcrack.disco_lasers.DiscoLasersVoice.ref;

public abstract class LaserModeRenderer<T extends LaserMode> {
    public static final Identifier LASER_TEXTURE = ref("textures/entity/laser.png");
    public Class<T> underlyingModeClass;

    public LaserModeRenderer() {
    }

    public abstract void render(T mode, LaserBlockEntity blockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay);

    protected static void renderDefaultBeam(MatrixStack matrices, VertexConsumerProvider vertexConsumers, float tickDelta, long worldTime, int yOffset, int maxY, int color) {
        renderBeam(matrices, vertexConsumers, LASER_TEXTURE, tickDelta, 1.0F, worldTime, yOffset, maxY, color, 0.03F, 0.25F);
    }

    protected static void renderBeam(
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            Identifier textureId,
            float tickDelta,
            float heightScale,
            long worldTime,
            int yOffset,
            int maxY,
            int color,
            float innerRadius,
            float outerRadius
    ) {
        int height = yOffset + maxY;

        float rotationY = 0;//(float) Math.floorMod(worldTime, 40) * 0+ tickDelta;

        float g = maxY < 0 ? rotationY : -rotationY;
        float h = MathHelper.fractionalPart(g * 0.2F - (float) MathHelper.floor(g * 0.1F));
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotationY * 2.25F - 45.0F));
        float x3 = -innerRadius;
        float z4 = -innerRadius;
        float v2 = -1.0F + h;
        float v1 = (float) maxY * heightScale * (0.5F / innerRadius) + v2;
        renderBeamLayer(
                matrices,
                vertexConsumers.getBuffer(RenderLayer.getBeaconBeam(textureId, false)),
                color,
                yOffset,
                height,
                0.0F,
                innerRadius,
                innerRadius,
                0.0F,
                x3,
                0.0F,
                0.0F,
                z4,
                0.0F,
                1.0F,
                v1,
                v2
        );
        matrices.pop();
    }

    protected static void renderBeamLayer(
            MatrixStack matrices,
            VertexConsumer vertices,
            int color,
            int yOffset,
            int height,
            float x1,
            float z1,
            float x2,
            float z2,
            float x3,
            float z3,
            float x4,
            float z4,
            float u1,
            float u2,
            float v1,
            float v2
    ) {
        MatrixStack.Entry entry = matrices.peek();
        renderBeamFace(entry, vertices, color, yOffset, height, x1, z1, x2, z2, u1, u2, v1, v2);
        renderBeamFace(entry, vertices, color, yOffset, height, x4, z4, x3, z3, u1, u2, v1, v2);
        renderBeamFace(entry, vertices, color, yOffset, height, x2, z2, x4, z4, u1, u2, v1, v2);
        renderBeamFace(entry, vertices, color, yOffset, height, x3, z3, x1, z1, u1, u2, v1, v2);
    }

    protected static void renderBeamFace(
            MatrixStack.Entry matrix,
            VertexConsumer vertices,
            int color,
            int yOffset,
            int height,
            float x1,
            float z1,
            float x2,
            float z2,
            float u1,
            float u2,
            float v1,
            float v2
    ) {
        renderBeamVertex(matrix, vertices, color, height, x1, z1, u2, v1);
        renderBeamVertex(matrix, vertices, color, yOffset, x1, z1, u2, v2);
        renderBeamVertex(matrix, vertices, color, yOffset, x2, z2, u1, v2);
        renderBeamVertex(matrix, vertices, color, height, x2, z2, u1, v1);
    }

    protected static void renderBeamVertex(MatrixStack.Entry matrix, VertexConsumer vertices, int color, int y, float x, float z, float u, float v) {
        vertices.vertex(matrix, x, (float) y, z)
                .color(color)
                .texture(u, v)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
                .normal(matrix, 0.0F, 1.0F, 0.0F);
    }
}