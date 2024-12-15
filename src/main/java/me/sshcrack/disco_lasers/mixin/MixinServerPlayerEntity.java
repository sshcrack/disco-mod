package me.sshcrack.disco_lasers.mixin;

import me.sshcrack.disco_lasers.screen.SingleLaserScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class MixinServerPlayerEntity {
    @Inject(method = "onScreenHandlerOpened", at = @At("TAIL"))
    public void onScreenHandlerOpened(ScreenHandler screenHandler, CallbackInfo ci) {
        if (screenHandler instanceof SingleLaserScreenHandler l) {
            l.sendInitialPacket();
        }
    }
}
