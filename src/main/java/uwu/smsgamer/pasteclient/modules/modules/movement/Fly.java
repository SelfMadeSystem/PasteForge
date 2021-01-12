package uwu.smsgamer.pasteclient.modules.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import com.google.gson.*;
import net.minecraft.network.play.client.CPacketPlayer;
import org.jetbrains.annotations.Nullable;
import uwu.smsgamer.pasteclient.events.*;
import uwu.smsgamer.pasteclient.modules.*;
import uwu.smsgamer.pasteclient.utils.*;
import uwu.smsgamer.pasteclient.values.*;

import java.util.*;

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
    public double startY; // Spoof ground to this

    @Override
    protected void onEnable() {
        if (mc.player != null) startY = mc.player.posY;
    }

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
        private ActionSettings actions;
        private ConditionSettings conditions;

        public AdvancedSettings(String name, String description) {
            super(name, description);
        }

        @Override
        public void genChildren(Value<?> parentValue) {
            parentValue.addChild(actions = new ActionSettings());
            parentValue.addChild(conditions = new ConditionSettings());
        }

        @Override
        public Value<?> genMainChild() {
            StringBuilder sb = new StringBuilder("Event");
            for (int i = 0; i <= children.size(); i++) {
                sb.append(i);
                if (!hasChild(sb.toString())) break;
                sb.setLength(sb.length() - String.valueOf(i).length());
            }
            StringValue n = new StringValue("Name", "Set the name of this event.", sb.toString());
            VoidValue val = new VoidValue(sb.toString(), "") {
                @Override
                public boolean rightClickRemove() {
                    return true;
                }

                @Override
                public String getName() {
                    return n.getValue();
                }
            };
            val.addChild(n);
            val.setParent(this);
            return val;
        }

        @Override
        public void loadFromJSON(Map.Entry<String, JsonElement> entry) {
            JsonObject obj = entry.getValue().getAsJsonObject();
            StringValue n = new StringValue("Name", "Set the name of this event.", obj.get("Name").getAsString());
            VoidValue val = new VoidValue(entry.getKey(), "") {
                @Override
                public boolean rightClickRemove() {
                    return true;
                }

                @Override
                public String getName() {
                    return n.getValue();
                }
            };
            val.addChild(n);
            val.setParent(this);
            genChildren(val);
            val.fromJSON(obj);
        }

        public void doAction(Fly fly) {
            if (conditions.isTickGood(fly)) {
                actions.doAction();
            }
        }
    }

    private static class ActionSettings extends ChildGen {

        public ActionSettings() {
            super("Action Settings", "Customize the actions of this event.");
        }

        @Override
        public void genChildren(Value<?> parentValue) {
            genChildren(parentValue, null);
        }

        public void genChildren(Value<?> parentValue, @Nullable JsonObject object) {
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
            if (object != null)
                parentValue.fromJSON(object);
        }

        @Override
        public Value<?> genMainChild() {
            StringBuilder sb = new StringBuilder("Action");
            for (int i = 0; i <= children.size(); i++) {
                sb.append(i);
                if (!hasChild(sb.toString())) break;
                sb.setLength(sb.length() - String.valueOf(i).length());
            }
            StringValue n = new StringValue("Name", "Set the name of this action.", sb.toString());
            VoidValue val = new VoidValue(sb.toString(), "") {
                @Override
                public boolean rightClickRemove() {
                    return true;
                }

                @Override
                public String getName() {
                    return n.getValue();
                }
            };
            val.setParent(this);
            val.addChild(n);
            return val;
        }

        @Override
        public void loadFromJSON(Map.Entry<String, JsonElement> entry) {
            JsonObject objects = entry.getValue().getAsJsonObject();
            StringValue n = new StringValue("Name", "Set the name of this value.", objects.get("Name").getAsString());
            VoidValue val = new VoidValue(entry.getKey(), "") {
                @Override
                public boolean rightClickRemove() {
                    return true;
                }

                @Override
                public String getName() {
                    return n.getValue();
                }
            };
            val.setParent(this);
            val.addChild(n);
            genChildren(val, objects);
        }

        public void doAction() {
            // todo
        }
    }

    private static class ConditionSettings extends ChildGen {
        private List<Cond> conds;

        public ConditionSettings() {
            super("Condition Settings", "Settings for triggering of event.");
        }

        @Override
        public void genChildren(Value<?> parentValue) {
            Cond c = new Cond();
            parentValue.addChild(c.checkTicks = new BoolValue("Check Ticks", "Checks for ticks.", true));
            parentValue.addChild(c.tickMod = new NumberValue("Ticks Modulus", "Number to do modulus of ticks.", 1, 1, 40, 1, NumberValue.NumberType.INTEGER) {
                @Override
                public boolean isVisible() {
                    return c.checkTicks.getValue();
                }
            });
            parentValue.addChild(c.tickRem = new NumberValue("Ticks Remainder", "Number to do modulus of ticks.", 1, 1, 40, 1, NumberValue.NumberType.INTEGER) {
                @Override
                public boolean isVisible() {
                    return c.checkTicks.getValue();
                }
            });
            parentValue.addChild(c.tickDel = new NumberValue("Ticks Delay", "The amount of delay from when this module starts.", 0, 0, 40, 1, NumberValue.NumberType.INTEGER) {
                @Override
                public boolean isVisible() {
                    return c.checkTicks.getValue();
                }
            });

            parentValue.addChild(c.checkXZMotion = new BoolValue("Check XZ Motion", "Checks for horizontal motion.", false));

            parentValue.addChild(c.xzMinSpeed = new NumberValue("Min XZ Speed", "Minimum horizontal speed.", 0, 0, 4, 0.01, NumberValue.NumberType.DECIMAL) {
                @Override
                public boolean isVisible() {
                    return c.checkXZMotion.getValue();
                }
            });
            parentValue.addChild(c.xzMaxSpeed = new NumberValue("Max XZ Speed", "Maximum horizontal speed.", 0, 0, 4, 0.01, NumberValue.NumberType.DECIMAL) {
                @Override
                public boolean isVisible() {
                    return c.checkXZMotion.getValue();
                }
            });

            parentValue.addChild(c.checkYMotion = new BoolValue("Check Y Motion", "Checks for vertical motion.", false));

            parentValue.addChild(c.yMinSpeed = new NumberValue("Min Y Speed", "Minimum vertical speed.", 0, -4, 4, 0.01, NumberValue.NumberType.DECIMAL) {
                @Override
                public boolean isVisible() {
                    return c.checkYMotion.getValue();
                }
            });
            parentValue.addChild(c.yMaxSpeed = new NumberValue("Max Y Speed", "Maximum vertical speed.", 0, -4, 4, 0.01, NumberValue.NumberType.DECIMAL) {
                @Override
                public boolean isVisible() {
                    return c.checkYMotion.getValue();
                }
            });

            parentValue.addChild(c.checkBelowStartY = new BoolValue("Check Below Start Y", "Checks for posY is below starting Y.", false));

            parentValue.addChild(c.belowYOffset = new NumberValue("Below Y Offset", "Offset for checking below Y.", 0, -8, 8, 0.1, NumberValue.NumberType.DECIMAL) {
                @Override
                public boolean isVisible() {
                    return c.checkBelowStartY.getValue();
                }
            });

            parentValue.addChild(c.checkAboveStartY = new BoolValue("Check Above Start Y", "Checks for posY is above starting Y.", false));

            parentValue.addChild(c.aboveYOffset = new NumberValue("Above Y Offset", "Offset for checking above Y.", 0, -8, 8, 0.1, NumberValue.NumberType.DECIMAL) {
                @Override
                public boolean isVisible() {
                    return c.checkAboveStartY.getValue();
                }
            });

            parentValue.addChild(c.checkGround = new BoolValue("Check Ground", "Checks for being on ground.", false));
            parentValue.addChild(c.checkAir = new BoolValue("Check Air", "Checks for being in air.", false));
            conds.add(c);
        }

        public void start() {
            for (Cond c : conds)
                c.ticks = -c.tickDel.getValue().intValue();
        }

        public boolean isTickGood(Fly fly) {
            boolean result = true;
            for (Cond c : conds) {
                if (c.checkGround.getValue())
                    result = mc.player.onGround;
                if (c.checkAir.getValue())
                    result &= !mc.player.onGround;
                if (c.checkXZMotion.getValue())
                    result &= MotionUtils.getSpeed() >= c.xzMinSpeed.getValue() && MotionUtils.getSpeed() <= c.xzMaxSpeed.getValue();
                if (c.checkYMotion.getValue())
                    result &= mc.player.motionY >= c.yMinSpeed.getValue() && mc.player.motionY <= c.yMaxSpeed.getValue();
                if (c.checkTicks.getValue())
                    result &= c.ticks >= 0 && (c.ticks % c.tickMod.getValue().intValue() == c.tickRem.getValue().intValue());
                if (c.checkBelowStartY.getValue())
                    result &= (fly.startY + c.belowYOffset.getValue()) < mc.player.posY;
                if (c.checkAboveStartY.getValue())
                    result &= (fly.startY + c.aboveYOffset.getValue()) > mc.player.posY;
                c.ticks++;
            }
            return result;
        }

        @Override
        public void loadFromJSON(Map.Entry<String, JsonElement> entry) {

        }

        private static class Cond {
            public int ticks;
            public BoolValue checkTicks;
            public NumberValue tickMod;
            public NumberValue tickRem;
            public NumberValue tickDel;
            public BoolValue checkXZMotion;
            public NumberValue xzMinSpeed;
            public NumberValue xzMaxSpeed;
            public BoolValue checkYMotion;
            public NumberValue yMinSpeed;
            public NumberValue yMaxSpeed;
            public BoolValue checkBelowStartY;
            public NumberValue belowYOffset;
            public BoolValue checkAboveStartY;
            public NumberValue aboveYOffset;
            public BoolValue checkGround;
            public BoolValue checkAir;
        }
    }
}
