package myRandom;

/**
 * Creat by 黄伟洪 on 2020.4.4.
 */
public class TrueFraction {
    private int numerator;//分子
    private int denominator;//分母
    private String division = "/";//分号

    public int getNumerator() {
        return numerator;
    }

    public void setNumerator(int numerator) {
        this.numerator = numerator;
    }

    public int getDenominator() {
        return denominator;
    }

    public void setDenominator(int denominator) {
        this.denominator = denominator;
    }

    @Override
    public String toString() {
        return "" + numerator + division + denominator ;
    }

}
