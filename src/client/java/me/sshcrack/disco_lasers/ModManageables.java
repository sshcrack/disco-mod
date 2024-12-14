package me.sshcrack.disco_lasers;

import com.mojang.serialization.Lifecycle;
import me.sshcrack.disco_lasers.blocks.modes.SpreadMode;
import me.sshcrack.disco_lasers.ui.initializers.UiManageable;
import me.sshcrack.disco_lasers.ui.initializers.registry.UiManageableRegistry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

public class ModManageables {
    public static void initialize() {
        UiManageableRegistry.register(SpreadMode.class, );
    }
}
