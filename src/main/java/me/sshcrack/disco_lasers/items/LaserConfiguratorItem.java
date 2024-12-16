package me.sshcrack.disco_lasers.items;

import me.sshcrack.disco_lasers.blocks.LaserBlockEntity;
import me.sshcrack.disco_lasers.registries.ModDataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

public class LaserConfiguratorItem extends Item {
    public LaserConfiguratorItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var stack = context.getStack();
        var entity = context.getWorld().getBlockEntity(context.getBlockPos());

        if (!(entity instanceof LaserBlockEntity laser)) {
            return ActionResult.FAIL;
        }

        if (context.getWorld().isClient)
            return ActionResult.SUCCESS;

        if (context.getPlayer() == null)
            return ActionResult.FAIL;

        if (context.getPlayer().isSneaking()) {
            context.getPlayer().sendMessage(Text.translatable("item.disco_lasers.laser_configurator.copied"), true);
            stack.set(ModDataComponentTypes.LASER_DATA, new ModDataComponentTypes.LaserData(laser.getLaserModes(), laser.getCurrentIndex()));
        } else {
            var data = stack.get(ModDataComponentTypes.LASER_DATA);
            if (data == null)
                return ActionResult.FAIL;

            laser.setLaserModes(data.mode());
            laser.setCurrentIndex(data.index());
            context.getWorld().markDirty(context.getBlockPos());
            ((ServerWorld) context.getWorld()).getChunkManager().markForUpdate(context.getBlockPos());
            context.getPlayer().sendMessage(Text.translatable("item.disco_lasers.laser_configurator.pasted"), true);
        }

        return ActionResult.SUCCESS;
    }
}
