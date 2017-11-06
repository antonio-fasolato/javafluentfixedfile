package net.fasolato.jfff;

import org.apache.commons.lang.StringUtils;

import java.util.LinkedList;
import java.util.List;

public class JfffColumn {
    private JfffTypes type;
    private String name;
    private int length;
    private boolean required;
    private boolean pad;
    private boolean padLeft;
    private char padFiller;
    private boolean trim;
    private char trimFiller;

    public JfffColumn() {
        type = JfffTypes.string;
        required = true;
        padFiller = ' ';
        trimFiller = ' ';
    }

    public void validate() throws JfffException {
        List<String> errors = new LinkedList<String>();

        if (StringUtils.isBlank(name)) {
            errors.add("Name is empty");
        }
        if (length <= 0) {
            errors.add("Length is zero or less");
        }

        if (!errors.isEmpty()) {
            JfffException e = new JfffException("Validation error");
            e.setErrors(errors);
            throw e;
        }
    }

    public Object parse(String s) throws JfffException {
        try {
            switch (type) {
                case integer:
                    if (StringUtils.isBlank(s)) {
                        return 0;
                    }
                    return Integer.parseInt(s);
                case string:
                    if(trim) {
                        return StringUtils.strip(s, "" + trimFiller);
                    } else {
                        return s;
                    }
                default:
                    return null;
            }
        } catch (Exception e) {
            throw new JfffException(e);
        }
    }

    public String toString(Object value) {
        String toReturn;

        if (padLeft) {
            toReturn = StringUtils.leftPad(value.toString(), length, padFiller);
        } else {
            toReturn = StringUtils.rightPad(value.toString(), length, padFiller);
        }

        return toReturn;
    }

    @Override
    public String toString() {
        return "{" +
                "\"type\":\"" + type + '"' +
                ", \"name\":\"" + name + '"' +
                ", \"length\":" + length +
                ", \"required\":" + required +
                ", \"pad\":" + pad +
                ", \"padLeft\":" + padLeft +
                ", \"padFiller\":\"" + padFiller + '"' +
                ", \"trim\":" + trim +
                ", \"trimFiller\":\"" + trimFiller + '"' +
                '}';
    }

    public JfffTypes getType() {
        return type;
    }

    public void setType(JfffTypes type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isPad() {
        return pad;
    }

    public void setPad(boolean pad) {
        this.pad = pad;
    }

    public boolean isPadLeft() {
        return padLeft;
    }

    public void setPadLeft(boolean padLeft) {
        this.padLeft = padLeft;
    }

    public char getPadFiller() {
        return padFiller;
    }

    public void setPadFiller(char padFiller) {
        this.padFiller = padFiller;
    }

    public boolean isTrim() {
        return trim;
    }

    public void setTrim(boolean trim) {
        this.trim = trim;
    }

    public char getTrimFiller() {
        return trimFiller;
    }

    public void setTrimFiller(char trimFiller) {
        this.trimFiller = trimFiller;
    }
}
