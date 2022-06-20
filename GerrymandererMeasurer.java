/**
 * Measures the effectiveness of a gerrymandering algorithm. Prints a single number between -1.0 and 1.0,
 * where 1.0 means the algorithm consistently wins every single district and -1.0 means that it consistently
 * loses every single district.
 */
public class GerrymandererMeasurer {

    public static void main(String[] args) {
        Gerrymanderer gerrymanderer = new Striper(); // Change this to create an instance of your class
        StdOut.println(measure(9, gerrymanderer, 100));
    }

    /**
     * Returns the average measurement over n random d * d electorates, each to be divided into d districts of d
     * voters.
     */
    public static double measure(int d, Gerrymanderer gerrymanderer, int n) {
        double sum = 0.0;
        for (int i = 0; i < n; i++) {
            Electorate e = new Electorate(d);
            int[][] districts = gerrymanderer.gerrymander(e, true);
            if (!e.isValidMap(districts)) {
                throw new RuntimeException("Invalid districts");
            }
            int purple = e.getPurpleWins(districts);
            districts = gerrymanderer.gerrymander(e, false);
            if (!e.isValidMap(districts)) {
                throw new RuntimeException("Invalid districts");
            }
            int yellow = e.getPurpleWins(districts);
            sum += (purple - yellow) / (d * 1.0);
        }
        return sum / n;
    }

}
