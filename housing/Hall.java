import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * The class represents a hall. It contains the method which sorts
 * roommates into wings. 
 * 
 * @author Ria Bakshi
 * @version 12/13/24
 */
public class Hall extends Grouping
{
    private String number;
    private Wing A;
    private Wing B;
    private Wing C;
    private Wing D;
    
    /**
     * @param number The hall's number. (ex. "01");
     * @param gender The gender of the hall (F for AFAB; M for AMAB; A for all gender)
     */
    public Hall(String number, String gender)
    {
        this.number = number;
        this.gender = gender;
        if (gender.equals("A")) {
            A = new Wing ("F", "A");
            B = new Wing("F", "B");
            C = new Wing("M", "C");
            D = new Wing("M", "D");
        } else {
            A = new Wing(gender, "A");
            B = new Wing(gender, "B");
            C = new Wing(gender, "C");
            D = new Wing(gender, "D");
        }
        
    }
    
    /**
     * Get the hall number. 
     * @return the hall number
     */
    public String getHallNumber() {
        return number;
    }
   
    /**
     * Get the desired wing for hall
     * @param type The character for the desired wing (A, B, C, D)
     * @return An object reference to the desired wing. 
     * Returns null if not a valid character
     */
    public Wing getWing(String type) {
        Wing wing = null;
        switch (type) {
            case ("A"):
                wing = A;
                break;
            case ("B"):
                wing = B;
                break;
            case ("C"):
                wing = C;
                break;
            case ("D"):
                wing = D;
                break;
        }
        return wing;
    }
    
    public Wing[] getWings() {
        Wing[] wings = new Wing[4];
        wings[0] = A;
        wings[1] = B;
        wings[2] = C;
        wings[3] = D;
        return wings;
    }
    
    public void setHallForRoommates() {
        for (Roommates roomies : roommatePairs) {
            roomies.setHall(this);
        }
    }

    /**
     * Sort students into wings.
     * TODO: is this fair? to first sort 
     * those with a room preference into wings? 
     * 
     * Methodology: 
     * 1. Sort those who with a room preference first. 
     * 2. Then sort those whose first choice hall preference it is get priority into wings
     * 3. Rest get sorted into wings based on availability
     */
    public void sortIntoWings() {
        //set the hall for these roommate pairs
        setHallForRoommates();
        System.out.println();
        System.out.println(number);
        //System.out.println("F: " + getNumPairsOfGender("F"));
        //System.out.println("M: " + getNumPairsOfGender("M"));
        ArrayList<Roommates> hasRoomPreference = getPairsWithRoomPreference();
        ArrayList<Roommates> priority = new ArrayList<>();
        ArrayList<Roommates> regular = new ArrayList<>();
        //int[] rankCounts = new int[5];
        Wing[] wings = getWings();
        
        sortRoomPreferences();
        
        //sort into priority lists for those who do not have a room preference
        for (Roommates roomies : roommatePairs) {
            if (!hasRoomPreference.contains(roomies)) {
                //check if its the roommates first choice hall
                if (roomies.getNthHallPreference(1).equals(number)) {
                    priority.add(roomies);
                } else {
                    regular.add(roomies);
                }
            }
        }
        
        //randomize order of dealing out wings
        Collections.shuffle(priority);
        Collections.shuffle(regular);
        
        for (Roommates roomies : priority) {
            sortRoomate(roomies, true);
        }
        
        for (Roommates roomies : regular) {
            sortRoomate(roomies, false);
        }
        
        System.out.println("Number of people in each wing: ");
        for (Wing wing : wings) {
            //System.out.println(wing.getTotal());
            wing.sortIntoRooms();
        }
    }
    
    /**
     * Sort all students that have a room preference. 
     */
    public void sortRoomPreferences() {
        ArrayList<Roommates> pairs = getPairsWithRoomPreference();
        //TODO add case quad
        for (Roommates roomies : pairs) {
            boolean stop = false;
            for (int i = 0; !stop && i < 4; i++) {
                Wing wing = getWing(roomies.getNthWingPreference(i+1));
                // holds the room number preference of the roommate
                int preference = -1;
                switch (roomies.getRoomPreference()) {
                    case ("l-room"):
                        preference = wing.getLRoom();
                        break;
                    case ("hypoallergenic"):
                        preference = wing.getHypoallergenic();
                        break;
                }
                //desired preference is valid
                if (preference > 0) {
                    //convert to index
                    int index = preference > 20 ? preference - 15 : preference -11;
                    //try adding to desired room
                    //else room is already taken or index is not valid
                    if (wing.addRoommateToRoom(roomies, index)) {
                        roomies.setRoomNumber(preference);
                        stop = true;
                    }
                }
            }
        }
    }
    
    /**
     * Sort a singular roommate pair. 
     * Methodology:
     * This first tries to evenly distribute classes into wings (senior, junior, soph). 
     * However, if this is not possible, then they are sorted into the next available wing. 
     * 
     * @param roomies The pair of roommates to sort. 
     * @param prority Whether or not this roommate is of priority.
     */
    private void sortRoomate(Roommates roomies, boolean priority) {
        Wing[] wings = getWings();
        boolean stop = false;
        // try to evenly distribute classes into wings first
        if (priority) {
            //get priority rankings
            for (int i = 0; !stop && i < 4; i++) {
                Wing wing = getWing(roomies.getNthWingPreference(i+1));
                if (wings[i].checkEligibility(roomies)) {
                    wings[i].addRoommatePair(roomies);
                    stop = true;
                }
            }
        } else {
            //check eligibility within any available wing
            for (int i = 0; !stop && i < 4; i++) {
                if (wings[i].checkEligibility(roomies)) {
                    wings[i].addRoommatePair(roomies);
                    stop = true;
                }
            }
        }
        
        //if unable to evenly distribute, add to next available wing
        //regardless of class distribution
        //TODO future implementation: add to the wing with the least amount of people in that class
        if (!stop) {
            Wing wing = getNextAvailableWing(roomies.getGender());
            if (wing != null) {
                wing.addRoommatePair(roomies);
            }
        }
    }
    
    /**
     * Get the next available wing for this hall. 
     * 
     * @param The gender to check for.
     * @return The next available wing.
     */
    public Wing getNextAvailableWing(String gender) {
        Wing[] wings = getWings();
        int j = 0;
        int f = 4;
        if (this.gender.equals("A")) {
            j = gender.equals("F") ? 0 : 2;
            f = gender.equals("F") ? 2 : 4;
        }
        Wing wing = null;
        boolean stop = false;
        for (int i = j; !stop && i < f; i++) {
            //System.out.println(gender + ": " + wings[i].getLetter());
            if (wings[i].isSpace()) {
                wing = wings[i];
                //System.out.println(wing.getLetter() + " " + wing.getTotal());
                stop = true;
            }
        }
        if (wing == null) {
            System.out.println("cannot fit: " + gender);
        }
        return wing;
    }
    
    /**
     * Check if a roommate pair is eligible to enter this hall. 
     * A roommate pair is eligible if their gender matches the gender of the hall. 
     * (Any gender is allowed into all gender halls).
     * They must also be less than or equal to the sixteenth (TODO change if extra implementation)
     * roommate pair in their grade in this hall. 
     * 
     * @param roomies The roommate pair to check for. 
     * @return True if the roommate pair is eligible to enter the hall.
     */
    public boolean checkEligibility(Roommates roomies) {
        boolean eligible = false;
        if (gender.equals("A")) {
            if (getNumPairsOfGender(roomies.getGender()) < 24 && roommatePairs.size() < 48 && getNumPairsInGrade(roomies.getGrade()) < 16) {
                eligible = true;
            }
        } else if (roomies.getGender().equals(gender)) {
            //TODO change if extra implementation
            if (roommatePairs.size() < 48 && getNumPairsInGrade(roomies.getGrade()) < 16) {
                eligible = true;
            }
        }
        return eligible;
    }
}
