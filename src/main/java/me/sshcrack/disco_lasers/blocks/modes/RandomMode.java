package me.sshcrack.disco_lasers.blocks.modes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.wispforest.owo.ui.component.DiscreteSliderComponent;
import io.wispforest.owo.ui.core.Component;
import me.sshcrack.disco_lasers.util.color.LaserColor;
import net.minecraft.text.Text;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
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
                            .forGetter(LaserMode::getColors)
            ).apply(e, RandomMode::new)
    );

    private float maxAngle;
    private float laserAge;
    private float laserSpeed;
    private int laserAmount;

    public RandomMode() {
        this(45 * MathHelper.RADIANS_PER_DEGREE, 3, 1, 5, List.of());
    }


    public RandomMode(float maxAngle, float laserAge, float laserSpeed, int laserAmount, List<LaserColor> colors) {
        super(colors);

        this.maxAngle = maxAngle;
        this.laserAmount = laserAmount;
        this.laserSpeed = laserSpeed;
        this.laserAge = laserAge;
    }

    public void setMaxAngle(float maxAngle) {
        this.maxAngle = maxAngle;
    }

    public void setLaserAge(float laserAge) {
        this.laserAge = laserAge;
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


    @Override
    public void initializeUI(Component parent) {
        var maxAngle = (DiscreteSliderComponent) parent.id("mode.random.max-angle");
        var laserAge = (DiscreteSliderComponent) parent.id("mode.random.laser-age");
        var laserSpeed = (DiscreteSliderComponent) parent.id("mode.random.laser-speed");
        var laserAmount = (DiscreteSliderComponent) parent.id("mode.random.laser-amount");

        maxAngle.value(this.maxAngle);
        laserAge.value(this.laserAge);
        laserSpeed.value(this.laserSpeed);
        laserAmount.value(this.laserAmount);

        maxAngle.onChanged().subscribe(e -> setMaxAngle((float) e));
        laserAge.onChanged().subscribe(e -> setLaserAge((float) e));
        laserSpeed.onChanged().subscribe(e -> setLaserSpeed((float) e));
        laserAmount.onChanged().subscribe(e -> setLaserAmount((int) e));
    }

    @Override
    public String getTemplateName() {
        return "mode.random";
    }

    @Override
    public LaserMode clone() {
        return new RandomMode(maxAngle, laserAge, laserSpeed, laserAmount, new ArrayList<>(colors));
    }
}
