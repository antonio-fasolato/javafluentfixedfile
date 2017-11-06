package net.fasolato.jfff;

import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Main Jfff class to work with a fixed field string
 *
 * @param <T> The POJO that represents the fields in the string
 */
public class JfffFile<T> {
    private List<JfffColumn> columns;
    private JfffColumn lastColumn;

    /**
     * empty constructor. Creates an empty JfffFile with no column configured
     */
    public JfffFile() {
        columns = new LinkedList<JfffColumn>();
    }

    @Override
    public String toString() {
        return "{" +
                "\"columns\":" + columns +
                ", \"lastColumn\":" + lastColumn +
                '}';
    }

    /**
     * Adds a new column to the JfffFile. All subsequent methods that modify a column will work on this column
     *
     * @return reference to this to permit fluent interface
     */
    public JfffFile addColumn() {
        lastColumn = new JfffColumn();
        columns.add(lastColumn);
        return this;
    }

    // = Columns type =========================================================================================

    /**
     * Defines the last column as String
     *
     * @return reference to this to permit fluent interface
     * @throws JfffException If no column is defined
     */
    public JfffFile asString() throws JfffException {
        if (lastColumn == null) {
            throw new JfffException("No column added jet");
        }

        lastColumn.setType(JfffTypes.string);
        return this;
    }

    /**
     * Defines the last column as integer
     *
     * @return reference to this to permit fluent interface
     * @throws JfffException If no column is defined
     */
    public JfffFile asInteger() throws JfffException {
        if (lastColumn == null) {
            throw new JfffException("No column added jet");
        }

        lastColumn.setType(JfffTypes.integer);
        lastColumn.setPadLeft(true);
        lastColumn.setPadFiller('0');
        return this;
    }
    // = Columns type = end ===================================================================================

    /**
     * Sets the last column name. Used when retreiving or setting the POJO values
     *
     * @param name The column name
     * @return reference to this to permit fluent interface
     * @throws JfffException If no column is defined
     */
    public JfffFile name(String name) throws JfffException {
        if (lastColumn == null) {
            throw new JfffException("No column added jet");
        }

        lastColumn.setName(name);
        return this;
    }

    /**
     * Sets the column length
     *
     * @param length The column length
     * @return reference to this to permit fluent interface
     * @throws JfffException If no column is defined
     */
    public JfffFile length(int length) throws JfffException {
        if (lastColumn == null) {
            throw new JfffException("No column added jet");
        }

        lastColumn.setLength(length);
        return this;
    }

    /**
     * Sets the column as required (the default). If a field is set as required it must be present in the String to be parsed, and it will be added (padded) to the output String
     *
     * @return reference to this to permit fluent interface
     * @throws JfffException If no column is defined or if the previous column is marked as optional. Optional columns are permitted only at the end of the file
     */
    public JfffFile required() throws JfffException {
        if (lastColumn == null) {
            throw new JfffException("No column added jet");
        }
        if (columns.size() > 1 && !columns.get(columns.size() - 1).isRequired()) {
            throw new JfffException("The previous column is optional. Optional columns are permitted only at the end of the JfffFile");
        }

        lastColumn.setRequired(true);
        return this;
    }

    /**
     * Sets the column as optional. This column will not generate an error if not found, and if the value in the POJO is null, it will not written in the generated String
     *
     * @return reference to this to permit fluent interface
     * @throws JfffException If no column is defined
     */
    public JfffFile optional() throws JfffException {
        if (lastColumn == null) {
            throw new JfffException("No column added jet");
        }

        lastColumn.setRequired(false);
        return this;
    }

    /**
     * Sets if the column must be left-padded when creating the output String.
     *
     * @param filler The character to use as a filler
     * @return reference to this to permit fluent interface
     * @throws JfffException If no column is defined
     */
    public JfffFile padLeft(char filler) throws JfffException {
        if (lastColumn == null) {
            throw new JfffException("No column added jet");
        }

        lastColumn.setPad(true);
        lastColumn.setPadLeft(true);
        lastColumn.setPadFiller(filler);
        return this;
    }

    /**
     * Sets if the column must be left-padded when creating the output String (using spaces).
     *
     * @return reference to this to permit fluent interface
     * @throws JfffException If no column is defined
     */
    public JfffFile padLeft() throws JfffException {
        return padLeft(' ');
    }

    /**
     * Sets if the column must be right-padded when creating the output String.
     *
     * @param filler The character to use as a filler
     * @return reference to this to permit fluent interface
     * @throws JfffException If no column is defined
     */
    public JfffFile padRight(char filler) throws JfffException {
        if (lastColumn == null) {
            throw new JfffException("No column added jet");
        }

        lastColumn.setPad(true);
        lastColumn.setPadLeft(false);
        lastColumn.setPadFiller(filler);
        return this;
    }

    /**
     * Sets if the column must be right-padded when creating the output String (using spaces).
     *
     * @return reference to this to permit fluent interface
     * @throws JfffException If no column is defined
     */
    public JfffFile padRight() throws JfffException {
        return padRight(' ');
    }

    /**
     * If the column value must be trimmed before being read/written
     *
     * @param filler The character to trim
     * @return reference to this to permit fluent interface
     * @throws JfffException If no column is defined
     */
    public JfffFile trim(char filler) throws JfffException {
        if (lastColumn == null) {
            throw new JfffException("No column added jet");
        }

        lastColumn.setTrim(true);
        lastColumn.setTrimFiller(filler);
        return this;
    }

    /**
     * If the column value must be trimmed before being read/written
     *
     * @return reference to this to permit fluent interface
     * @throws JfffException If no column is defined
     */
    public JfffFile trim() throws JfffException {
        return trim(' ');
    }

    /**
     * Reads all the columns defined from the input and returns the POJO with all the values
     *
     * @param input The String to parse
     * @param clazz The POJO class
     * @return A new POJO with its fields assigned with the columns read from the input
     */
    public T parse(String input, Class<T> clazz) throws JfffException {
        try {
            T obj = clazz.newInstance();

            int i = 0;
            for (JfffColumn c : columns) {
                c.validate();
                String s = StringUtils.substring(input, i, i += c.getLength());
                Object v = c.parse(s);
                PropertyUtils.setSimpleProperty(obj, c.getName(), v);
            }

            return obj;
        } catch (Exception e) {
            throw new JfffException(e);
        }
    }

    /**
     * Parses an entire string in input containig a series of rows.
     *
     * @param input The rows to parse
     * @param separator The String used as row separator
     * @param clazz The POJO class
     * @return A list of POJO containing all the values from the file
     */
    public List<T> parseAll(String input, String separator, Class<T> clazz) throws JfffException {
        String[] lines = StringUtils.split(input, separator);
        List<T> toReturn = new ArrayList<T>();
        for (String s : lines) {
            toReturn.add(parse(s, clazz));
        }
        return toReturn;
    }

    /**
     * Parses an entire string in input containig a series of rows. The OS line separator is used as row separator
     *
     * @param input The rows to parse
     * @param clazz The POJO class
     * @return A list of POJO containing all the values from the file
     */
    public List<T> parseAll(String input, Class<T> clazz) throws JfffException {
        return parseAll(input, System.lineSeparator(), clazz);
    }

    /**
     * Generates a String with the values taken from the input POJO and formatted as specified by the JfffFile columns
     *
     * @param values The POJO containig all the values
     * @return The fornmatted String
     */
    public String createString(T values) throws JfffException {
        StringBuilder sb = new StringBuilder();

        try {
            for (JfffColumn c : columns) {
                c.validate();
                sb.append(c.toString(PropertyUtils.getSimpleProperty(values, c.getName())));
            }
        } catch (Exception e) {
            throw new JfffException(e);
        }

        return sb.toString();
    }

    /**
     * Formats all the POJOs passed in input as specified by the list of columns and returns a String with all the corresponding lines
     * Warning: il the List of values is too big, the program could incurr in memory allocation issues. If you are converting a lot of objects in Strings, you should use createString in a loop and dump the String in a file or some other container.
     *
     * @param values    The list of POJOs to convert
     * @param separator The char to use as POJO separators once they are converted
     * @return The converted POJOs
     */
    public String createString(List<T> values, String separator) throws JfffException {
        StringBuilder sb = new StringBuilder("");

        int i = 0;
        for (T v : values) {
            sb.append(createString(v));
            if (i < values.size() - 1) {
                sb.append(separator);
            }
            i++;
        }

        return sb.toString();
    }

    /**
     * Formats all the POJOs passed in input as specified by the list of columns and returns a String with all the corresponding lines (using newline as separator)
     * Warning: il the List of values is too big, the program could incurr in memory allocation issues. If you are converting a lot of objects in Strings, you should use createString in a loop and dump the String in a file or some other container.
     *
     * @param values The list of POJOs to convert
     * @return The converted POJOs
     */
    public String createString(List<T> values) throws NoSuchMethodException, IllegalAccessException, JfffException, InvocationTargetException {
        return createString(values, System.lineSeparator());
    }
}
