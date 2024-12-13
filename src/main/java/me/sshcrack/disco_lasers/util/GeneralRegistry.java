package me.sshcrack.disco_lasers.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import me.sshcrack.disco_lasers.blocks.modes.LaserMode;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class GeneralRegistry<T> {
    private final HashMap<Identifier, GeneralRegistryEntry<? extends T>> registeredTypes = new HashMap<>();

    public void register(Identifier identifier, GeneralRegistryEntry<? extends T> registryEntry) {
        registeredTypes.put(identifier, registryEntry);
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

}
