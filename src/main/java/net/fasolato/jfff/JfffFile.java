package net.fasolato.jfff;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class JfffFile<T> {
    List<JfffColumn> columns;
    JfffColumn lastColumn;

    public JfffFile() {
        columns = new LinkedList<JfffColumn>();
    }

    public JfffFile addColumn() {
        lastColumn = new JfffColumn();
        columns.add(lastColumn);
        return this;
    }

    // = Columns type =========================================================================================
    public JfffFile asString() throws JfffException {
        if (lastColumn == null) {
            throw new JfffException("No column added jet");
        }

        lastColumn.setType(JfffTypes.string);
        return this;
    }

    public JfffFile asInteger() throws JfffException {
        if (lastColumn == null) {
            throw new JfffException("No column added jet");
        }

        lastColumn.setType(JfffTypes.integer);
        return this;
    }
    // = Columns type = end ===================================================================================

    public JfffFile name(String name) throws JfffException {
        if (lastColumn == null) {
            throw new JfffException("No column added jet");
        }

        lastColumn.setName(name);
        return this;
    }

    public JfffFile length(long length) throws JfffException {
        if (lastColumn == null) {
            throw new JfffException("No column added jet");
        }

        lastColumn.setLength(length);
        return this;
    }

    public JfffFile required() throws JfffException {
        if (lastColumn == null) {
            throw new JfffException("No column added jet");
        }

        lastColumn.setRequired(true);
        return this;
    }

    public JfffFile optional() throws JfffException {
        if (lastColumn == null) {
            throw new JfffException("No column added jet");
        }

        lastColumn.setRequired(false);
        return this;
    }

    public JfffFile padLeft(char filler) throws JfffException {
        if (lastColumn == null) {
            throw new JfffException("No column added jet");
        }

        lastColumn.setPad(true);
        lastColumn.setPadLeft(true);
        lastColumn.setPadFiller(filler);
        return this;
    }

    public JfffFile padLeft() throws JfffException {
        return padLeft(' ');
    }

    public JfffFile padRight(char filler) throws JfffException {
        if (lastColumn == null) {
            throw new JfffException("No column added jet");
        }

        lastColumn.setPad(true);
        lastColumn.setPadLeft(false);
        lastColumn.setPadFiller(filler);
        return this;
    }

    public JfffFile padRight() throws JfffException {
        return padRight(' ');
    }

    public T parse(String input) {
        throw new UnsupportedOperationException();
    }

    public List<T> parseAll(File f) {
        throw new UnsupportedOperationException();
    }

    public String createString(T values) {
        throw new UnsupportedOperationException();
    }

    public String createString(List<T> values, String separator) {
        throw new UnsupportedOperationException();
    }

    public String createString(List<T> values) {
        return createString(values, System.lineSeparator());
    }
}
