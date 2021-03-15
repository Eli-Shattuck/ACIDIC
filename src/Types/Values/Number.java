package Types.Values;

import Types.Exceptions.RunTimeException;
import Types.Result;

public class Number extends Value{
    private long iVal;
    private double fVal;
    private boolean isInt;

    public static final boolean INT = true;
    public static final boolean FLOAT = false;

    public Number(double val, boolean isInt) {
        if(isInt)
            this.iVal = (long)val;
        else
            this.fVal = val;
        this.isInt = isInt;
    }

    public Number(long val, boolean isInt) {
        if(isInt)
            this.iVal = val;
        else
            this.fVal = (double)val;
        this.isInt = isInt;
    }

    public static Result<Value> add(Value a, Value b) {
        if(a instanceof Number && b instanceof Number) {
            Number x = (Number)a;
            Number y = (Number)b;
            return new Result<>(
                new Number(
                    (x.isInt ? x.iVal : x.fVal) + (y.isInt ? y.iVal : y.fVal),
                    x.isInt && y.isInt
                ),
                null
            );
        }
        return null;
    }

    public static Result<Value> sub(Value a, Value b) {
        if(a instanceof Number && b instanceof Number) {
            Number x = (Number)a;
            Number y = (Number)b;
            return new Result<>(
                    new Number(
                            (x.isInt ? x.iVal : x.fVal) - (y.isInt ? y.iVal : y.fVal),
                            x.isInt && y.isInt
                    ),
                    null
            );
        }
        return null;
    }

    public static Result<Value> mul(Value a, Value b) {
        if(a instanceof Number && b instanceof Number) {
            Number x = (Number)a;
            Number y = (Number)b;
            return new Result<>(
                    new Number(
                            (x.isInt ? x.iVal : x.fVal) * (y.isInt ? y.iVal : y.fVal),
                            x.isInt && y.isInt
                    ),
                    null
            );
        }
        return null;
    }

    public static Result<Value> div(Value a, Value b) {
        if(a instanceof Number && b instanceof Number) {
            Number x = (Number)a;
            Number y = (Number)b;

            if((y.isInt ? y.iVal : y.fVal) == 0.0)
                return new Result<>(
                        null,
                        new RunTimeException("CANNOT DIVIDE BY 0")
                );

            return new Result<>(
                    new Number(
                            (x.isInt ? x.iVal : x.fVal) / (y.isInt ? y.iVal : y.fVal),
                            x.isInt && y.isInt
                    ),
                    null
            );
        }
        return null;
    }

    public static Result<Value> pow(Value a, Value b) {
        if(a instanceof Number && b instanceof Number) {
            Number x = (Number)a;
            Number y = (Number)b;

            if((y.isInt ? y.iVal : y.fVal) == 0.0 && (x.isInt ? x.iVal : x.fVal) == 0.0)
                return new Result<>(
                        null,
                        new RunTimeException("CANNOT RAISE 0 TO THE 0TH POWER")
                );

            return new Result<>(
                    new Number(
                            Math.pow((x.isInt ? x.iVal : x.fVal), (y.isInt ? y.iVal : y.fVal)),
                            x.isInt && y.isInt
                    ),
                    null
            );
        }
        return null;
    }

    public static Result<Value> invert(Value a) {
        if(a instanceof Number) {
            Number x = (Number)a;
            return new Result<>(new Number(x.isInt ? -x.iVal : -x.fVal, x.isInt), null);
        }
        return null;
    }

    @Override
    public java.lang.String toString() {
        if(isInt) return ""+iVal;
        return ""+fVal;
    }
}
