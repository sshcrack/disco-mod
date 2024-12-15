package me.sshcrack.disco_lasers.ui.initializers;

import me.sshcrack.disco_lasers.blocks.modes.RandomMode;
import me.sshcrack.disco_lasers.blocks.modes.SpreadMode;
import me.sshcrack.disco_lasers.ui.initializers.color.RainbowColorManage;
import me.sshcrack.disco_lasers.ui.initializers.color.StaticColorManage;
import me.sshcrack.disco_lasers.ui.initializers.mode.RandomModeManage;
import me.sshcrack.disco_lasers.ui.initializers.mode.SpreadModeManage;
import me.sshcrack.disco_lasers.util.color.RainbowColor;
import me.sshcrack.disco_lasers.util.color.StaticColor;

public class ManageableRegistry {
    public static void initialize() {
        RandomMode.UI_FACTORY.setValue(RandomModeManage::new);
        SpreadMode.UI_FACTORY.setValue(SpreadModeManage::new);

        RainbowColor.UI_FACTORY.setValue(RainbowColorManage::new);
        StaticColor.UI_FACTORY.setValue(StaticColorManage::new);
    }
}
