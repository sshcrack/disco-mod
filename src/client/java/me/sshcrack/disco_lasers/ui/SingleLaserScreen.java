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
import org.jetbrains.annotations.Nullable;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class SingleLaserScreen extends BaseUIModelHandledScreen<FlowLayout, SingleLaserScreenHandler> {
    private static final ButtonComponent.Renderer SELECTED_RENDERER = ButtonComponent.Renderer.flat(
            Color.ofRgb(0x00FF00).argb(),
            Color.ofRgb(0x04ba04).argb(),
            Color.ofRgb(0x536153).argb()
    );

    @Nullable
    private LaserColor currentColor;
    @Nullable
    private LaserMode currentMode;
    private int currentIndex;
    @Nullable
    private List<LaserMode> modesInBlockEntity;

    public SingleLaserScreen(SingleLaserScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title, FlowLayout.class, BaseUIModelScreen.DataSource.asset(DiscoLasers.ref("single_laser")));
    }

    private void setColor(LaserColor color) {
        this.currentColor = color;
        assert currentColor != null;

        var root = this.uiAdapter.rootComponent;

        var settings = root.childById(FlowLayout.class, "color-settings");
        settings.clearChildren();


        var template = this.model.expandTemplate(Component.class, currentColor.getUI().getTemplateName(), new HashMap<>());
        settings.child(template);

        currentColor.getUI().initializeUI(this.uiAdapter.rootComponent);
    }

    private void setMode(int modeIndex) {
        assert modesInBlockEntity != null;

        if (modeIndex < 0 || modeIndex >= modesInBlockEntity.size()) {
            DiscoLasers.LOGGER.error("Invalid mode index: {}", modeIndex);
            return;
        }

        this.currentMode = modesInBlockEntity.get(modeIndex);
        this.currentIndex = modeIndex;

        assert currentMode != null;

        var root = this.uiAdapter.rootComponent;
        var modeSelect = root.childById(FlowLayout.class, "mode-select");
        for (Component child : modeSelect.children()) {
            var button = (ButtonComponent) child;
            var id = button.id();
            if (id == null) continue;

            if (!id.endsWith(currentMode.getUI().getTemplateName())) {
                button.renderer(ButtonComponent.Renderer.VANILLA);
                continue;
            }

            button.renderer(SELECTED_RENDERER);
        }

        var settings = root.childById(FlowLayout.class, "mode-settings");
        settings.clearChildren();

        var template = (ParentComponent) this.model.expandTemplate(Component.class, currentMode.getUI().getTemplateName(), new HashMap<>());
        settings.child(template);

        currentMode.getUI().initializeUI(template);
        updateColorCount();
        sendIndexUpdate();
    }

    private void updatePresetModeSelect() {
        if (modesInBlockEntity == null) {
            DiscoLasers.LOGGER.warn("Modes in block entity is null");
            return;
        }

        var presetModeSelect = uiAdapter.rootComponent.childById(FlowLayout.class, "preset-mode-select");
        presetModeSelect.clearChildren();

        for (int i = 0; i < modesInBlockEntity.size(); i++) {
            var laserMode = modesInBlockEntity.get(i);

            int finalI = i;
            ButtonComponent button = Components.button(Text.literal(String.valueOf(i)), e -> {
                currentIndex = finalI;
                updatePresetModeSelect();
            });
            button.id("preset-mode-select-" + laserMode.getUI().getTemplateName());

            if (currentIndex == i)
                button.renderer(SELECTED_RENDERER);

            presetModeSelect.child(button);
        }


        presetModeSelect.child(Components.button(Text.literal("Add New Mode"), e -> {
            var values = ModLaserModes.REGISTRY.getDefaultValues();
            var firstModeOpt = values.stream().findFirst();
            if (firstModeOpt.isEmpty()) {
                DiscoLasers.LOGGER.error("No default laser mode found");
                return;
            }

            var newMode = firstModeOpt.get().get();
            modesInBlockEntity.add(newMode);

            currentIndex = modesInBlockEntity.size() - 1;
            updatePresetModeSelect();
            sendUpdateModes();
        }).id("preset-mode-select-add-new"));

        presetModeSelect.child(Components.button(Text.literal("Remove Mode"), e -> {
            if (modesInBlockEntity.size() <= 1) {
                DiscoLasers.LOGGER.error("Cannot remove last mode");
                return;
            }

            modesInBlockEntity.remove(currentIndex);
            currentIndex = Math.max(0, currentIndex - 1);

            updatePresetModeSelect();
            sendUpdateModes();
        }).id("preset-mode-select-remove"));

        setMode(currentIndex);
    }

    private void recalculateRainbowOffsets() {
        if (currentMode == null) {
            DiscoLasers.LOGGER.error("Current mode is null");
            return;
        }

        var size = currentMode.getColors().size();
        for (int i = 0; i < size; i++) {
            var color = currentMode.getColors().get(i);
            if (color instanceof RainbowColor rColor) {
                rColor.setOffset((float) i / (float) size);
            }
        }
    }

    public void sendUpdateModes() {
        if (modesInBlockEntity == null)
            return;

        this.handler.sendMessage(new SingleLaserScreenHandler.SetBlockEntityLaserMode(modesInBlockEntity));
    }

    public void sendIndexUpdate() {
        this.handler.sendMessage(new SingleLaserScreenHandler.SetBlockEntityLaserIndex(currentIndex));
    }

    public void updateColorCount() {
        if (currentMode == null) {
            DiscoLasers.LOGGER.error("Current mode is null for update color count");
            return;
        }

        var colorCount = uiAdapter.rootComponent.childById(LabelComponent.class, "current-colors");
        colorCount.text(Text.literal(String.valueOf(currentMode.getColors().size())));
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent.child(Components.label(Text.literal("Not synced with server")).id("not-synced"));

        var modeSelect = rootComponent.childById(FlowLayout.class, "mode-select");

        for (Supplier<LaserMode> modeConstructor : ModLaserModes.REGISTRY.getDefaultValues()) {
            var mode = modeConstructor.get();

            modeSelect.child(Components.button(mode.getDisplayName(), e -> {
                if (modesInBlockEntity == null)
                    return;

                var newMode = modeConstructor.get();
                if (currentMode != null)
                    newMode.setColors(currentMode.getColors());

                modesInBlockEntity.set(currentIndex, newMode);

                setMode(currentIndex);
            }).id("mode-select-" + mode.getUI().getTemplateName()));
        }

        handler.registerInitialModeListener(msg -> {
            modesInBlockEntity = new ArrayList<>(msg.modes());
            currentIndex = msg.index();
            updatePresetModeSelect();

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
            if (currentMode == null || currentColor == null)
                return;

            currentMode.addColor(currentColor.clone());

            recalculateRainbowOffsets();
            updateColorCount();
            sendUpdateModes();
        });


        rootComponent.childById(ButtonComponent.class, "add-color-5x").onPress(e -> {
            if (currentMode == null || currentColor == null)
                return;

            for (int i = 0; i < 5; i++) {
                currentMode.addColor(currentColor.clone());
            }

            updateColorCount();
            recalculateRainbowOffsets();

            sendUpdateModes();
        });

        rootComponent.childById(ButtonComponent.class, "clear-colors").onPress(e -> {
            if (currentMode == null || currentColor == null)
                return;

            currentMode.setColors(new ArrayList<>());
            updateColorCount();

            sendUpdateModes();
        });

        rootComponent.childById(ButtonComponent.class, "export").onPress(e -> {
            var newPath = TinyFileDialogs.tinyfd_saveFileDialog("Choose Export Path", null, null, null);
            if (newPath == null)
                return;

            var file = new File(newPath);
            try (var writer = new FileWriter(file)) {
                writer.write(LaserMode.LASER_CODEC.listOf().encodeStart(JsonOps.INSTANCE, modesInBlockEntity).getOrThrow().toString());
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

                modesInBlockEntity = LaserMode.LASER_CODEC.listOf().decode(JsonOps.INSTANCE, json).getOrThrow().getFirst();
                updatePresetModeSelect();
                sendUpdateModes();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
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
            sendUpdateModes();
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }
}
