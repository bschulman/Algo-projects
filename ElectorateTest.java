import edu.princeton.cs.algs4.Graph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ElectorateTest {

    private Electorate electorate;

    @BeforeEach
    void setUp() {
        electorate = new Electorate(3);
    }

    @Test
    void acceptsValidMap() {
        int[][] districts = {
                {0, 1, 2},
                {3, 4, 5},
                {6, 7, 8}};
        assertTrue(electorate.isValidMap(districts));
    }

    @Test
    void rejectsMapWithTooManyVoters() {
        int[][] districts = {
                {0, 1, 2, 9},
                {3, 4, 5, 10},
                {6, 7, 8, 11}};
        assertFalse(electorate.isValidMap(districts));
    }

    @Test
    void rejectsMapWithVoterInMoreThaOneDistrict() {
        int[][] districts = {
                {0, 1, 2},
                {0, 1, 2},
                {0, 1, 2}};
        assertFalse(electorate.isValidMap(districts));
    }

    @Test
    void rejectsMapThatOmitsVoter() {
        int[][] districts = {
                {0, 1},
                {3, 4},
                {6, 7}};
        assertFalse(electorate.isValidMap(districts));
    }

    @Test
    void rejectsMapWithNoncontinguousDistrict() {
        int[][] districts = {
                {1, 2, 5},
                {0, 4, 8},
                {3, 6, 7}};
        assertFalse(electorate.isValidMap(districts));
    }

    @Test
    void rejectsMapWithTooManyDistricts() {
        int[][] districts = {
                {0},
                {1},
                {2},
                {3},
                {4},
                {5},
                {6},
                {7},
                {8}};
        assertFalse(electorate.isValidMap(districts));
    }

    @Test
    void rejectsMapWithTooFewDistricts() {
        int[][] districts = {{0, 1, 2, 3, 4, 5, 6, 7, 8}};
        assertFalse(electorate.isValidMap(districts));
    }

    @Test
    void rejectsMapWithUnevenDistricts() {
        int[][] districts = {
                {0, 1},
                {2, 3, 4, 5},
                {6, 7, 8}};
        assertFalse(electorate.isValidMap(districts));
    }



}