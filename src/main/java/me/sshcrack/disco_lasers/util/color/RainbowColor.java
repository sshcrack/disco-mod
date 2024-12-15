package me.sshcrack.disco_lasers.util.color;

import com.anthonyhilyard.prism.util.ColorUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.sshcrack.disco_lasers.screen.UiManageable;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.mutable.MutableObject;

public class RainbowColor implements LaserColor {
    public static MutableObject<UiManageable.UiManageableFactory<RainbowColor>> UI_FACTORY = new MutableObject<>();

    public static final MapCodec<RainbowColor> CODEC = RecordCodecBuilder.mapCodec(e ->
            e.group(
                    Codec.FLOAT.fieldOf("offset")
                            .forGetter(RainbowColor::getOffset),
                    Codec.FLOAT.fieldOf("animation_speed")
                            .forGetter(RainbowColor::getAnimationSpeed)
            ).apply(e, RainbowColor::new)
    );
    private float offset;
    private float animationSpeed;
    private UiManageable<RainbowColor> ui;

    public RainbowColor() {
        this(0, 1f);
    }

    public RainbowColor(float offset, float animationSpeed) {
        this.offset = offset;
        this.animationSpeed = animationSpeed;
    }

    private int currColor;

    @Override
    public int getARGB() {
        return currColor;
    }

    public float getOffset() {
        return offset;
    }

    public float getAnimationSpeed() {
        return animationSpeed;
    }

    public void setAnimationSpeed(float animationSpeed) {
        this.animationSpeed = animationSpeed;
    }

    public void setOffset(float offset) {
        this.offset = offset;
    }

    @Override
    public void tick(float worldTick, float delta) {
        var time = (worldTick * 0.01F) * 0.2f * animationSpeed + offset;

        currColor = ColorUtil.AHSVtoARGB(1f, MathHelper.fractionalPart(time), 1.0F, 1.0F);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("color.disco_lasers.rainbow");
    }

    @Override
    public UiManageable<? extends LaserColor> getUI() {
        if (ui == null)
            ui = UI_FACTORY.getValue().create(this);

        return ui;
    }
}
