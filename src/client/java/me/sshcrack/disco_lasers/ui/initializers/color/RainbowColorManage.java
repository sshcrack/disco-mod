package me.sshcrack.disco_lasers.ui.initializers.color;

import io.wispforest.owo.ui.component.DiscreteSliderComponent;
import io.wispforest.owo.ui.core.ParentComponent;
import me.sshcrack.disco_lasers.screen.UiManageable;
import me.sshcrack.disco_lasers.util.color.RainbowColor;

public class RainbowColorManage extends UiManageable<RainbowColor> {
    public RainbowColorManage(RainbowColor data) {
        super(data);
    }

    @Override
    public String getTemplateName() {
        return "color.rainbow";
    }

    @Override
    public void initializeUI(ParentComponent root) {
        var speed = root.childById(DiscreteSliderComponent.class, "rainbow-speed");

        speed.value(data.getAnimationSpeed());
        speed.onChanged().subscribe(e -> data.setAnimationSpeed((float) e));
    }
}
