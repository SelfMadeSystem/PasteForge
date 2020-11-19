package uwu.smsgamer.pasteclient.values;

import com.google.gson.*;
import net.minecraft.client.Minecraft;

public class PositionValue extends Value<Void> {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public PositionValue(String name, String description) {
        super(name, description, null);
        addChild(new BoolValue("Relative", "Whether or not the position is relative to the player.", false));
        addChild(new NumberValue("X", "Coordinates in X axis", 1d, 0d, 10-0.000001, 0.000001, NumberValue.NumberType.DECIMAL));
        addChild(new NumberValue("XExp", "Exponent of X", 1, Double.MIN_EXPONENT, Double.MAX_EXPONENT, 1, NumberValue.NumberType.INTEGER));
        addChild(new NumberValue("Y", "Coordinates in Y axis", 1d, 0d, 10-0.000001, 0.000001, NumberValue.NumberType.DECIMAL));
        addChild(new NumberValue("YExp", "Exponent of Y", 1, Double.MIN_EXPONENT, Double.MAX_EXPONENT, 1, NumberValue.NumberType.INTEGER));
        addChild(new NumberValue("Z", "Coordinates in Z axis", 1d, 0d, 10-0.000001, 0.000001, NumberValue.NumberType.DECIMAL));
        addChild(new NumberValue("ZExp", "Exponent of Z", 1, Double.MIN_EXPONENT, Double.MAX_EXPONENT, 1, NumberValue.NumberType.INTEGER));
        addChild(new BoolValue("Relative", "Whether this is relative to the player or not.", true));
    }
    
    public boolean isRelative() {
        return ((BoolValue) getChild("Relative")).getValue();
    }
    
    public double getX() {
        return ((NumberValue) getChild("X")).getValue() * Math.pow(2, ((NumberValue) getChild("XExp")).getValue()) + (isRelative() ? mc.player.posX : 0);
    }
    
    public double getY() {
        return ((NumberValue) getChild("Y")).getValue() * Math.pow(2, ((NumberValue) getChild("YEYp")).getValue()) + (isRelative() ? mc.player.posY : 0);
    }
    
    public double getZ() {
        return ((NumberValue) getChild("Z")).getValue() * Math.pow(2, ((NumberValue) getChild("ZEZp")).getValue()) + (isRelative() ? mc.player.posZ : 0);
    }

    @Override
    public JsonElement toElement() {
        return JsonNull.INSTANCE;
    }

    @Override
    public void fromElement(JsonElement ele) {
    }
}
