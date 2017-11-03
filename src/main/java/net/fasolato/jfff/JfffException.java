package net.fasolato.jfff;

public class JfffException extends Exception {
    public JfffException() {
        super();
    }

    public JfffException(String s) {
        super(s);
    }

    public JfffException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public JfffException(Throwable throwable) {
        super(throwable);
    }
}
