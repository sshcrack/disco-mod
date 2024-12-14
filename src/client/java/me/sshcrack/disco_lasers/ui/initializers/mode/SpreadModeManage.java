package me.sshcrack.disco_lasers.ui.initializers.mode;

import io.wispforest.owo.ui.component.DiscreteSliderComponent;
import io.wispforest.owo.ui.core.Component;
import me.sshcrack.disco_lasers.blocks.modes.SpreadMode;
import me.sshcrack.disco_lasers.ui.initializers.UiManageable;
import net.minecraft.util.math.MathHelper;

public class SpreadModeManage extends UiManageable<SpreadMode> {
    public SpreadModeManage(SpreadMode data) {
        super(data);
    }

    @Override
    public void initializeUI(Component parent) {
        var spread = ((DiscreteSliderComponent) parent.id("mode.spread.spread-angle"));
        var tilt = ((DiscreteSliderComponent) parent.id("mode.spread.tilt-angle"));
        var multiplier = ((DiscreteSliderComponent) parent.id("mode.spread.laser-multiplier"));

        spread.value(data.getSpreadAngle());
        tilt.value(data.getTiltAngle());
        multiplier.value(data.getLaserMultiplier());

        spread.onChanged().subscribe(e -> data.setSpreadAngle((float) e * MathHelper.RADIANS_PER_DEGREE));
        tilt.onChanged().subscribe(e -> data.setTiltAngle((float) e * MathHelper.RADIANS_PER_DEGREE));
        multiplier.onChanged().subscribe(e -> data.setLaserMultiplier((int) e));
    }

    @Override
    public String getTemplateName() {
        return "mode.spread";
    }

}
