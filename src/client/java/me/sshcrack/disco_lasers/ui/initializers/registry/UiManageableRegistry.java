package me.sshcrack.disco_lasers.ui.initializers.registry;

import me.sshcrack.disco_lasers.ui.initializers.UiManageable;

import java.util.HashMap;
import java.util.function.Function;

public class UiManageableRegistry {
    private static final HashMap<Class<?>, Function<Object, UiManageable<?>>> registry = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> void register(Class<T> clazz, Function<T, UiManageable<T>> manageable) {
        registry.put(clazz, o -> manageable.apply((T) o));
    }

    public static UiManageable<?> get(Object object) {
        return registry.get(object.getClass()).apply(object);
    }
}
