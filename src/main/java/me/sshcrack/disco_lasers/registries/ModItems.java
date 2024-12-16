package me.sshcrack.disco_lasers.registries;

import me.sshcrack.disco_lasers.DiscoLasers;
import me.sshcrack.disco_lasers.items.LaserConfiguratorItem;
import me.sshcrack.disco_lasers.items.LaserControllerItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModItems {
    public static <T extends Item> T register(String name, T item) {
        var id = DiscoLasers.ref(name);
        return Registry.register(Registries.ITEM, id, item);
    }

    public static LaserControllerItem LASER_CONTROLLER_ITEM = register("laser_controller", new LaserControllerItem(new Item.Settings().maxCount(1)));
    public static LaserConfiguratorItem LASER_CONFIGURATOR_ITEM = register("laser_configurator", new LaserConfiguratorItem(new Item.Settings().maxCount(1)));

    public static void initialize() {

    }
}
