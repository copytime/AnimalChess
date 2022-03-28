package numberPropertyExtension;

import javafx.beans.property.SimpleDoubleProperty;

public class SimpleDoublePropertyExt extends SimpleDoubleProperty {
    public SimpleDoublePropertyExt(){
        super();
    }
    public SimpleDoublePropertyExt(int var1) {
        super(var1);
    }

    public SimpleDoublePropertyExt(Object var1, String var2) {
        super(var1,var2);
    }

    public SimpleDoublePropertyExt(Object var1, String var2, int var3) {
        super(var1,var2,var3);
    }


    public void addSelf(double v) {
        this.setValue(this.add(v).get());
    }

    public void addSelf(long v) {
        this.setValue(this.add(v).get());
    }

    public void addSelf(float v) {
        this.setValue(this.add(v).get());
    }

    public void addSelf(int v) {
        this.setValue(this.add(v).get());
    }

    public void subtractSelf(double v) {
        this.setValue(this.subtract(v).get());
    }

    public void subtractSelf(long v) {
        this.setValue(this.subtract(v).get());
    }

    public void subtractSelf(float v) {
        this.setValue(this.subtract(v).get());
    }

    public void subtractSelf(int v) {
        this.setValue(this.subtract(v).get());
    }

    public void multiplySelf(double v) {
        this.setValue(this.multiply(v).get());
    }

    public void multiplySelf(long v) {
        this.setValue(this.multiply(v).get());
    }

    public void multiplySelf(float v) {
        this.setValue(this.multiply(v).get());
    }

    public void multiplySelf(int v) {
        this.setValue(this.multiply(v).get());
    }


    public void divideSelf(double v) {
        this.setValue(this.divide(v).get());
    }

    public void divideSelf(long v) {
        this.setValue(this.divide(v).get());
    }

    public void divideSelf(float v) {
        this.setValue(this.divide(v).get());
    }

    public void divideSelf(int v) {
        this.setValue(this.divide(v).get());
    }
}
