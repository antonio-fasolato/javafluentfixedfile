package net.fasolato.jfff;

public class JfffColumn {
    private JfffTypes type;
    private String name;
    private long length;
    private boolean required;
    private boolean pad;
    private boolean padLeft;
    private char padFiller;
    private boolean trim;
    private char trimFiller;

    public JfffColumn() {
        type = JfffTypes.string;
        required = true;
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

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
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
