import edu.princeton.cs.algs4.Graph;

import java.awt.*;

/**
 * Draws an electorate once gerrymandered for purple, then (after a mouse click) gerrymandered for yellow. The
 * effectiveness of the gerrymandering algorithm (as defined in GerrymandererMeasurer) is printed to stdout.
 */
public class ElectorateDrawer {

    private static final Color PURPLE = new Color(128, 0, 128);

    private static final Color YELLOW = new Color(255, 255, 0);

    public static void main(String[] args) {
        StdDraw.enableDoubleBuffering();
        Gerrymanderer gerrymanderer = new Striper(); // Change this to create an instance of your class
        Electorate e = new Electorate(9);
        int[][] districts = gerrymanderer.gerrymander(e, true);
        if (!e.isValidMap(districts)) {
            throw new RuntimeException("Invalid districts");
        }
        draw(e, districts);
        int purple = e.getPurpleWins(districts);
        StdOut.println("Districts won by purple when gerrymandering for purple: " + purple);
        while (!StdDraw.isMousePressed()) {
            // Wait for user to click
        }
        districts = gerrymanderer.gerrymander(e, false);
        if (!e.isValidMap(districts)) {
            throw new RuntimeException("Invalid districts");
        }
        draw(e, districts);
        int yellow = e.getPurpleWins(districts);
        StdOut.println("Districts won by purple when gerrymandering for yellow: " + yellow);
        StdOut.println("Effectiveness of gerrymandering algorithm: " + (purple - yellow) / (districts.length * 1.0));
    }

    /**
     * Draws e, partitioned into the indicated districts. Each voter's party is indicated by a circle. The winner of
     * each district is indicated by a square behind each voter. Voters within a district are connected by black lines.
     */
    public static void draw(Electorate electorate, int[][] districts) {
        StdDraw.clear();
        boolean[] voters = electorate.getVoters();
        int v = voters.length;
        int d = electorate.getNumberOfDistricts();
        StdDraw.setScale(-0.5, d - 0.5);
        // Draw district winner boxes
        for (int[] district : districts) {
            boolean winner = electorate.winner(district);
            for (int i : district) {
                int x = i / d;
                int y = i % d;
                if (winner) {
                    StdDraw.setPenColor(PURPLE);
                } else {
                    StdDraw.setPenColor(YELLOW);
                }
                StdDraw.filledSquare(x, y, 0.35);
            }
        }
        // Draw lines
        StdDraw.setPenColor(Color.BLACK);
        Graph g = electorate.graphWithOnlyWithinDistrictLines(districts);
        for (int i = 0; i < v; i++) {
            int x1 = i / d;
            int y1 = i % d;
            for (int j : g.adj(i)) {
                if (i < j) {
                    int x2 = j / d;
                    int y2 = j % d;
                    StdDraw.line(x1, y1, x2, y2);
                }
            }
        }
        // Draw dots
        for (int i = 0; i < v; i++) {
            int x = i / d;
            int y = i % d;
            if (voters[i]) {
                StdDraw.setPenColor(PURPLE);
            } else {
                StdDraw.setPenColor(YELLOW);
            }
            StdDraw.filledCircle(x, y, 0.25);
            StdDraw.setPenColor(Color.BLACK);
            StdDraw.circle(x, y, 0.25);
        }
        StdDraw.show();
    }

}
