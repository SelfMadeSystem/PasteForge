package uwu.smsgamer.pasteclient.gui.selectors;

import net.minecraft.client.gui.GuiScreen;
import uwu.smsgamer.pasteclient.utils.Colour;
import uwu.smsgamer.pasteclient.values.ColorValue;

import java.io.IOException;

//todo
public class ColorSelectorGUI extends GuiScreen {
    public final GuiScreen prev;
    public final ColorValue value;
    public Colour.HSV hsv;
    public Colour.RGB rgb;

    public ColorSelectorGUI(GuiScreen prev, ColorValue value) {
        this.prev = prev;
        this.value = value;
        this.rgb = new Colour.RGB(value.getValue());
        this.hsv = Colour.rgb2hsv(value.getValue());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    }

    protected void keyTyped(char p_keyTyped_1_, int p_keyTyped_2_) {
        if (p_keyTyped_2_ == 1) {
            this.mc.displayGuiScreen(prev);
            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        //blockGUI.click(mouseX, mouseY, mouseButton, 0);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceLastClick) {
        //blockGUI.click(mouseX, mouseY, mouseButton, 1);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        //blockGUI.click(mouseX, mouseY, mouseButton, 2);
    }

    static class Slider {
    }

    static class HSVSlider extends Slider {
    }

    static class RGBSlider extends Slider {
    }
}
