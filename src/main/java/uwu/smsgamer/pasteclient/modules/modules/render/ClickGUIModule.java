/*
 * Copyright (c) 2018 superblaubeere27
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package uwu.smsgamer.pasteclient.modules.modules.render;

import org.lwjgl.input.Keyboard;
import uwu.smsgamer.pasteclient.gui.clickgui.ClickGUI;
import uwu.smsgamer.pasteclient.modules.*;
import uwu.smsgamer.pasteclient.values.IntChoiceValue;

public class ClickGUIModule extends Module {
    public static final ClickGUI clickGui = new ClickGUI();

    public ClickGUIModule() {
        super("ClickGUI", "The click gui", ModuleCategory.RENDER, true, false, Keyboard.KEY_RSHIFT);
    }

    @Override
    protected void onEnable() {
        mc.displayGuiScreen(clickGui.getSelectedGUI());
        setState(false);
    }

    @Override
    public void setState(boolean state) {
        this.state = state;
        if (state) onEnable();
        else onDisable();
    }
}
