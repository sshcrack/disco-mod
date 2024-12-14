package me.sshcrack.disco_lasers.util.color;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.wispforest.owo.ui.component.ColorPickerComponent;
import io.wispforest.owo.ui.core.Color;
import io.wispforest.owo.ui.core.Component;
import net.minecraft.text.Text;

public class StaticColor implements LaserColor {
    public static final MapCodec<StaticColor> CODEC = Codec.INT.xmap(StaticColor::new, StaticColor::getARGB).fieldOf("color");
    private int color;

    public StaticColor(int color) {
        this.color = color;
    }

    @Override
    public int getARGB() {
        return color;
    }

    @Override
    public void tick(float worldTick, float delta) {
    }

    @Override
    public String getTemplateName() {
        return "color.static";
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("color.disco_lasers.static");
    }


    @Override
    public void initializeUI(Component parent) {
        var picker = ((ColorPickerComponent) parent.id("static-color"));
        picker.selectedColor(Color.ofArgb(color));

        picker.onChanged().subscribe(e -> color = e.argb());
    }
}
