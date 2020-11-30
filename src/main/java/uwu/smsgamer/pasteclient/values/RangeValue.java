package uwu.smsgamer.pasteclient.values;

import com.google.gson.*;
import uwu.smsgamer.pasteclient.utils.*;

public class RangeValue extends Value<Pair<Double, Double>> { // FIXME: 2020-11-30 FIX ME!!!!!!!!!!!!! I'm broken!!!!!!!
    protected Double min;
    protected Double max;
    protected Double step;
    protected NumberValue.NumberType type;

    public RangeValue(String name, String description,
                      Number dMin, Number dMax, Number min, Number max, Number step, NumberValue.NumberType type, Value<?>... children) {
        super(name, description, new Pair<>(dMin.doubleValue(), dMax.doubleValue()), children);
        this.min = min.doubleValue();
        this.max = max.doubleValue();
        this.step = step.doubleValue();
        this.type = type;
    }

    public Double getMin() {
        return min;
    }

    public Double getMax() {
        return max;
    }

    public Double getStep() {
        return step;
    }

    public NumberValue.NumberType getType() {
        return type;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setValue(Object val) {
        if (val instanceof Pair &&
          ((Pair<?, ?>) val).a instanceof Number &&
          ((Pair<?, ?>) val).b instanceof Number) {
            this.value.a = ((Pair<Number, Number>) val).a.doubleValue();
            this.value.b = ((Pair<Number, Number>) val).b.doubleValue();
        } else {
            new IllegalArgumentException("Invalid argument: " + val).printStackTrace();
        }
    }

    public void setMin(Double val) {
        this.value.a = MathUtil.roundInc(val, step);
    }

    public void setMax(Double val) {
        this.value.b = MathUtil.roundInc(val, step);
    }

    public double getSizeScaled() {
        return (Math.abs(value.a - value.b) - min) / (max - min);
    }

    public double getMinScaled() {
        return (value.a - min) / (max - min);
    }

    public void setMinScaled(Double val) {
        this.value.a = MathUtil.roundInc(((val * (max - min)) + min), step);
    }

    public double getMaxScaled() {
        return (value.b - min) / (max - min);
    }

    public void setMaxScaled(Double val) {
        this.value.b = MathUtil.roundInc(((val * (max - min)) + min), step);
    }

    public void setClosestScaled(double val) {
        if (Math.abs(getMinScaled() - val) <= Math.abs(getMaxScaled() - val))
            value.a = MathUtil.roundInc(((val * (max - min)) + min), step);
        else value.b = MathUtil.roundInc(((val * (max - min)) + min), step);
        if (value.a > value.b) { // Shouldn't be necessary.
            double c = value.b;
            value.a = value.b;
            value.b = c;
        }
    }

    public double getRandomValue() {
        return Math.random() * Math.abs(this.value.a - this.value.b) + Math.min(this.value.a, this.value.b);
    }

    @Override
    public String getValStr() {
        return type.formatter.apply(value.a) + ":" + type.formatter.apply(value.b);
    }

    @Override
    public JsonElement toElement() {
        JsonObject object = new JsonObject();
        object.add("a", new JsonPrimitive(value.a));
        object.add("b", new JsonPrimitive(value.b));
        return object;
    }

    @Override
    public void fromElement(JsonElement ele) {
        JsonObject object = (JsonObject) ele;
        value.a = object.get("a").getAsDouble();
        value.b = object.get("b").getAsDouble();
    }
}
