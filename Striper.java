/**
 * Baseline Gerrymanderer that divides the electorate into vertical stripes (when gerrymandering for the purple party)
 * or horizontal stripes (when gerrymandering for the yellow party).
 */
public class Striper implements Gerrymanderer {

    @Override
    public int[][] gerrymander(Electorate e, boolean party) {
        int d = e.getNumberOfDistricts();
        int[][] result = new int[d][d];
        int i = 0;
        for (int x = 0; x < d; x++) {
            for (int y = 0; y < d; y++) {
                if (party) {
                    result[x][y] = i;
                } else {
                    result[y][x] = i;
                }
                i++;
            }
        }
        return result;
    }

}
