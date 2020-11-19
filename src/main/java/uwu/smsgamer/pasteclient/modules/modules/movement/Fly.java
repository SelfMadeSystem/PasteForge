package uwu.smsgamer.pasteclient.modules.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import net.minecraft.network.play.client.CPacketPlayer;
import uwu.smsgamer.pasteclient.events.*;
import uwu.smsgamer.pasteclient.modules.*;
import uwu.smsgamer.pasteclient.utils.MotionUtils;
import uwu.smsgamer.pasteclient.values.*;

import java.lang.reflect.Field;

public class Fly extends Module {
    private final IntChoiceValue mode = addIntChoice("Mode", "Mode for fly", 0,
      0, "Vanilla",
      1, "Float",
      2, "Advanced");// TODO: 2020-11-13 make me
    private final NumberValue speed = addDeci("Speed", "How fast you go.", 1, 0, 5, 0.01);
    private final BoolValue ground = addBool("Ground", "Spoof ground client side.", false);
    private final BoolValue groundSpoof = addBool("GroundSpoof", "Spoofs your ground.", false);
    private final BoolValue groundNotSpoof = addBool("GroundNotSpoof", "Spoofs so you're never on ground.", false);

    public Fly() {
        super("Fly", "Lets you fly.", ModuleCategory.MOVEMENT);
    }

    @EventTarget
    private void onMove(MotionUpdateEvent event) {
        if (!this.getState()) return;
        if (ground.getValue()) mc.player.onGround = true;
        switch (mode.getValue()) {
            case 0: {
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
                mc.player.motionY = 0;
                break;
            }
            case 2: {

            }
        }
    }

    @EventTarget
    private void onPacket(PacketEvent event) {
        if (event.getEventType().equals(EventType.SEND)) {
            if (event.getPacket() instanceof CPacketPlayer) {
                CPacketPlayer packet = (CPacketPlayer) event.getPacket();
                try {
                    Field onGround = packet.getClass().getDeclaredField(name);
                    onGround.setAccessible(true);
                    if (groundSpoof.getValue()) {
                        onGround.set(packet, true);
                    } else if (groundNotSpoof.getValue()) {
                        onGround.set(packet, false);
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }
}
