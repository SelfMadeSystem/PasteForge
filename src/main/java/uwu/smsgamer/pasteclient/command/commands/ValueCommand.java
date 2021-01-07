/*
 * Copyright (c) 2018 superblaubeere27
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package uwu.smsgamer.pasteclient.command.commands;

import org.jetbrains.annotations.NotNull;
import uwu.smsgamer.pasteclient.PasteClient;
import uwu.smsgamer.pasteclient.command.*;
import uwu.smsgamer.pasteclient.modules.PasteModule;
import uwu.smsgamer.pasteclient.utils.ChatUtils;
import uwu.smsgamer.pasteclient.values.*;

import java.util.*;

public class ValueCommand extends Command {
    public ValueCommand() {
        super("value", "val", "v", "set");
    }

    @Override
    public void run(String alias, @NotNull String[] args) {
        if (args.length < 3)
            throw new CommandException("Usage: ." + alias + " <module> <name> [2nd name] [3rd name...] <value>");

        Value<?> value;
        {
            PasteModule module = PasteClient.INSTANCE.moduleManager.getModule(args[0], false);
            if (module == null) throw new CommandException("Module: " + args[0] + " does not exist!");
            value = module.getValue(args[1]);
            if (value == null) throw new CommandException("Value: " + args[1] + " does not exist!");
        }

        for (int i = 2; i < args.length - 1; i++) {
            value = value.getChild(args[i]);
            if (value == null) throw new CommandException("Value: " + args[i] + " does not exist!");
        }
        String lastArg = args[args.length - 1];
        if (value instanceof BoolValue) {
            if (lastArg.equalsIgnoreCase("true") ||
              lastArg.equalsIgnoreCase("yes") ||
              lastArg.equalsIgnoreCase("on") ||
              lastArg.equalsIgnoreCase("enable") ||
              lastArg.equalsIgnoreCase("enabled")) {
                value.setValue(true);
            } else if (lastArg.equalsIgnoreCase("toggle") ||
              lastArg.equalsIgnoreCase("switch")) {
                value.setValue(!((BoolValue) value).getValue());
            } else value.setValue(false);
        } else if (value instanceof NumberValue) {
            Double doubleValue;
            try {
                doubleValue = Double.parseDouble(lastArg);
            } catch (NumberFormatException e) {
                throw new CommandException("Not a valid number: " + lastArg + "!");
            }
            if (((NumberValue) value).getType().equals(NumberValue.NumberType.INTEGER))
                ((NumberValue) value).setValue(doubleValue);
            else
                ((NumberValue) value).setValueRaw(doubleValue);
        } else if (value instanceof IntChoiceValue) {
            int doubleValue;
            try {
                doubleValue = Integer.parseInt(lastArg);
            } catch (NumberFormatException e) {
                throw new CommandException("Not a valid number: " + lastArg + "!");
            }
            value.setValue(doubleValue);
        }
        ChatUtils.success(value.getName() + " was set to: " + value.getValue());
    }

    @NotNull
    @Override
    public List<String> autocomplete(int arg, String[] args) {
        return new ArrayList<>();
    }
}
