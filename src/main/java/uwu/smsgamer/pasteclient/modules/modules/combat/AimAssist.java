package uwu.smsgamer.pasteclient.modules.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.Entity;
import uwu.smsgamer.pasteclient.events.MouseMoveEvent;
import uwu.smsgamer.pasteclient.injection.interfaces.IMixinMouseHelper;
import uwu.smsgamer.pasteclient.modules.*;
import uwu.smsgamer.pasteclient.utils.*;
import uwu.smsgamer.pasteclient.values.*;

public class AimAssist extends Module {
    public IntChoiceValue targetOrder = addIntChoice("TargetOrder      ->", "Which entities to target first.", 0,
      0, "Closest",
      1, "Lowest Health",
      2, "Least Angle");
    public RangeValue yawSpeed = addRange("YawSpeed", "Speed of the yaw.", 1, 1.3, 0.5, 5, 0.01, NumberValue.NumberType.PERCENT);
    public RangeValue pitchSpeed = addRange("PitchSpeed", "Speed of the pitch.", 1, 1.3, 0.5, 5, 0.01, NumberValue.NumberType.PERCENT);

    public RangeValue yawSlow = addRange("YawSlow", "Slow of the yaw.", 0.7, 1, 0.1, 2, 0.01, NumberValue.NumberType.PERCENT);
    public RangeValue pitchSlow = addRange("PitchSlow", "Slow of the pitch.", 0.7, 1, 0.1, 2, 0.01, NumberValue.NumberType.PERCENT);

    public NumberValue maxRange;
    public NumberValue maxAngle;

    public AimAssist() {
        super("AimAssist", "Helps you aims at entities", ModuleCategory.COMBAT);
        targetOrder.addChild(maxRange = genDeci("MaxRange", "Maximum distance the entity has to be in blocks.", 6, 2, 16, 0.5));
        targetOrder.addChild(maxAngle = genInt("MaxAngle", "Maximum angle the entity has to be in degrees.", 180, 0, 180));
    }

    @EventTarget
    private void onRender(MouseMoveEvent event) {
        if (!getState()) return;
        Entity target = null;
        double range = this.maxRange.getValue();
        double angle = this.maxAngle.getValue();
        switch (targetOrder.getValue()) {
            case 0:
                target = TargetUtil.getClosestEntity(range, angle);
                break;
            case 1:
                target = TargetUtil.getLowestHealthEntity(range, angle);
                break;
            case 2:
                target = TargetUtil.getLowestAngleEntity(range, angle);
                break;
        }
        if (target != null) {
            //if (mark.getValue()) render(target);
            RotationUtil util = new RotationUtil(target);

            Rotation rotation = util.getClosestRotation();
            Rotation angleDiff = RotationUtil.rotationDiff(rotation, Rotation.player());
            IMixinMouseHelper mh = (IMixinMouseHelper) mc.mouseHelper;
            mh.setMode(3);
            if (Math.abs(angleDiff.yaw) > 0.1)
                mh.setSideX(angleDiff.yaw > 0 != mc.gameSettings.invertMouse ? 1 : -1);
            else mh.setSideX(0);
            if (Math.abs(angleDiff.pitch) > 0.1)
                mh.setSideY(angleDiff.pitch < 0 ? 1 : -1);
            else mh.setSideY(0);
            double ym = yawSpeed.getRandomValue();
            double pm = pitchSpeed.getRandomValue();
            double yd = yawSlow.getRandomValue();
            double pd = pitchSlow.getRandomValue();
            mh.setMultX(ym);
            mh.setMultY(pm);
            mh.setDivX(1 / yd);
            mh.setDivY(1 / pd);
        } else ((IMixinMouseHelper) mc.mouseHelper).reset();
    }

    @Override
    protected void onDisable() {
        ((IMixinMouseHelper) mc.mouseHelper).reset();
    }
}
