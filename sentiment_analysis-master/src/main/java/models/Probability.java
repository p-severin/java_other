package models;

public class Probability {

    private final double neg;

    private final double neutral;

    private final double pos;

    public Probability(double neg, double neutral, double pos) {
        this.neg = neg;
        this.neutral = neutral;
        this.pos = pos;
    }

    public double getNeg() {
        return neg;
    }

    public double getNeutral() {
        return neutral;
    }

    public double getPos() {
        return pos;
    }
}
