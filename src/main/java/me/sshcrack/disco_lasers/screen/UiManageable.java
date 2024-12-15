package me.sshcrack.disco_lasers.screen;

import io.wispforest.owo.ui.core.ParentComponent;
import me.sshcrack.disco_lasers.blocks.modes.LaserMode;

public abstract class UiManageable<T> {
    @FunctionalInterface
    public interface UiManageableFactory<T> {
        UiManageable<T> create(T data);
    }

    protected T data;

    public UiManageable(T data) {
        this.data = data;
    }

    /**
     * Update the components of the shown UI to reflect the current state of the color
     * Also add event listeners and other stuff here
     *
     * @param root The parent component of the UI
     */
    public abstract void initializeUI(ParentComponent root);

    public abstract String getTemplateName();
}
