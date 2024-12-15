package me.sshcrack.disco_lasers.ui.initializers.mode;

import io.wispforest.owo.ui.component.DiscreteSliderComponent;
import io.wispforest.owo.ui.core.ParentComponent;
import me.sshcrack.disco_lasers.blocks.modes.RandomMode;
import me.sshcrack.disco_lasers.screen.UiManageable;

public class RandomModeManage extends UiManageable<RandomMode> {
    public RandomModeManage(RandomMode data) {
        super(data);
    }


    @Override
    public void initializeUI(ParentComponent root) {
        var maxAngle = root.childById(DiscreteSliderComponent.class, "mode.random.max-angle");
        var laserAge = root.childById(DiscreteSliderComponent.class, "mode.random.laser-age");
        var laserSpeed = root.childById(DiscreteSliderComponent.class, "mode.random.laser-speed");
        var laserAmount = root.childById(DiscreteSliderComponent.class, "mode.random.laser-amount");

        maxAngle.setFromDiscreteValue(data.getMaxAngle());
        laserAge.setFromDiscreteValue(data.getLaserAge());
        laserSpeed.setFromDiscreteValue(data.getLaserSpeed());
        laserAmount.setFromDiscreteValue(data.getLaserAmount());

        maxAngle.onChanged().subscribe(e -> data.setMaxAngle((float) e));
        laserAge.onChanged().subscribe(e -> data.setLaserAge((float) e));
        laserSpeed.onChanged().subscribe(e -> data.setLaserSpeed((float) e));
        laserAmount.onChanged().subscribe(e -> data.setLaserAmount((int) e));
    }

    @Override
    public String getTemplateName() {
        return "mode.random";
    }
}
