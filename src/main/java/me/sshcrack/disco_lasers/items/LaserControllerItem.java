package me.sshcrack.disco_lasers.items;

import me.sshcrack.disco_lasers.blocks.LaserBlockEntity;
import me.sshcrack.disco_lasers.blocks.modes.OffMode;
import me.sshcrack.disco_lasers.registries.ModDataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class LaserControllerItem extends Item {
    public LaserControllerItem(Settings settings) {
        super(settings);
    }

    public List<BlockPos> getLinkedLasers(ItemStack stack) {
        var list = stack.get(ModDataComponentTypes.LINKED_LASERS);
        return list == null ? new ArrayList<>() : new ArrayList<>(list);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            return TypedActionResult.pass(user.getStackInHand(hand));
        }

        var stack = user.getStackInHand(hand);
        if (user.isSneaking()) {
            stack.set(ModDataComponentTypes.LINKED_LASERS, new ArrayList<>());
            user.sendMessage(Text.translatable("item.disco_lasers.laser_controller.clear"), true);
            return TypedActionResult.success(stack);
        }

        var list = stack.get(ModDataComponentTypes.LINKED_LASERS);
        if (list != null && !list.isEmpty()) {
            for (BlockPos pos : list) {
                var blockEntity = world.getBlockEntity(pos);
                if (!(blockEntity instanceof LaserBlockEntity laser))
                    continue;


                var size = laser.getLaserModes().size();

                var currIndex = laser.getCurrentIndex();
                var newIndex = -1;
                for (int x = 0; x <= size; x++) {
                    var z = (currIndex + x + 1) % size;
                    var mode = laser.getLaserModes().get(z);
                    if (mode instanceof OffMode) {
                        continue;
                    }

                    newIndex = z;
                    break;
                }

                if(newIndex != -1)
                    laser.setCurrentIndex(newIndex);
            }
        }

        return TypedActionResult.pass(stack);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var stack = context.getStack();
        var list = getLinkedLasers(stack);
        var entity = context.getWorld().getBlockEntity(context.getBlockPos());
        var alreadyLinked = list.contains(context.getBlockPos());

        if (!(entity instanceof LaserBlockEntity))
            return ActionResult.PASS;

        if (alreadyLinked)
            return ActionResult.PASS;

        list.add(context.getBlockPos());
        stack.set(ModDataComponentTypes.LINKED_LASERS, list);
        context.getPlayer().sendMessage(Text.translatable("item.disco_lasers.laser_controller.added", context.getBlockPos().toShortString()), true);

        return ActionResult.CONSUME;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        var list = stack.get(ModDataComponentTypes.LINKED_LASERS);
        var size = list == null ? 0 : list.size();

        tooltip.add(Text.translatable("item.disco_lasers.laser_controller.tooltip_1", size));
        tooltip.add(Text.translatable("item.disco_lasers.laser_controller.tooltip_2"));
        tooltip.add(Text.translatable("item.disco_lasers.laser_controller.tooltip_3"));
        tooltip.add(Text.translatable("item.disco_lasers.laser_controller.tooltip_4"));
    }
}
