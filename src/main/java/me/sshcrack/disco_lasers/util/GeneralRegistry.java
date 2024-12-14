package me.sshcrack.disco_lasers.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.function.Supplier;

public class GeneralRegistry<T> {
    private final HashMap<Identifier, GeneralRegistryEntry<? extends T>> registeredTypes = new HashMap<>();
    private final HashMap<Identifier, Supplier<T>> defaultValues = new HashMap<>();

    public <K extends T> void register(Identifier identifier, GeneralRegistryEntry<K> registryEntry, @Nullable Supplier<K> defaultValue) {
        registeredTypes.put(identifier, registryEntry);

        if (defaultValue != null)
            //noinspection unchecked
            defaultValues.put(identifier, (Supplier<T>) defaultValue);
    }

    @Nullable
    public <K extends T> Identifier getId(K mode) {
        for (var entry : registeredTypes.entrySet()) {
            if (!entry.getValue().instanceClass().isInstance(mode))
                continue;

            return entry.getKey();
        }

        return null;
    }

    public MapCodec<? extends T> getEntryCodec(Identifier identifier) {
        return registeredTypes.get(identifier).codec();
    }

    public Codec<T> getSerializableCodec() {
        return Identifier.CODEC.dispatch(this::getId, this::getEntryCodec);
    }

    public Collection<Supplier<T>> getDefaultValues() {
        return defaultValues.values();
    }
}
