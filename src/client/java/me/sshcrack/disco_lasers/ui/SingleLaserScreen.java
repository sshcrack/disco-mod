package me.sshcrack.disco_lasers.ui;

import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import io.wispforest.owo.ui.base.BaseUIModelHandledScreen;
import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Color;
import io.wispforest.owo.ui.core.Component;
import io.wispforest.owo.ui.core.ParentComponent;
import me.sshcrack.disco_lasers.DiscoLasers;
import me.sshcrack.disco_lasers.blocks.modes.LaserMode;
import me.sshcrack.disco_lasers.registries.ModLaserColor;
import me.sshcrack.disco_lasers.registries.ModLaserModes;
import me.sshcrack.disco_lasers.screen.SingleLaserScreenHandler;
import me.sshcrack.disco_lasers.util.color.LaserColor;
import me.sshcrack.disco_lasers.util.color.RainbowColor;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import java.io.*;
import java.util.ArrayList;
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

        var settings = root.childById(FlowLayout.class, "color-settings");
        settings.clearChildren();


        var template = this.model.expandTemplate(Component.class, currentColor.getUI().getTemplateName(), new HashMap<>());
        settings.child(template);

        currentColor.getUI().initializeUI(this.uiAdapter.rootComponent);
    }

    private void setMode(LaserMode mode, boolean sendUpdate) {
        this.currentMode = mode;
        var root = this.uiAdapter.rootComponent;
        var modeSelect = root.childById(FlowLayout.class, "mode-select");
        for (Component child : modeSelect.children()) {
            var button = (ButtonComponent) child;
            var id = button.id();
            if (id == null) continue;

            if (!id.contains(mode.getUI().getTemplateName())) {
                button.renderer(ButtonComponent.Renderer.VANILLA);
                continue;
            }

            button.renderer(ButtonComponent.Renderer.flat(
                    Color.ofRgb(0x00FF00).argb(),
                    Color.ofRgb(0x04ba04).argb(),
                    Color.ofRgb(0x536153).argb()
            ));
        }

        var settings = root.childById(FlowLayout.class, "mode-settings");
        settings.clearChildren();

        var template = (ParentComponent) this.model.expandTemplate(Component.class, currentMode.getUI().getTemplateName(), new HashMap<>());
        settings.child(template);

        currentMode.getUI().initializeUI(template);

        updateColorCount();
        if (sendUpdate)
            sendMode(currentMode);
    }

    private void recalculateRainbowOffsets() {
        var size = currentMode.getColors().size();
        for (int i = 0; i < size; i++) {
            var color = currentMode.getColors().get(i);
            if (color instanceof RainbowColor rColor) {
                rColor.setOffset((float) i / (float) size);
            }
        }
    }

    public void sendMode(LaserMode mode) {
        this.handler.sendMessage(new SingleLaserScreenHandler.SetBlockEntityLaserMode(mode));
    }

    public void updateColorCount() {
        var colorCount = uiAdapter.rootComponent.childById(LabelComponent.class, "current-colors");
        colorCount.text(Text.literal(String.valueOf(currentMode.getColors().size())));
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent.child(Components.label(Text.literal("Not synced with server")).id("not-synced"));
        var modeSelect = rootComponent.childById(FlowLayout.class, "mode-select");

        LaserMode firstMode = null;

        for (Supplier<LaserMode> modeConstructor : ModLaserModes.REGISTRY.getDefaultValues()) {
            var mode = modeConstructor.get();
            if (firstMode == null) firstMode = mode;

            modeSelect.child(Components.button(mode.getDisplayName(), e -> {
                var newMode = modeConstructor.get();
                newMode.setColors(currentMode.getColors());

                setMode(newMode, true);
            }).id("mode-select-" + mode.getUI().getTemplateName()));
        }

        if (firstMode != null)
            setMode(firstMode, false);

        handler.registerInitialModeListener(mode -> {
            this.setMode(mode, false);
            this.uiAdapter.rootComponent.childById(LabelComponent.class, "not-synced").remove();
        }, true);


        var colorSelector = rootComponent.childById(FlowLayout.class, "color-select");
        LaserColor firstColor = null;
        for (Supplier<LaserColor> colorConstructor : ModLaserColor.REGISTRY.getDefaultValues()) {
            var color = colorConstructor.get();
            if (firstColor == null) firstColor = color;

            colorSelector.child(
                    Components.button(color.getDisplayName(), button -> setColor(color)).id("color-select-" + color.getUI().getTemplateName())
            );
        }

        if (firstColor != null)
            setColor(firstColor);

        rootComponent.childById(ButtonComponent.class, "add-color").onPress(e -> {
            currentMode.addColor(currentColor.clone());

            recalculateRainbowOffsets();
            updateColorCount();
            sendMode(currentMode);
        });


        rootComponent.childById(ButtonComponent.class, "add-color-5x").onPress(e -> {
            for (int i = 0; i < 5; i++) {
                currentMode.addColor(currentColor.clone());
            }

            updateColorCount();
            recalculateRainbowOffsets();

            sendMode(currentMode);
        });

        updateColorCount();
        rootComponent.childById(ButtonComponent.class, "clear-colors").onPress(e -> {
            currentMode.setColors(new ArrayList<>());
            updateColorCount();

            sendMode(currentMode);
        });

        rootComponent.childById(ButtonComponent.class, "export").onPress(e -> {
            var newPath = TinyFileDialogs.tinyfd_saveFileDialog("Choose Export Path", null, null, null);
            if (newPath == null)
                return;

            var file = new File(newPath);
            try (var writer = new FileWriter(file)) {
                writer.write(LaserMode.LASER_CODEC.encodeStart(JsonOps.INSTANCE, currentMode).getOrThrow().toString());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        rootComponent.childById(ButtonComponent.class, "import").onPress(e -> {
            var newPath = TinyFileDialogs.tinyfd_openFileDialog("Choose file to import", null, null, null, false);
            if (newPath == null)
                return;

            var file = new File(newPath);
            try (var reader = new BufferedReader(new FileReader(file))) {
                var json = JsonParser.parseReader(reader);
                var mode = LaserMode.LASER_CODEC.decode(JsonOps.INSTANCE, json).getOrThrow().getFirst();

                setMode(mode, true);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        rootComponent.childById(ButtonComponent.class, "update").onPress(e -> {
            sendMode(currentMode);
        });
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        var isOutside = this.isClickOutsideBounds(mouseX, mouseY, x, y, button);
        if (!isOutside) {
            sendMode(currentMode);
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }
}
