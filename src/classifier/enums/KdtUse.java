package classifier.enums;

public enum KdtUse {
    True, False;

    public boolean shouldUse() {
        return this.equals(True);
    }
}
