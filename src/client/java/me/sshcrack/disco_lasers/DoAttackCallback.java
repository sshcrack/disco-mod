package me.sshcrack.disco_lasers;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

public interface DoAttackCallback {
    Event<DoAttackCallback> EVENT = EventFactory.createArrayBacked(DoAttackCallback.class, (listeners) -> (player, world, stack) -> {
        for (DoAttackCallback event : listeners) {
            ActionResult result = event.onAttack(player, world, stack);
            if (result != ActionResult.PASS) {
                return result;
            }
        }

        return ActionResult.PASS;
    });

    ActionResult onAttack(PlayerEntity player, World world, ItemStack stack);
}
