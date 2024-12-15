package me.sshcrack.disco_lasers.ui.initializers.color;

import io.wispforest.owo.ui.component.ColorPickerComponent;
import io.wispforest.owo.ui.core.Color;
import io.wispforest.owo.ui.core.ParentComponent;
import me.sshcrack.disco_lasers.screen.UiManageable;
import me.sshcrack.disco_lasers.util.color.StaticColor;

public class StaticColorManage extends UiManageable<StaticColor> {
    public StaticColorManage(StaticColor data) {
        super(data);
    }

    @Override
    public String getTemplateName() {
        return "color.static";
    }


    @Override
    public void initializeUI(ParentComponent root) {
        var picker = root.childById(ColorPickerComponent.class, "static-color");
        picker.selectedColor(Color.ofArgb(data.getARGB()));

        picker.onChanged().subscribe(e -> data.setARGB(e.argb()));
    }
}
