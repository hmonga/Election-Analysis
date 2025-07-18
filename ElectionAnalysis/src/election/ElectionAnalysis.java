package election;

/* 
 * Election Analysis class which parses past election data for the house/senate
 * in csv format, and implements methods which can return information about candidates
 * and nationwide election results. 
 * 
 * It stores the election data by year, state, then election using nested linked structures.
 * 
 * The years field is a Singly linked list of YearNodes.
 * 
 * Each YearNode has a states Circularly linked list of StateNodes
 * 
 * Each StateNode has its own singly linked list of ElectionNodes, which are elections
 * that occured in that state, in that year.
 * 
 * This structure allows information about elections to be stored, by year and state.
 * 
 * @author Colin Sullivan
 */
public class ElectionAnalysis {

  // Reference to the front of the Years SLL
  private YearNode years;

  public YearNode years() {
    return years;
  }

  /*
   * Read through the lines in the given elections CSV file
   * 
   * Loop Though lines with StdIn.hasNextLine()
   * 
   * Split each line with:
   * String[] split = StdIn.readLine().split(",");
   * Then access the Year Name with split[4]
   * 
   * For each year you read, search the years Linked List
   * -If it is null, insert a new YearNode with the read year
   * -If you find the target year, skip (since it's already inserted)
   * 
   * If you don't find the read year:
   * -Insert a new YearNode at the end of the years list with the corresponding
   * year.
   * 
   * @param file String filename to parse, in csv format.
   */
  public void readYears(String file) {
    // WRITE YOUR CODE HERE
    StdIn.setFile(file);

    while (StdIn.hasNextLine()) {
      String[] split = StdIn.readLine().split(",");
      int year = Integer.parseInt(split[4]);

      if (years == null) {
        years = new YearNode(year);
      } else {
        YearNode ptr = years;
        while (ptr != null) {
          if (year == ptr.getYear()) {
            break;
          } else {
            ptr = ptr.getNext();
          }
        }
        if (ptr == null) {
          YearNode temp = years;
          while (temp != null) {
            if (temp.getNext() == null) {
              temp.setNext(new YearNode(year));
              break;
            }
            temp = temp.getNext();
          }
        }
      }
    }

  }

  /*
   * Read through the lines in the given elections CSV file
   * 
   * Loop Though lines with StdIn.hasNextLine()
   * 
   * Split each line with:
   * String[] split = StdIn.readLine().split(",");
   * Then access the State Name with split[1] and the year with split[4]
   * 
   * For each line you read, search the years Linked List for the given year.
   * 
   * In that year, search the states list. If the target state exists, continue.
   * onto the next csv line. Else, insert a new state node at the END of that
   * year's
   * states list (aka that years "states" reference will now point to that new
   * node).
   * Remember the states list is circularly linked.
   * 
   * @param file String filename to parse, in csv format.
   */
  public void readStates(String file) {
    // WRITE YOUR CODE HERE
    StdIn.setFile(file);

    while (StdIn.hasNextLine()) {
      String[] split = StdIn.readLine().split(",");
      String state = split[1];
      int year = Integer.parseInt(split[4]);

      YearNode ptrYr = years; // points to the first year node

      while (ptrYr != null && ptrYr.getYear() != year) { // searches through the years LL to find the correct year
        ptrYr = ptrYr.getNext();
      }

      if (ptrYr != null) { // year is found
        StateNode ptrState = ptrYr.getStates(); // points to the last state node
        if (ptrState == null) { // if there are no states in the year
          StateNode newState = new StateNode(); // create a new state node, sets its name, and points to itself
          newState.setStateName(state);
          newState.setNext(newState);
          ptrYr.setStates(newState);
        } else { // if there are states in the year
          StateNode curr = ptrState; // points to the last state node
          do {
            if (curr.getStateName().equals(state)) { // if the state is already in the list
              break;
            }
            curr = curr.getNext();
          } while (curr != ptrState); // go to the next state node until last state node

          if (!curr.getStateName().equals(state)) { // if there isnt a duplicate state
            StateNode newNode = new StateNode(); // create a new state node and set its name
            newNode.setStateName(state);
            newNode.setNext(ptrState.getNext()); // insert the new state node at the end of the list
            ptrState.setNext(newNode);
            ptrYr.setStates(newNode);
          }
        }
      }
    }
  }

  /*
   * Read in Elections from a given CSV file, and insert them in the
   * correct states list, inside the correct year node.
   * 
   * Each election has a unique ID, so multiple people (lines) can be inserted
   * into the same ElectionNode in a single year & state.
   * 
   * Before we insert the candidate, we should check that they dont exist already.
   * If they do exist, instead modify their information new data.
   * 
   * The ElectionNode class contains addCandidate() and modifyCandidate() methods
   * for you to use.
   * 
   * @param file String filename of CSV to read from
   */
  public void readElections(String file) {
    // WRITE YOUR CODE HERE
      StdIn.setFile(file); 
      while(StdIn.hasNextLine()){
      String line = StdIn.readLine();
      String[] split = line.split(",");

      int raceID = Integer.parseInt(split[0]);
      String stateName = split[1];
      int officeID = Integer.parseInt(split[2]);
      boolean senate = split[3].equals("U.S. Senate");
      int year = Integer.parseInt(split[4]);
      String canName = split[5];
      String party = split[6];
      int votes = Integer.parseInt(split[7]);
      boolean winner = split[8].toLowerCase().equals("true");

      YearNode ptrYr = years;                                 // points to the first node in the years LL
      while (ptrYr != null && ptrYr.getYear() != year) {      // searches through the years LL to find year
        ptrYr = ptrYr.getNext();
      }
      if(ptrYr != null && ptrYr.getStates() != null){

      StateNode ptrState = ptrYr.getStates();                // points to the last state node in CLL
        do {
          if (ptrState.getStateName().equalsIgnoreCase(stateName)) { // if the state is found
            break;
          }
          ptrState = ptrState.getNext();                      // go to the next state node    
        } while (ptrState != ptrYr.getStates());                // go to the next state node until the last state node
      
      ElectionNode ptrElec = new ElectionNode();              // points to the first election node in the state
      ptrElec = ptrState.getElections();                          // points to the first election node in the state

      if(ptrState.getElections() == null){                      // if there are no elections in the state
        ElectionNode newElec = new ElectionNode();
        newElec.setRaceID(raceID);
        newElec.setoOfficeID(officeID);
        newElec.addCandidate(canName, votes, party, winner);
        newElec.setSenate(senate);
        ptrState.setElections(newElec);
      }
      else{                                                     // if there are elections in the state          
        while(ptrElec.getNext() != null && ptrElec.getRaceID() != raceID) {         // searches through the election nodes to find the raceID
          if(ptrElec.getRaceID() == raceID){
            break;
          }
        ptrElec = ptrElec.getNext();
      }
      }
    
      if(ptrElec != null){
        if(ptrElec.getRaceID() == raceID){                        // if the raceID is found 
          if(ptrElec.isCandidate(canName)){                        // if the candidate is found  
          ptrElec.modifyCandidate(canName, votes, party);       // modify the candidate
        } else {
          ptrElec.addCandidate(canName, votes, party, winner);      // add the candidate
        }
      }else{                                                      
        ElectionNode newElec = new ElectionNode();              // if the raceID is not found
        newElec.setRaceID(raceID);
        newElec.setoOfficeID(officeID);
        newElec.addCandidate(canName, votes, party, winner);
        newElec.setSenate(senate);
        ptrElec.setNext(newElec);
      }
      }
    }
    }
  }
  
  
    
  

  /*
   * DO NOT EDIT
   * 
   * Calls the next method to get the difference in voter turnout between two
   * years
   * 
   * @param int firstYear First year to track
   * 
   * @param int secondYear Second year to track
   * 
   * @param String state State name to track elections in
   * 
   * @return int Change in voter turnout between two years in that state
   */
  public int changeInTurnout(int firstYear, int secondYear, String state) {
    // DO NOT EDIT
    int last = totalVotes(firstYear, state);
    int first = totalVotes(secondYear, state);
    return last - first;
  }

  /*
   * Given a state name, find the total number of votes cast
   * in all elections in that state in the given year and return that number
   * 
   * If no elections occured in that state in that year, return 0
   * 
   * Use the ElectionNode method getVotes() to get the total votes for any single
   * election
   * 
   * @param year The year to track votes in
   * 
   * @param stateName The state to track votes for
   * 
   * @return avg number of votes this state in this year
   */
  public int totalVotes(int year, String stateName) {
    // WRITE YOUR CODE HERE
    YearNode ptrYr = years;
    int totalVotes = 0;
    while (ptrYr != null && ptrYr.getYear() != year) {
      ptrYr = ptrYr.getNext();
    }
    if(ptrYr != null){
      StateNode ptrState = ptrYr.getStates();
      if(ptrState != null){
      do {
        if (ptrState.getStateName().equalsIgnoreCase(stateName)) {
          break;
        }
        ptrState = ptrState.getNext();
      } while (ptrState != ptrYr.getStates());
    
    if (ptrState.getStateName().equalsIgnoreCase(stateName)) {
      ElectionNode ptrElec = ptrState.getElections();
      while (ptrElec != null) {
        totalVotes += ptrElec.getVotes();
        ptrElec = ptrElec.getNext();
      }
    }
  }
}
    return totalVotes;
}
  /*
   * Given a state name and a year, find the average number of votes in that
   * state's elections in the given year
   * 
   * @param year The year to track votes in
   * 
   * @param stateName The state to track votes for
   * 
   * @return avg number of votes this state in this year
   */
  public int averageVotes(int year, String stateName) {
    // WRITE YOUR CODE HERE

    YearNode ptrYr = years;
    int totalVotes = 0;
    int totalElections = 0;
    while (ptrYr != null && ptrYr.getYear() != year) {
      ptrYr = ptrYr.getNext();
    }
    
    StateNode ptrState = ptrYr.getStates();
    if (ptrYr != null) {
      do {
        if (ptrState.getStateName().equalsIgnoreCase(stateName)) {
          break;
        }
        ptrState = ptrState.getNext();
      } while (ptrState != ptrYr.getStates());
    }

    if (ptrState.getStateName().equalsIgnoreCase(stateName)) {
      ElectionNode ptrElec = ptrState.getElections();
      while (ptrElec != null) {
        totalVotes += ptrElec.getVotes();
        totalElections++;
        ptrElec = ptrElec.getNext();
      }

    }

    if (ptrYr == null || ptrState == null || totalElections == 0) {
      return 0;
    }

    return totalVotes / totalElections;
  }

  /*
   * Given a candidate name, return the party they most recently ran with
   * 
   * Search each year node for elections with the given candidate
   * name. Update that party each time you see the candidates name and
   * return the party they most recently ran with
   * 
   * @param candidateName name to find
   * 
   * @return String party abbreviation
   */
  public String candidatesParty(String candidateName) {
    // WRITE YOUR CODE HERE
    String party = null;
    YearNode ptrYr = years;

    while (ptrYr != null) {
      StateNode ptrState = ptrYr.getStates();
      do {
        ElectionNode ptrElec = ptrState.getElections();
        while (ptrElec != null) {
          if (ptrElec.getCandidates().contains(candidateName) && party == null) {
            party = ptrElec.getParty(candidateName);
          }
          ptrElec = ptrElec.getNext();
        }
        ptrState = ptrState.getNext();
      } while (ptrState != ptrYr.getStates());
      ptrYr = ptrYr.getNext();
    }

    return party;
  }
}