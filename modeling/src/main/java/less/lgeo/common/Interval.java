package less.lgeo.common;

public record Interval(double min, double max) {

    private static final Interval empty = new Interval(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
    private static final Interval world = new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

    public static Interval of(double min, double max) {
        return new Interval(min, max);
    }

    public double size() {
        return max - min;
    }

    public boolean contains(double x) {
        return min <= x && x <= max;
    }

    public boolean surrounds(double x) {
        return min < x && x < max;
    }

    public double clamp(double x) {
        return x < min ? min : Math.min(x, max);
    }
}
