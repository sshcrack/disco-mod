package me.sshcrack.disco_lasers.util.color;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import me.sshcrack.disco_lasers.screen.UiManageable;
import net.minecraft.text.Text;
import org.apache.commons.lang3.mutable.MutableObject;

public class StaticColor implements LaserColor {
    public static MutableObject<UiManageable.UiManageableFactory<StaticColor>> UI_FACTORY = new MutableObject<>();
    public static final MapCodec<StaticColor> CODEC = Codec.INT.xmap(StaticColor::new, StaticColor::getARGB).fieldOf("color");
    private int color;
    private UiManageable<StaticColor> ui;

    public StaticColor(int color) {
        this.color = color;
    }

    @Override
    public int getARGB() {
        return color;
    }

    public void setARGB(int color) {
        this.color = color;
    }

    @Override
    public void tick(float worldTick, float delta) {
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("color.disco_lasers.static");
    }


    @Override
    public UiManageable<? extends LaserColor> getUI() {
        if (ui == null)
            ui = UI_FACTORY.getValue().create(this);

        return ui;
    }
}
