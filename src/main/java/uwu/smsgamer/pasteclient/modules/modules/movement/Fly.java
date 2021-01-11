package uwu.smsgamer.pasteclient.modules.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import net.minecraft.network.play.client.CPacketPlayer;
import uwu.smsgamer.pasteclient.events.*;
import uwu.smsgamer.pasteclient.modules.*;
import uwu.smsgamer.pasteclient.utils.*;
import uwu.smsgamer.pasteclient.values.*;

public class Fly extends PasteModule {
    private final IntChoiceValue mode = addIntChoice("Mode", "Mode for fly", 0,
      0, "Vanilla",
      1, "Float",
      2, "Advanced");// TODO: 2020-11-13 make me
    private final NumberValue speed = addDeci("Speed", "How fast you go.", 1, 0, 5, 0.01);
    private final BoolValue ground = addBool("Ground", "Sets ground client side.", false);
    private final BoolValue groundSpoof = addBool("GroundSpoof", "Spoofs your ground.", false);
    private final BoolValue groundSpoofM = addBool("GroundSpoofM", "Sets spoof ground to this.", false);

    public Fly() {
        super("Fly", "Lets you fly.", ModuleCategory.MOVEMENT);
    }

    private boolean spoofGround; // Whether to spoof ground or not
    private boolean spoofGroundM; // Spoof ground to this

    @EventTarget
    private void onMove(MotionUpdateEvent event) {
        if (!this.getState()) return;
        if (ground.getValue()) mc.player.onGround = true;
        spoofGround = false;
        switch (mode.getValue()) {
            case 0: {
                setSpoofGround();
                int v = 0;
                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    v++;
                }
                if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                    v--;
                }
                MotionUtils.vset(v * speed.getValue());
                if (MotionUtils.isMoving()) {
                    MotionUtils.strafe(speed.getValue());
                } else {
                    mc.player.motionX = 0;
                    mc.player.motionZ = 0;
                }
                break;
            }
            case 1: {
                setSpoofGround();
                mc.player.motionY = 0;
                break;
            }
            case 2: {

            }
        }
    }

    private void setSpoofGround() {
        setSpoofGround(groundSpoof.getValue(), groundSpoofM.getValue());
    }

    private void setSpoofGround(boolean spoof, boolean mode) {
        spoofGround = spoof;
        spoofGroundM = mode;
    }

    @EventTarget
    private void onPacket(PacketEvent event) {
        if (!this.getState()) return;
        if (event.getEventType().equals(EventType.SEND)) {
            if (event.getPacket() instanceof CPacketPlayer) {
                CPacketPlayer packet = (CPacketPlayer) event.getPacket();
                PacketUtils.CPacketPlayerBuilder builder = new PacketUtils.CPacketPlayerBuilder(packet);
                if (spoofGround) builder.setOnGround(spoofGroundM);
                event.setPacket(builder.build());
            }
        }
    }
}
