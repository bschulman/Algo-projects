import edu.princeton.cs.algs4.CC;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * Electorate representing a square grid of voters each randomly allied to one of two parties: purple (true) and yellow
 * (false). The number of voters is the square of the number of districts. The voters are numbered 0 through v - 1,
 * working up a series of vertical stripes from left to right; therefore voter 0 is at the lower left, voter 1 is above
 * that, and so on, with voter v - 1 at the upper right.
 */
public class Electorate {

    /** The number of districts to be created in this Electorate. */
    private final int districts;

    /**
     * The graph indicating which voters are adjacent to which other voters. This information could be derived from
     * the number of voters, but this representation us useful for running various graph algorithms.
     */
    private final Graph graph;

    /** Party allegiances of the voters. */
    private final boolean[] voters;

    /** Creates a new Electorate of d * d voters, to be divided into d districts. */
    public Electorate(int d) {
        if (d % 2 != 1) {
            throw new RuntimeException("Number of districts must be odd");
        }
        this.districts = d;
        int v = d * d; // # of voters
        graph = new Graph(v);
        voters = new boolean[v];
        for (int i = 0; i < v; i++) {
            int x = i / d;
            int y = i % d;
            if (x < d - 1) {
                // Add neighbor to east
                graph.addEdge(i, (x + 1) * d + y);
            }
            if (y < d - 1) {
                // Add neighbor to north
                graph.addEdge(i, x * d + y + 1);
            }
            // Assign each voter to a random party
            voters[i] = StdRandom.bernoulli();
        }
    }

    /** Returns a multiline String showing voter parties: purple (#) or yellow (.). */
    public String toString() {
        String result = "";
        for (int i = 0; i < voters.length; i++) {
            result += voters[i] ? '#' : '.';
            if (i % districts == (districts - 1)) {
                result += '\n';
            }
        }
        return result;
    }

    /** Returns true if i and j appear in the same row of matrix. */
    private boolean inSameRow(int i, int j, int[][] matrix) {
        for (int a = 0; a < matrix.length; a++) {
            for (int b = 0; b < matrix[a].length; b++) {
                if (matrix[a][b] == i) {
                    // Found i; look for j in the same row
                    for (int c = 0; c < matrix[a].length; c++) {
                        if (matrix[a][c] == j) {
                            return true;
                        }
                    }
                    return false;
                }
            }
        }
        return false; // i was never found
    }

    /** Returns a copy of the Graph of the voters in the district. */
    public Graph getGraph() {
        // Make a copy so that nobody else can alter the graph. */
        return new Graph(graph);
    }

    /** Returns a subgraph of the electorate graph, but only including edges within the specified districts. */
    public Graph graphWithOnlyWithinDistrictLines(int[][] districts) {
        int v = graph.V();
        Graph result = new Graph(v);
        for (int i = 0; i < v; i++) {
            for (int j : graph.adj(i)) {
                if (i < j && inSameRow(i, j, districts)) {
                    result.addEdge(i, j);
                }
            }
        }
        return result;
    }

    /**
     * Returns true if districts legitimately divides this d * d Electorate into d contiguous districts of d
     * voters each, allocating each voter to exactly one district.
     */
    public boolean isValidMap(int[][] districts) {
        int v = graph.V();
        boolean[] counted = new boolean[v];
        int sum = 0;
        for (int[] district : districts) {
            if (district.length != v / this.districts) {
                return false; // District not correct size
            }
            for (int voter : district) {
                if (voter < 0 || voter >= v) {
                    return false; // Invalid voter
                }
                if (counted[voter]) {
                    return false; // Voter already assigned to another district
                }
                counted[voter] = true;
                sum++;
            }
        }
        if (sum != v) {
            return false; // Not enough voters counted
        }
        Graph g = graphWithOnlyWithinDistrictLines(districts);
        CC components = new CC(g); // Connected components of g
        if (components.count() != this.districts) {
            return false; // Wrong number of districts or noncontiguous districts
        }
        return true;
    }

    /** Returns a copy of the array indicate the party of each voter. */
    public boolean[] getVoters() {
        return Arrays.copyOf(voters, voters.length);
    }

    /** Returns the number of districts in this Electorate. */
    public int getNumberOfDistricts() {
        return districts;
    }

    /** Returns the number of the indicated districts that were won by te purple (true) party. */
    public int getPurpleWins(int[][] districts) {
        int wins = 0;
        for (int[] district : districts) {
            if (winner(district)) {
                wins++;
            }
        }
        return wins;
    }

    /** Returns the winner of the indicated district: true (purple) or false (yellow). */
    public boolean winner(int[] district) {
        int count = 0;
        for (int v : district) {
            if (voters[v]) {
                count++;
            }
        }
        return count > this.districts / 2;
    }

}
