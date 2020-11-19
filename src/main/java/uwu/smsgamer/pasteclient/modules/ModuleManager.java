/*
 * Copyright (c) 2018 superblaubeere27
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package uwu.smsgamer.pasteclient.modules;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import uwu.smsgamer.pasteclient.PasteClient;
import uwu.smsgamer.pasteclient.events.KeyEvent;
import uwu.smsgamer.pasteclient.modules.modules.combat.AimBot;
import uwu.smsgamer.pasteclient.modules.modules.fun.DemoModeModule;
import uwu.smsgamer.pasteclient.modules.modules.misc.*;
import uwu.smsgamer.pasteclient.modules.modules.movement.*;
import uwu.smsgamer.pasteclient.modules.modules.render.ClickGUIModule;
import uwu.smsgamer.pasteclient.modules.modules.render.HUD;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ModuleManager { //todo: THIS NEEDS TO BE REWORKED

    @NotNull
    private final List<Module> modules = new ArrayList<>();

    @NotNull
    private final EnumMap<ModuleCategory, List<Module>> categoryModuleMap = new EnumMap<>(ModuleCategory.class);

    public ModuleManager() {
        for (ModuleCategory value : ModuleCategory.values()) {
            categoryModuleMap.put(value, new ArrayList<>());
        }
        EventManager.register(this);
    }


    public void addModules() {
        //Combat
        addModule(new AimBot());

        //Fun
        addModule(new DemoModeModule());

        //Misc
        addModule(new ValuesTest());
        addModule(new Disabler());

        //Movement
        addModule(new Fly());
        addModule(new Speed());
        addModule(new SetbackDetector());

        //Render
        addModule(new ClickGUIModule());
        addModule(new HUD());
    }

    private void addModule(@NotNull Module module) {
        modules.add(module);
        categoryModuleMap.get(module.getCategory()).add(module);
        EventManager.register(module);
//        PasteClient.INSTANCE.valueManager.registerObject(module.getName(), module);
    }

    @NotNull
    public List<Module> getModules() {
        return modules;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public <T extends Module> T getModule(Class<T> clazz) {
        return (T) Objects.requireNonNull(modules.stream().filter(mod -> mod.getClass() == clazz).findFirst().orElse(null));
    }

    public Module getModule(@NotNull String name, boolean caseSensitive) {
        return modules.stream().filter(mod -> !caseSensitive && name.equalsIgnoreCase(mod.getName()) || name.equals(mod.getName())).findFirst().orElse(null);
    }

    public List<Module> getModules(@NotNull ModuleCategory category) {
        return categoryModuleMap.get(category);
    }

    @EventTarget
    private void onKey(@NotNull KeyEvent event) {
        for (Module module : modules) if (module.getKeybind() == event.getKey()) module.setState(!module.getState());
    }

    /*public void addScriptModule(ScriptModule module) {
        addModule(module);
    }*/
}
