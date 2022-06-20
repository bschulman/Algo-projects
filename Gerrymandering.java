// BY BEN SCHULMAN, ZACH DWORKIN, AND ALEX LOTERO

import java.util.Arrays;

public class Gerrymandering implements Gerrymanderer {
    private int[][] result; //array used to keep track of the current board state
    private int[][][] results; //array used to keep track of previous board states in the case that a previous board state was better than the new one
    private Electorate elect; //current instance of electorate.java
    private int d; //number of districts
    private boolean par; //current party that gerrymandering is being drawn for

    public int[][] gerrymander(Electorate e, boolean party) {
        d = e.getNumberOfDistricts();
        this.result = new int[d][d];
        results = new int[1000][d][d];
        this.par=party;
        this.elect = e;
        int i = 0;
        for (int x = 0; x < d; x++) {
            for (int y = 0; y < d; y++) {
                if (party) {
                    result[x][y] = i;
                    results[0][x][y]=i;
                } else {
                    result[y][x] = i;
                    results[0][y][x]=i;
                }
                i++;
            }
        }
        StdOut.println("Striper "+elect.getPurpleWins(result)); //prints out the striper districts won by purple for comparison
        int a=1000;
        int savedIndex=0;
        int wins;
        if(par){wins=0;}else{wins=d;}//set wins to the max or the min depending on party
        for(i=0;i<a;i++) {
            if(swap(i)) {
                for (int p = 0; p < d; p++) {
                    for (int q = 0; q < d; q++) {
                        results[i][p][q] = result[p][q];
                    }
                }
                int purp = elect.getPurpleWins(results[i]);
                if (par) {//gerrymandering for purple
                    if (purp == d) {//best board for purple
                        return results[i];
                    }
                    if (purp > wins) {
                        wins = purp;
                        savedIndex = i;
                    }
                } else { // gerrymandering for yellow
                    if (purp == 0) {//best board for yellow
                        return results[i];
                    }
                    if (purp < wins) {
                        wins = purp;
                        savedIndex = i;
                    }
                }
            }
        }
        return results[savedIndex];
    }

    /**
     * swaps two district lines to create different permutaions of the board
     * @return true if a swap occured, false otherwise
     */
    public boolean swap(int a){
        while(true) {
            int district1 = StdRandom.uniform(0, d); //district 1
            int district2 = StdRandom.uniform(0, d); //district 2
            while(district2==district1){//get a new district if they are the same
                district2 = StdRandom.uniform(0, d); //district 2
            }
            int[] save1 = new int[d];
            int[] save2 = new int[d];
            for(int p=0;p<d;p++){
                save1[p]=result[district1][p];
                save2[p]=result[district2][p];
            }

            if (adjacentDistricts(result[district1], result[district2])) {
                int i=1;
                for( int j=0;j<d;j++) {
                    //start by swapping the ends because the ends are the most likely to be swappable
                    int saved1 = result[district1][j];
                    int saved2 = result[district2][d-i];
                    for ( i = 1; i < d; i++) {
                        result[district1][j] = saved2;
                        result[district2][d-i] = saved1;
                        if(elect.isValidMap(result)){//checks if swap was valid
                            for(int b=1;b<a;b++){
                                if(Arrays.equals(results[b],result)) {//checks if swap creates a new and different board
                                    return false;
                                }
                            }
                            //sort arrays to produce better districts than only swapping ends
                            Arrays.sort(result[district1]);
                            Arrays.sort(result[district2]);
//                            ElectorateDrawer.draw(elect,result);
//                            StdDraw.pause(1000);
                            return true;
                        }else{
                            //reset if the swap was invalid
                            for(int p=0;p<d;p++){
                                result[district1][p]=save1[p];
                                result[district2][p]=save2[p];
                            }
                        }
                    }
                }
                return false;
            }
        }

    }

    /**
     * checks if two districts are next to eachother to see if a swap is even possible between them
     * @param d1 district 1
     * @param d2 district 2
     * @return whether or not district 1 and district 2 are adjacent
     */
    private boolean adjacentDistricts(int[] d1, int[] d2) {
        for(int i=0;i<d;i++){
            for(int j=0;j<d;j++){
                if((d1[i]-1==d2[j]) || (d1[i]+1==d2[j]) || (d1[i]-d==d2[j]) || (d1[i]+d==d2[j])){//check west, east, north, and south
                    return true;
                }
            }
        }
        return false;
    }
}
