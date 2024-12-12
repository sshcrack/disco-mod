package me.sshcrack.disco_lasers;

import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.VoicechatServerStartedEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VoiceChatServerPlugin implements VoicechatPlugin {
    @Nullable
    public static VoicechatServerApi voicechatServerApi;
    private static final List<Runnable> runnables = new ArrayList<>();


    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(VoicechatServerStartedEvent.class, this::onServerStarted);
    }

    @Override
    public String getPluginId() {
        return "disco_lasers";
    }

    private void onServerStarted(VoicechatServerStartedEvent event) {
        voicechatServerApi = event.getVoicechat();

        synchronized (runnables) {
            runnables.forEach(Runnable::run);
            runnables.clear();
        }
    }
}
