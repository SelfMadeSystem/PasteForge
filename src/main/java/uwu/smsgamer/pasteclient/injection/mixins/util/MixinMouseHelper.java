package uwu.smsgamer.pasteclient.injection.mixins.util;

import net.minecraft.util.MouseHelper;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.*;
import uwu.smsgamer.pasteclient.injection.interfaces.IMixinMouseHelper;

@Mixin(MouseHelper.class)
public class MixinMouseHelper implements IMixinMouseHelper {
    @Shadow public int deltaX, deltaY;
    public int mode;
    public boolean sideX, sideY;
    public int x, y;
    public int addX, addY;
    public double multX, divX, multY, divY;

    /**
     * @author Sms_Gamer_3808
     */
    @Overwrite
    public void mouseXYChange() {
        int dX = Mouse.getDX();
        int dY = Mouse.getDY();
        switch (mode) {
            case 0:
                deltaX = dX;
                deltaY = dY;
                break;
            case 1:
                deltaX = x;
                deltaY = y;
                break;
            case 2:
                deltaX = dX + addX;
                deltaY = dY + addY;
                break;
            case 3:
                if ((dX > 0 && sideX) || (dX < 0 && !sideX)) dX *= multX;
                else dX /= divX;
                if ((dY > 0 && sideY) || (dY < 0 && !sideY)) dY *= multY;
                else dY /= divY;
                deltaX = dX;
                deltaY = dY;
        }
    }

    @Override
    public void setMode(int mode) {
        this.mode = mode;
    }

    @Override
    public void reset() {
        this.multX = divX = multY = divY = mode = x = y = addX = addY = 0;
    }

    @Override
    public void setSideX(boolean plus) {
        this.sideX = plus;
    }

    @Override
    public void setSideY(boolean plus) {
        this.sideY = plus;
    }

    @Override
    public void setX(int amount) {
        x = amount;
    }

    @Override
    public void setY(int amount) {
        y = amount;
    }

    @Override
    public void setAddX(int amount) {
        addX = amount;
    }

    @Override
    public void setAddY(int amount) {
        addY = amount;
    }

    @Override
    public void setMultX(double amount) {
        multX = amount;
    }

    @Override
    public void setDivX(double amount) {
        divX = amount;
    }

    @Override
    public void setMultY(double amount) {
        multY = amount;
    }

    @Override
    public void setDivY(double amount) {
        divY = amount;
    }
}
