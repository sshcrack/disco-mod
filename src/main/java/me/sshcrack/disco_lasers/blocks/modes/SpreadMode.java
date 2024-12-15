package me.sshcrack.disco_lasers.blocks.modes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.sshcrack.disco_lasers.screen.UiManageable;
import me.sshcrack.disco_lasers.util.color.LaserColor;
import net.minecraft.text.Text;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.ArrayList;
import java.util.List;

public class SpreadMode extends LaserMode {
    public static MutableObject<UiManageable.UiManageableFactory<SpreadMode>> UI_FACTORY = new MutableObject<>();

    public static MapCodec<SpreadMode> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    LaserColor.CODEC.listOf().fieldOf("colors")
                            .forGetter(SpreadMode::getColors),
                    Codec.FLOAT.fieldOf("spread_angle")
                            .forGetter(SpreadMode::getSpreadAngle),
                    Codec.FLOAT.fieldOf("tiltAngle")
                            .forGetter(SpreadMode::getTiltAngle),
                    Codec.FLOAT.fieldOf("tiltSpeed")
                            .forGetter(SpreadMode::getTiltSpeed),
                    Codecs.POSITIVE_INT.fieldOf("laserMultiplier")
                            .forGetter(SpreadMode::getLaserMultiplier)
            ).apply(instance, SpreadMode::new)
    );


    private float spreadAngle;
    private float tiltAngle;
    private float tiltSpeed;

    private int laserMultiplier;
    private UiManageable<SpreadMode> ui;

    public SpreadMode() {
        this(List.of(), 45 * MathHelper.RADIANS_PER_DEGREE, 10 * MathHelper.RADIANS_PER_DEGREE, 1f, 1);
    }

    public SpreadMode(List<LaserColor> colors, float spreadAngle, float tiltAngle, float tiltSpeed, int laserMultiplier) {
        super(colors);
        this.spreadAngle = spreadAngle;
        this.tiltAngle = tiltAngle;
        this.laserMultiplier = laserMultiplier;
        this.tiltSpeed = tiltSpeed;
    }

    public float getTiltSpeed() {
        return tiltSpeed;
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

    public void setTiltSpeed(float tiltSpeed) {
        this.tiltSpeed = tiltSpeed;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.disco_lasers.laser_block.modes.spread");
    }

    @Override
    public UiManageable<? extends LaserMode> getUI() {
        if (ui == null)
            ui = UI_FACTORY.getValue().create(this);

        return ui;
    }
}
