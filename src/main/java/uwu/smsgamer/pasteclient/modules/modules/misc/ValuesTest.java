package uwu.smsgamer.pasteclient.modules.modules.misc;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import uwu.smsgamer.pasteclient.modules.*;
import uwu.smsgamer.pasteclient.utils.ChatUtils;
import uwu.smsgamer.pasteclient.values.*;

import java.awt.*;

public class ValuesTest extends Module {
    public BoolValue boolVal = addBool("BoolVal", "Boolean Value", true);
    public BoolValue boolVal1 = addBool("BoolVal1", "Boolean Value", false);
    public NumberValue deciVal = addDeci("DeciVal", "Decimal Value", 4, 0.0, 5, 0.1);
    public NumberValue intVal = addInt("IntVal", "Decimal Value", 4, 0, 5);
    public NumberValue perVal = addPer("PerVal", "Percentage Value", 0.5);
    public RangeValue rangeValInt = addRange("RangeValInt", "Range Value Integer", 2, 4, 0, 5, 1, NumberValue.NumberType.INTEGER);
    public RangeValue rangeValDeci = addRange("RangeValDeci", "Range Value Decimal", 1.2, 5.3, -2, 13, 0.05, NumberValue.NumberType.DECIMAL);
    public RangeValue rangeValPer = addRange("RangeValPer", "Range Value Percentage", 0.3, 0.5, 0, 1, 0.01, NumberValue.NumberType.PERCENT);
    public StrChoiceValue strChoiceValue = addStrChoice("StrChoice", "String Choice", "a",
      "a", "zero",
      "b", "one",
      "c", "two",
      "d", "three",
      "e", "four");
    public IntChoiceValue intChoiceValue = addIntChoice("IntChoice", "Integer Choice", 0,
      0, "zero",
      1, "one",
      2, "two",
      3, "three",
      4, "four");
    public StringValue stringValue = addStr("StrVal", "String", "Hello");
    public NumberValue hiddenValue;
    public MultiString multiString = (MultiString) addValue(new MultiString("MultiStr", "Multiple Strings UwU"));
    public PacketValue packetValue = (PacketValue) addValue(new PacketValue("Packet", "Packet Value (:", CPacketPlayer.class));
    public PacketValue cPacketValue = (PacketValue) addValue(new PacketValue("CPacket", "Client Packet Value (:", CPacketPlayer.class, PacketValue.cPacketChoices));
    public PacketValue sPacketValue = (PacketValue) addValue(new PacketValue("SPacket", "Server Packet Value (:", SPacketPlayerPosLook.class, PacketValue.sPacketChoices));
    public PositionValue positionValue = (PositionValue) addValue(new PositionValue("Position", "Position Value (:"));
    public ColorValue colorValue = (ColorValue) addValue(new ColorValue("Color", "Color Value", Color.RED));
    public FancyColorValue fancyColorValue = (FancyColorValue) addValue(new FancyColorValue("FancyColor", "Color Value", Color.BLUE));

    public ValuesTest() {
        super("ValuesTest", "Just testing values", ModuleCategory.MISC);
        Value<?> val = boolVal.addChild(genBool("AYE", "A Bool", true));
        val = val.addChild(genBool("BBB", "A Bool", true));
        val.addChild(genBool("CCC", "A Bool", true));
        boolVal.addChild(genBool("AYE1", "A Bool1", true));
        boolVal.addChild(genBool("AYE2", "A Bool2", true));
        hiddenValue = (NumberValue) addValue(new NumberValue("HiddenVal", "A hidden value UwU", 3, 1, 6, 0.01, NumberValue.NumberType.DECIMAL) {
            @Override
            public boolean isVisible() {
                return boolVal1.getValue();
            }
        });
        intVal.addChild(genBool("Cool", "Cool", false));
    }

    @Override
    protected void onEnable() {
        ChatUtils.info(boolVal.getValStr() + ":" + boolVal1.getValStr() + ":" + boolVal.getChild("aye").getValStr() +
          ":" + boolVal.getChild("aye1").getValStr() + ":" + boolVal.getChild("aye2").getValStr() +
          ":" + deciVal.getValStr() + ":" + intVal.getValStr() + ":" + perVal.getValStr());
        ChatUtils.info(strChoiceValue.getValue() + ":" + strChoiceValue.getValStr() + " : " +
          intChoiceValue.getValue() + ":" + intChoiceValue.getValStr());
        ChatUtils.info(stringValue.getValStr());
    }
}
