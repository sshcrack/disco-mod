package me.sshcrack.disco_lasers.renderer.modes.registry;

import me.sshcrack.disco_lasers.blocks.modes.LaserMode;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class LaserModeRendererRegistry {
    private static final Map<Class<? extends LaserMode>, LaserModeRendererFactory<?>> RENDERERS = new HashMap<>();

    @FunctionalInterface
    public interface LaserModeRendererFactory<T extends LaserMode> {
        LaserModeRenderer<T> create();
    }

    public static <T extends LaserMode> void register(Class<T> modeClass, LaserModeRendererFactory<T> rendererFactory) {
        RENDERERS.put(modeClass, rendererFactory);
    }

    public static <T extends LaserMode> LaserModeRenderer<T> createRenderer(@NotNull LaserMode mode) {
        var factory = RENDERERS.get(mode.getClass());
        if (factory == null)
            throw new IllegalArgumentException("No renderer factory found for mode " + mode);

        //noinspection unchecked
        var renderer = (LaserModeRenderer<T>) factory.create();

        //noinspection unchecked
        renderer.underlyingModeClass = (Class<T>) mode.getClass();

        return renderer;
    }
}
