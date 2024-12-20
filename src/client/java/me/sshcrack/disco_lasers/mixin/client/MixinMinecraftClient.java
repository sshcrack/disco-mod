package me.sshcrack.disco_lasers.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import me.sshcrack.disco_lasers.DoAttackCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {
    @Shadow @Nullable public ClientWorld world;

    @Shadow @Nullable public ClientPlayerEntity player;

    @Inject(method="doAttack", at= @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;resetLastAttackedTicks()V"))
    private void onAttack(CallbackInfoReturnable<Boolean> cir, @Local ItemStack stack) {
        DoAttackCallback.EVENT.invoker().onAttack(player, world, stack);
    }
}
