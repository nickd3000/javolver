package travellingsalesman;

public class City {

    public double x, y;

    public City(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distance(City c2) {
        double dx = c2.x - this.x;
        double dy = c2.y - this.y;
        return Math.sqrt((dx * dx) + (dy * dy));
    }
}
