package me.sshcrack.disco_lasers.ui.initializers;

import io.wispforest.owo.ui.core.Component;

public abstract class UiManageable<T> {
    protected T data;

    public UiManageable(T data) {
        this.data = data;
    }

    /**
     * Update the components of the shown UI to reflect the current state of the color
     * Also add event listeners and other stuff here
     *
     * @param parent The parent component of the UI
     */
    public abstract void initializeUI(Component parent);

    public abstract String getTemplateName();
}
