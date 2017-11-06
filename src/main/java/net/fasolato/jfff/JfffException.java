package net.fasolato.jfff;

import java.util.LinkedList;
import java.util.List;

public class JfffException extends Exception {
    private List<String> errors = new LinkedList<String>();

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

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
