package me.sshcrack.disco_lasers.ui.initializers.mode;

import io.wispforest.owo.ui.core.ParentComponent;
import me.sshcrack.disco_lasers.blocks.modes.OffMode;
import me.sshcrack.disco_lasers.screen.UiManageable;

public class OffModeManage extends UiManageable<OffMode> {
    public OffModeManage(OffMode data) {
        super(data);
    }

    @Override
    public void initializeUI(ParentComponent root) {

    }

    @Override
    public String getTemplateName() {
        return "mode.off";
    }
}
