package me.sshcrack.disco_lasers.blocks.modes;

import com.anthonyhilyard.prism.util.IColor;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.sshcrack.disco_lasers.ModDataComponents;
import net.minecraft.text.Text;
import net.minecraft.util.dynamic.Codecs;

import java.util.List;

public class SpreadMode extends LaserMode {
    public static MapCodec<SpreadMode> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ModDataComponents.DYNAMIC_COLOR_CODEC.listOf().fieldOf("lasers")
                            .forGetter(SpreadMode::getLasers),
                    Codec.FLOAT.fieldOf("spread_angle")
                            .forGetter(SpreadMode::getSpreadAngle),
                    Codec.FLOAT.fieldOf("tiltAngle")
                            .forGetter(SpreadMode::getTiltAngle),
                    Codecs.POSITIVE_INT.fieldOf("laserMultiplier")
                            .forGetter(SpreadMode::getLaserMultiplier)
            ).apply(instance, SpreadMode::new)
    );


    private final List<IColor> lasers;
    private float spreadAngle;
    private float tiltAngle;
    private int laserMultiplier;

    public SpreadMode(List<IColor> lasers, float spreadAngle, float tiltAngle, int laserMultiplier) {
        this.lasers = lasers;
        this.spreadAngle = spreadAngle;
        this.tiltAngle = tiltAngle;
        this.laserMultiplier = laserMultiplier;
    }


    public List<IColor> getLasers() {
        return lasers;
    }

    public float getSpreadAngle() {
        return spreadAngle;
    }

    public float getTiltAngle() {
        return tiltAngle;
    }

    public int getLaserMultiplier() {
        return laserMultiplier;
    }

    public void setLaserMultiplier(int laserMultiplier) {
        this.laserMultiplier = laserMultiplier;
    }

    public void setTiltAngle(float tiltAngle) {
        this.tiltAngle = tiltAngle;
    }

    public void setSpreadAngle(float spreadAngle) {
        this.spreadAngle = spreadAngle;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.laser_block.modes.spread");
    }
}
