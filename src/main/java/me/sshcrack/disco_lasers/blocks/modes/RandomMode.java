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

public class RandomMode extends LaserMode {
    public static MutableObject<UiManageable.UiManageableFactory<RandomMode>> UI_FACTORY = new MutableObject<>();

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
    private UiManageable<RandomMode> ui;

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
        return Text.translatable("block.disco_lasers.laser_block.modes.random");
    }


    @Override
    public LaserMode clone() {
        return new RandomMode(maxAngle, laserAge, laserSpeed, laserAmount, new ArrayList<>(colors));
    }

    @Override
    public UiManageable<? extends LaserMode> getUI() {
        if (ui == null)
            ui = UI_FACTORY.getValue().create(this);

        return ui;
    }
}
