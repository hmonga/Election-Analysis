import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;

import org.junit.*;

import election.*;

/*
* This is a Java Test Class, which uses the JUnit package to create
* and run tests
* 
* You can use this to evaluate your code. Examine these tests, as writing
* similar test cases will help you immensly on other Assignments/Labs, as
* well as moving forward in your CS career.
*/
public class ElectionAnalysisTest {

    // INPUT FILE FORMAT:
    // RaceID, State, OfficeID, Senate/House, YEAR, CANDIDATENAME, Party, VotesReceived, Winner (true/false)
    private static final String testFile = "testInput.csv";

    @Test
    public void testReadYears() {
        ElectionAnalysis test = new ElectionAnalysis();
        test.readYears(testFile);

        // By Default, the test input csv contains three unique years (1, 2, and 6)
        // So we will traverse the years list and check they appear in the specified order
        YearNode ptr = test.years();
        assertTrue(ptr != null);
        assertTrue(ptr.getYear() == 2003);
        ptr = ptr.getNext();
        assertTrue(ptr.getYear() == 2011);
        ptr = ptr.getNext();
        assertTrue(ptr.getYear() == 2009);

        // We will also check that there are no extra nodes
        assertTrue(ptr.getNext() == null);
    }


    @Test
    public void testReadStates() {
        ElectionAnalysis test = new ElectionAnalysis();
        test.readYears(testFile);
        test.readStates(testFile);
        
        int[] years = {2003, 2011, 2009};
        String[][] statesInYears = {{"A1"}, {"A2"}, {"B3", "B5"}};
        int year = 0;
        for (YearNode yr = test.years(); yr != null; yr = yr.getNext()) {
            assertTrue(yr.getYear() == years[year]);
            int state = 0;
            StateNode st = yr.getStates().getNext(); // Starts at front of list
            do {
                assertTrue(st.getStateName().equals(statesInYears[year][state]));
                state++;
                st = st.getNext();
            } while ( st != yr.getStates().getNext());
            year++;
        }
        
        ElectionAnalysis house = new ElectionAnalysis();
        house.readYears("house.csv");
        house.readStates("house.csv");
        house.readElections("house.csv");
        ElectionAnalysis senate = new ElectionAnalysis();
        senate.readYears("senate.csv");
        senate.readStates("senate.csv");
        senate.readElections("senate.csv");

        assertTrue(house.years().getStates().getStateName().equals("VA"));
        
        // AL is the last state to be added, and GA is the first
        // getStates() should always point to the last state
        assertTrue(senate.years().getStates().getStateName().equals("AL"));
        assertTrue(senate.years().getStates().getNext().getStateName().equals("GA"));
    }    
    @Test
    public void testReadElections() {
        ElectionAnalysis house = new ElectionAnalysis();
        house.readYears("house.csv");
        house.readStates("house.csv");
        house.readElections("house.csv");
        ElectionAnalysis senate = new ElectionAnalysis();
        senate.readYears("senate.csv");
        senate.readStates("senate.csv");
        senate.readElections("senate.csv");

        assertTrue(house.years().getStates().getElections().getRaceID() == 10004);
        assertTrue(senate.years().getStates().getElections().getRaceID() == 8917);
        assertTrue(senate.years().getStates().getNext().getElections().getNext().getRaceID() == 8925);
    }

    @Test
    public void testAvgVotes() {
        ElectionAnalysis senate = new ElectionAnalysis();
        senate.readYears("senate.csv");
        senate.readStates("senate.csv");
        senate.readElections("senate.csv");
        ElectionAnalysis house = new ElectionAnalysis();
        house.readYears("house.csv");
        house.readStates("house.csv");
        house.readElections("house.csv");

        assertTrue(house.averageVotes(1998, "CO") == 212358);
        assertTrue(senate.averageVotes(1998, "NY") == 4127269);
    }

    @Test
    public void testTotalVotes() {
        ElectionAnalysis test = new ElectionAnalysis();
        test.readYears(testFile);
        test.readStates(testFile);
        test.readElections(testFile);
        ElectionAnalysis house = new ElectionAnalysis();
        house.readYears("house.csv");
        house.readStates("house.csv");
        house.readElections("house.csv");
        ElectionAnalysis senate = new ElectionAnalysis();
        senate.readYears("senate.csv");
        senate.readStates("senate.csv");
        senate.readElections("senate.csv");

        assertTrue(test.totalVotes(2003, "A1") == 15002);
        assertTrue(test.totalVotes(2011, "A2") == 1061);
        assertTrue(test.totalVotes(2009, "B3") == 2000);
        assertTrue(test.totalVotes(2009, "B5") == 2498);

        assertTrue(house.totalVotes(2020, "NJ") == 4432923);
        assertTrue(senate.totalVotes(2001, "NJ") == 0);
        assertTrue(house.totalVotes(2008, "NY") == 1826591);
    }

    @Test
    public void testCandidatesParty() {
        ElectionAnalysis house = new ElectionAnalysis();
        house.readYears("house.csv");
        house.readStates("house.csv");
        house.readElections("house.csv");
        ElectionAnalysis senate = new ElectionAnalysis();
        senate.readYears("senate.csv");
        senate.readStates("senate.csv");
        senate.readElections("senate.csv");

        assertTrue(senate.candidatesParty("Hillary Rodham Clinton").equals("DEM"));
        assertTrue(house.candidatesParty("Michael C. Turzai").equals("REP"));
        assertTrue(senate.candidatesParty("Mitt Romney").equals("REP"));
        assertTrue(house.candidatesParty("Hakeem S. Jeffries").equals("DEM"));
    }
        
}
