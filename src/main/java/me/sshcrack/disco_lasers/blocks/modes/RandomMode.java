package me.sshcrack.disco_lasers.blocks.modes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.sshcrack.disco_lasers.util.color.LaserColor;
import net.minecraft.text.Text;
import net.minecraft.util.dynamic.Codecs;

import java.util.List;

public class RandomMode extends LaserMode {
    public static MapCodec<RandomMode> CODEC = RecordCodecBuilder.mapCodec(e ->
            e.group(
                    Codec.FLOAT.fieldOf("max_angle")
                            .forGetter(RandomMode::getMaxAngle),
                    Codec.FLOAT.fieldOf("laser_duration")
                            .forGetter(RandomMode::getLaserAge),
                    Codec.FLOAT.fieldOf("laser_speed")
                            .forGetter(RandomMode::getLaserSpeed),
                    Codecs.POSITIVE_INT.fieldOf("laser_amount")
                            .forGetter(RandomMode::getLaserAmount),
                    LaserColor.CODEC
                            .listOf()
                            .fieldOf("color")
                            .forGetter(RandomMode::getAvailableColors)
            ).apply(e, RandomMode::new)
    );

    private float maxAngle;
    private float laserAge;
    private float laserSpeed;
    private int laserAmount;
    private List<LaserColor> availableColors;

    public RandomMode(float maxAngle, float laserAge, float laserSpeed, int laserAmount, List<LaserColor> availableColors) {
        this.maxAngle = maxAngle;
        this.laserAmount = laserAmount;
        this.laserSpeed = laserSpeed;
        this.laserAge = laserAge;
        this.availableColors = availableColors;
    }

    public void setMaxAngle(float maxAngle) {
        this.maxAngle = maxAngle;
    }

    public void setLaserAge(float laserAge) {
        this.laserAge = laserAge;
    }

    public void setAvailableColors(List<LaserColor> availableColors) {
        this.availableColors = availableColors;
    }

    public void setLaserAmount(int laserAmount) {
        this.laserAmount = laserAmount;
    }

    public void setLaserSpeed(float laserSpeed) {
        this.laserSpeed = laserSpeed;
    }

    public float getMaxAngle() {
        return maxAngle;
    }

    public float getLaserAge() {
        return laserAge;
    }

    public List<LaserColor> getAvailableColors() {
        return availableColors;
    }

    public int getLaserAmount() {
        return laserAmount;
    }

    public float getLaserSpeed() {
        return laserSpeed;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.laser_block.modes.random");
    }
}
