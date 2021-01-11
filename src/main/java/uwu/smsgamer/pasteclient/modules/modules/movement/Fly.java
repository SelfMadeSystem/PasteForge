package uwu.smsgamer.pasteclient.modules.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import com.google.gson.JsonElement;
import net.minecraft.network.play.client.CPacketPlayer;
import uwu.smsgamer.pasteclient.events.*;
import uwu.smsgamer.pasteclient.modules.*;
import uwu.smsgamer.pasteclient.utils.*;
import uwu.smsgamer.pasteclient.values.*;

import java.util.Map;

public class Fly extends PasteModule {
    private final IntChoiceValue mode = addIntChoice("Mode", "Mode for fly", 0,
      0, "Vanilla",
      1, "Float",
      2, "Advanced");// TODO: 2020-11-13 make me
    private final NumberValue speed = addDeci("Speed", "How fast you go.", 1, 0, 5, 0.01);
    private final BoolValue ground = addBool("Ground", "Sets ground client side.", false);
    private final BoolValue groundSpoof = addBool("GroundSpoof", "Spoofs your ground.", false);
    private final BoolValue groundSpoofM = addBool("GroundSpoofM", "Sets spoof ground to this.", false);
    private final AdvancedSettings advanced = (AdvancedSettings) addValue(new AdvancedSettings("Advanced settings", "idk todo: this")); // todo: this

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

    private static class AdvancedSettings extends ChildGen {

        public AdvancedSettings(String name, String description) {
            super(name, description);
        }

        @Override
        public void genChildren(Value<?> parentValue) {
            parentValue.addChild(new ActionSettings());
        }

        @Override
        public Value<?> genMainChild() {
            StringBuilder sb = new StringBuilder("Event");
            for (int i = 0; i <= children.size(); i++) {
                sb.append(i);
                if (!hasChild(sb.toString())) break;
                sb.setLength(sb.length() - String.valueOf(i).length());
            }
            VoidValue val = new VoidValue(sb.toString(), "") {
                @Override
                public boolean rightClickRemove() {
                    return true;
                }
            };
            val.setParent(this);
            return val;
        }

        @Override
        public void loadFromJSON(Map.Entry<String, JsonElement> entry) {
            // TODO
        }
    }

    private static class ActionSettings extends ChildGen {

        public ActionSettings() {
            super("Action Settings", "Customize the actions of this event.");
        }

        @Override
        public void genChildren(Value<?> parentValue) {
            BoolValue v;
            parentValue.addChild(v = new BoolValue("Packet", "Send packet to position.", false));
            parentValue.addChild(new BoolValue("ToFloor", "Teleports to floor.", false) {
                @Override
                public String getDescription() {
                    return v.getValue() ? "Sends packet to floor." : "Teleports to floor.";
                }
            });
            parentValue.addChild(new PositionValue("Position", "How to set the position.", false));
            parentValue.addChild(new RangeValue("Timer",
              "To set the timer to (random between two points). Set to 0 to have no effect.",
              1, 1, 0, 10, 0.05, NumberValue.NumberType.DECIMAL));
            parentValue.addChild(new IntChoiceValue("MotionXZType", "The type of motion to set for XZ.", 0,
              new StringHashMap<>(
                0, "Set",
                1, "Add",
                2, "Times"
              )));
            parentValue.addChild(new IntChoiceValue("MotionYType", "The type of motion to set for Y.", 0,
              new StringHashMap<>(
                0, "Set",
                1, "Add",
                2, "Times"
              )));
            parentValue.addChild(new PositionValue("Motion", "The motion to set.", false));
        }

        @Override
        public Value<?> genMainChild() {
            StringBuilder sb = new StringBuilder("Action");
            for (int i = 0; i <= children.size(); i++) {
                sb.append(i);
                if (!hasChild(sb.toString())) break;
                sb.setLength(sb.length() - String.valueOf(i).length());
            }
            VoidValue val = new VoidValue(sb.toString(), "") {
                @Override
                public boolean rightClickRemove() {
                    return true;
                }
            };
            val.setParent(this);
            return val;
        }

        @Override
        public void loadFromJSON(Map.Entry<String, JsonElement> entry) {
            // TODO
        }
    }
}
