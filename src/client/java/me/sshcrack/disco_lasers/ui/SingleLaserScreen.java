package me.sshcrack.disco_lasers.ui;

import io.wispforest.owo.ui.base.BaseUIModelHandledScreen;
import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.DropdownComponent;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.CollapsibleContainer;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Component;
import me.sshcrack.disco_lasers.DiscoLasers;
import me.sshcrack.disco_lasers.blocks.modes.LaserMode;
import me.sshcrack.disco_lasers.registries.ModLaserColor;
import me.sshcrack.disco_lasers.registries.ModLaserModes;
import me.sshcrack.disco_lasers.screen.SingleLaserScreenHandler;
import me.sshcrack.disco_lasers.util.color.LaserColor;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.function.Supplier;

public class SingleLaserScreen extends BaseUIModelHandledScreen<FlowLayout, SingleLaserScreenHandler> {
    private LaserColor currentColor;
    private LaserMode currentMode;

    public SingleLaserScreen(SingleLaserScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title, FlowLayout.class, BaseUIModelScreen.DataSource.asset(DiscoLasers.ref("single_laser")));
    }

    private void setColor(LaserColor color) {
        this.currentColor = color;
        var root = this.uiAdapter.rootComponent;

        var settings = (FlowLayout) root.id("color-settings");
        settings.clearChildren();


        var template = this.model.expandTemplate(Component.class, currentColor.getTemplateName(), new HashMap<>());
        settings.child(template);

        currentColor.initializeUI(template);
    }

    private void setMode(LaserMode mode) {
        this.currentMode = mode;
        var root = this.uiAdapter.rootComponent;
        var container = (CollapsibleContainer) root.id("collapsible-mode");
        var text = (LabelComponent) container.titleLayout().children().getFirst();

        text.text(currentMode.getDisplayName());

        var settings = (FlowLayout) root.id("mode-settings");
        settings.clearChildren();

        var template = this.model.expandTemplate(Component.class, currentMode.getTemplateName(), new HashMap<>());
        settings.child(template);

        currentMode.initializeUI(template);

        this.handler.laserMode.set(currentMode.clone());
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        var modeSelect = (DropdownComponent) rootComponent.id("mode-select");
        LaserMode firstMode = null;
        for (Supplier<LaserMode> modeConstructor : ModLaserModes.REGISTRY.getDefaultValues()) {
            var mode = modeConstructor.get();
            if (firstMode == null) firstMode = mode;

            modeSelect.button(mode.getDisplayName(), e -> setMode(mode));
        }


        var colorSelector = (FlowLayout) rootComponent.id("color-select");
        LaserColor firstColor = null;
        for (Supplier<LaserColor> colorConstructor : ModLaserColor.REGISTRY.getDefaultValues()) {
            var color = colorConstructor.get();
            if (firstColor == null) firstColor = color;

            colorSelector.child(
                    Components.button(color.getDisplayName(), button -> setColor(color))
            );
        }
        if (firstColor != null)
            setColor(firstColor);

        ((ButtonComponent) rootComponent.id("add-color")).onPress(e -> {
            currentMode.addColor(currentColor);
            this.handler.laserMode.set(currentMode.clone());
        });


        ((ButtonComponent) rootComponent.id("add-color-5x")).onPress(e -> {
            for (int i = 0; i < 5; i++) {
                currentMode.addColor(currentColor);
            }

            this.handler.laserMode.set(currentMode.clone());
        });
    }
}
