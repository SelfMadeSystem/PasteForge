package uwu.smsgamer.pasteclient.gui.clickgui;

import net.minecraft.client.gui.GuiScreen;
import uwu.smsgamer.pasteclient.gui.clickgui.block.BlockClickGUI;
import uwu.smsgamer.pasteclient.notifications.*;

public class ClickGUI {
    public final BlockClickGUI blockClickGUI = BlockClickGUI.getInstance();
    public int selected = 0;

    public GuiScreen getSelectedGUI() {
        switch (selected) {
            case 0:
                return blockClickGUI;
            default:
                NotificationManager.show(new Notification(NotificationType.ERROR, "GUI Error", "Selected: " + selected + " is not an option.", 1));
                throw new IllegalStateException("Selected: " + selected + " is not an option.");
        }
    }
}
