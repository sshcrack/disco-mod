package me.sshcrack.disco_lasers.renderer.modes;

import me.sshcrack.disco_lasers.blocks.LaserBlockEntity;
import me.sshcrack.disco_lasers.blocks.modes.OffMode;
import me.sshcrack.disco_lasers.renderer.modes.registry.LaserModeRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

public class OffModeRenderer extends LaserModeRenderer<OffMode> {
    @Override
    public void render(OffMode mode, LaserBlockEntity blockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
        // Do nothing
    }
}
