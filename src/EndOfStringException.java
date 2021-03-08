public class EndOfStringException extends Exception {
    private String msg;
    public EndOfStringException(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return msg;
    }
}
