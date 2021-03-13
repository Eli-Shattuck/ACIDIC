package Types;

public class IllegalCharException extends Exception{
    private String msg;
    public IllegalCharException(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return msg;
    }
}
