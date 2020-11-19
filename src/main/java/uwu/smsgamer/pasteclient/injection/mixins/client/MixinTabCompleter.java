package uwu.smsgamer.pasteclient.injection.mixins.client;

import net.minecraft.util.TabCompleter;
import net.minecraftforge.fml.relauncher.*;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uwu.smsgamer.pasteclient.PasteClient;

@Mixin(TabCompleter.class)
@SideOnly(Side.CLIENT)
public abstract class MixinTabCompleter {
    @Shadow
    protected boolean requestedCompletions;

    @Shadow
    public abstract void setCompletions(String... p_setCompletions_1_);

    @Inject(method = "requestCompletions", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/NetHandlerPlayClient;sendPacket(Lnet/minecraft/network/Packet;)V", shift = At.Shift.BEFORE), cancellable = true)
    private void autocomplete(String cmd, @NotNull CallbackInfo ci) {
        if (cmd.startsWith(".")) {
            String[] ls = PasteClient.INSTANCE.commandManager.autoComplete(cmd).toArray(new String[0]);
            if (ls.length == 0 || cmd.toLowerCase().endsWith(ls[ls.length - 1].toLowerCase())) {
                return;
            }
            requestedCompletions = true;
            setCompletions(ls);
            ci.cancel();
        }
    }
}
