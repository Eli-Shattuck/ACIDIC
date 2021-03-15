package Types;

import Types.Exceptions.MyException;

public class Result<T> {
    private MyException error;
    private T val;

    public Result(T val, MyException error) {
        this.val = val;
        this.error = error;
    }

    public Result() {
        this(null, null);
    }

    public T register(Result<T> res) {
        setError(res.error);
        return res.getVal();
    }
    public T register(T res) {
        return res;
    }
    public Result<T> success(T val) {
        setVal(val);
        return this;
    }
    public Result<T> failure(MyException error) {
        setError(error);
        return this;
    }

    public MyException getError() {
        return error;
    }
    public boolean error() {
        return error != null;
    }
    public void setError(MyException error) {
        this.error = error;
    }

    public T getVal() {
        return val;
    }

    public void setVal(T val) {
        this.val = val;
    }
}

