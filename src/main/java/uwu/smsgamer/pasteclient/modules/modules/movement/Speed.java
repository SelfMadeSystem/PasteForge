package uwu.smsgamer.pasteclient.modules.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import uwu.smsgamer.pasteclient.events.MotionUpdateEvent;
import uwu.smsgamer.pasteclient.modules.*;
import uwu.smsgamer.pasteclient.utils.MotionUtils;
import uwu.smsgamer.pasteclient.values.*;

public class Speed extends Module {
    public static Speed instance;

    private final IntChoiceValue mode = addIntChoice("Mode", "Mode for speed", 0,
      0, "Vanilla",
      1, "Packet", // TODO: 2020-11-13 make me
      2, "LowHop");
    private final NumberValue speed = addDeci("Speed", "How fast you go.", 1, 0, 2, 0.01);
    private final NumberValue yInit = (NumberValue) addValue(new NumberValue("JumpHeight", "How high you go for initial jump.",
      1, 0, 2, 0.01, NumberValue.NumberType.PERCENT) {
        @Override
        public boolean isVisible() {
            return mode.getValue() == 2;
        }
    });
    private final NumberValue yMinus = (NumberValue) addValue(new NumberValue("YMinus", "How fast you go down.",
      1, 0, 2, 0.01, NumberValue.NumberType.DECIMAL) {
        @Override
        public boolean isVisible() {
            return mode.getValue() == 2;
        }
    });

    public Speed() {
        super("Speed", "Lets you move faster than legit.", ModuleCategory.MOVEMENT);
        instance = this;
    }

    @EventTarget
    private void onMove(MotionUpdateEvent event) {
        if (!getState()) return;
        if (event.getEventType().equals(EventType.PRE))
        if (mc.player.isSprinting()) {
            switch (mode.getValue()) {
                case 0: {
                    if (MotionUtils.isMoving()) {
                        MotionUtils.strafe(speed.getValue());
                    } else {
                        mc.player.motionX = 0;
                        mc.player.motionZ = 0;
                    }
                    break;
                }
                case 2: {
                    if (mc.player.onGround) {
                        mc.player.jump();
                        mc.player.motionY *= yInit.getValue();
                        mc.player.motionX *= speed.getValue();
                        mc.player.motionZ *= speed.getValue();
                    } else {
                        mc.player.motionY -= yMinus.getValue() / 100F;
                    }
                }
            }
        }
    }
}
