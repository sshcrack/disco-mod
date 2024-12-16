package me.sshcrack.disco_lasers.blocks.modes;

import com.mojang.serialization.MapCodec;
import me.sshcrack.disco_lasers.screen.UiManageable;
import net.minecraft.text.Text;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.List;

public class OffMode extends LaserMode {
    public static MutableObject<UiManageable.UiManageableFactory<OffMode>> UI_FACTORY = new MutableObject<>();

    public static MapCodec<OffMode> CODEC = MapCodec.unit(new OffMode());
    private UiManageable<OffMode> ui;

    public OffMode() {
        super(List.of());
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.disco_lasers.laser_block.modes.off");
    }

    @Override
    public UiManageable<? extends LaserMode> getUI() {
        if (ui == null)
            ui = UI_FACTORY.getValue().create(this);

        return ui;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Object clone() throws CloneNotSupportedException {
        return new OffMode();
    }
}
