package Types.Exceptions;

public class MyException extends Exception {
    private String msg;
    public MyException(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ": " + msg;
    }
}
