package uwu.smsgamer.pasteclient.gui.clickgui;

import net.minecraft.client.gui.GuiScreen;
import uwu.smsgamer.pasteclient.gui.clickgui.block.BlockClickGUI;
import uwu.smsgamer.pasteclient.gui.clickgui.sesq.SesqClickGUI;
import uwu.smsgamer.pasteclient.notifications.*;

public class ClickGUI {
    public final BlockClickGUI blockClickGUI = BlockClickGUI.getInstance();
    public final SesqClickGUI sesqClickGUI = SesqClickGUI.getInstance();
    public int selected = 0;

    public GuiScreen getSelectedGUI() {
        switch (selected) {
            case 0:
                return blockClickGUI;
            case 1:
                return sesqClickGUI;
            default:
                NotificationManager.show(new Notification(NotificationType.ERROR, "GUI Error", "Selected: " + selected + " is not an option.", 1));
                throw new IllegalStateException("Selected: " + selected + " is not an option.");
        }
    }
}
