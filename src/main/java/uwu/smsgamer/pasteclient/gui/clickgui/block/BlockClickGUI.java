package uwu.smsgamer.pasteclient.gui.clickgui.block;

import net.minecraft.client.gui.*;
import org.lwjgl.input.Keyboard;
import uwu.smsgamer.pasteclient.gui.clickgui.block.blockguis.CategoryGUI;
import uwu.smsgamer.pasteclient.gui.clickgui.utils.*;
import uwu.smsgamer.pasteclient.utils.fontRenderer.GlyphPageFontRenderer;

import java.awt.*;
import java.io.IOException;

public class BlockClickGUI extends GuiScreen { // TODO: Keybind
    public static Color GUI_BACKGROUND = new Color(0F, 0F, 0F, 0.5F);
    public static Color GUI_COLOR = Color.WHITE;
    public static Color GUI_BORDER = Color.RED;

    public static Color CATEGORY = new Color(200, 200, 200);
    public static Color CATEGORY_HOVER = new Color(170, 170, 170);

    public static Color MODULE_ON = new Color(160, 200, 160);
    public static Color MODULE_ON_HOVER = new Color(120, 170, 120);
    public static Color MODULE_OFF = CATEGORY;
    public static Color MODULE_OFF_HOVER = CATEGORY_HOVER;

    public static Color DEFAULT = new Color(180, 180, 180);
    public static Color DEFAULT_HOVER = new Color(100, 100, 100);

    public static Color BACK = new Color(230, 20, 20);
    public static Color BACK_HOVER = new Color(180, 0, 0);

    public static Color CHOICE = CATEGORY;
    public static Color CHOICE_HOVER = CATEGORY_HOVER;

    public static Color CHOICE_UNSELECT = MODULE_OFF;
    public static Color CHOICE_UNSELECT_HOVER = MODULE_OFF_HOVER;
    public static Color CHOICE_SELECT = MODULE_ON;
    public static Color CHOICE_SELECT_HOVER = MODULE_ON_HOVER;

    public static Color NUM_BG = CATEGORY;
    public static Color NUM_BG_HOVER = CATEGORY_HOVER;
    public static Color NUM_BG_SELECT = new Color(120, 120, 120);
    public static Color NUM_SLIDER = new Color(130, 200, 130);
    public static Color NUM_SLIDER_HOVER = new Color(80, 150, 80);
    public static Color NUM_SLIDER_SELECT = new Color(80, 120, 80);

    public static Color STR = NUM_BG;
    public static Color STR_HOVER = NUM_BG_HOVER;
    public static Color STR_SELECT = NUM_BG_SELECT;

    public static Color ADD = new Color(80, 230, 80);
    public static Color ADD_HOVER = new Color(60, 150, 60);

    public static final IRenderer renderer;

    static {
        GlyphPageFontRenderer consolas = GlyphPageFontRenderer.create("Montserrat", 15, false, false, false);
        renderer = new ClientBaseRendererImpl(consolas);
    }

    public String currentDescription = "";

    private final BlockGUI blockGUI = new CategoryGUI();

    private static BlockClickGUI instance;

    public static BlockClickGUI getInstance() {
        if (instance == null) instance = new BlockClickGUI();
        return instance;
    }

    private BlockClickGUI() {
    }

    public int getMidScreenX() {
        ScaledResolution res = new ScaledResolution(mc);
        return res.getScaledWidth();
    }

    public int getMidScreenY() {
        ScaledResolution res = new ScaledResolution(mc);
        return res.getScaledHeight();
    }

    private long lastRender = System.currentTimeMillis();

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        long now = System.currentTimeMillis();
        blockGUI.render(mouseX, mouseY, (int) (now - lastRender));
        if (currentDescription != null && !currentDescription.isEmpty()) {
            int height = renderer.getStringHeight(currentDescription);
            int width = renderer.getStringWidth(currentDescription);
            renderer.drawRect(0, getMidScreenY() * 2 - height - 6, width + 10, height + 6, Color.BLACK);
            renderer.drawOutline(0, getMidScreenY() * 2 - height - 6, width + 10, height + 6, 2, Color.WHITE);
            renderer.drawString(4, getMidScreenY() * 2 - height - 4, currentDescription, Color.WHITE);
        }
        lastRender = now;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        blockGUI.click(mouseX, mouseY, mouseButton, 0);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceLastClick) {
        blockGUI.click(mouseX, mouseY, mouseButton, 1);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        blockGUI.click(mouseX, mouseY, mouseButton, 2);
    }

    /*
    if (keyCode == 1) {
            close();
        } else blockGUI.keyPress(typedChar, keyCode);
     */

    @Override
    public void handleKeyboardInput() {
        char typedChar = Keyboard.getEventCharacter();
        int keyCode = Keyboard.getEventKey();
        if (keyCode == 1) {
            close();
        } else blockGUI.keyPress(typedChar, keyCode, Keyboard.getEventKeyState() ?
          (Keyboard.isRepeatEvent() ? 1 : 0) : 2);
//        if (Keyboard.getEventKey() == 0 && keyChar >= ' ' || Keyboard.getEventKeyState()) {
//            this.keyTyped(keyChar, Keyboard.getEventKey());
//        }

        this.mc.dispatchKeypresses();
    }

    public void close() {
        blockGUI.setChild(null); // just to make sure
        this.mc.displayGuiScreen(null);
        if (this.mc.currentScreen == null) {
            this.mc.setIngameFocus();
        }
    }
}
