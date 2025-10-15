import java.util.ArrayList;
/**
 * This class represents a wing.
 *
 * @author Ria Bakshi
 * @version 12/13/24
 */
public class Wing extends Grouping
{
    //room numbers for special rooms
    public static final int LROOM_1 = 11;
    public static final int LROOM_2 = 21;
    public static final int DOWNQUAD_1 = 15;
    public static final int DOWNQUAD_2 = 16;
    public static final int UPQUAD_1 = 25; 
    public static final int UPQUAD_2 = 26;
    public static final int HYPOALLERGENIC = 15;
    
    private Roommates[] rooms;
    private String letter;
    
    /**
     * @param gender The gender of the this wing. 
     * @param letter The letter assigned to this wing ("A", "B", "C", or "D")
     */
    public Wing(String gender, String letter) {
        // each room index corresponds with the room number
        // 1-6 are on the first floor, 7-12 are on the second floor
        // initial values are null
        this.gender = gender;
        this.letter = letter;
        rooms = new Roommates[12];
    }
       
    /**
     * Get the letter associated with this wing.
     * @return the letter of this wing.
     */
    public String getLetter() {
        return letter;
    }
    
    /** 
     * Set the wing for all the roommates in this wing to 
     * this wing. 
     */
    private void setWingForAllRoommates() {
        for (Roommates roomies : roommatePairs) {
            roomies.setWing(this);
        }
    }
    
    /**
     * Sort those in this wing into a room. 
     * In the future, this method could be implemented to account for room preference info 
     * (ex. quads, LRooms, etc.) 
     */
    public void sortIntoRooms() {
        setWingForAllRoommates();
        randomizeRoommatePairs();
        
        ArrayList<Roommates> pairs = new ArrayList<>();
        ArrayList<Roommates> hasRoomPreference = getPairsWithRoomPreference();
        for (Roommates roomies : roommatePairs) {
            if (!hasRoomPreference.contains(roomies)) {
                pairs.add(roomies);
            }
        }
        
        //print some info about number of people in this wing
        System.out.println(letter);
        System.out.println("AFAB: " + getNumPairsOfGender("F"));
        System.out.println("AMAB: " + getNumPairsOfGender("M"));
        System.out.println("\n");
        
        //assign each roommate pair to a room
        for (int i = 0; i < pairs.size(); i++) {
            //rooms.add(roommatePairs.get(i));
            rooms[i] = pairs.get(i);
            int num = i+1;
            //room is upstairs
            if (num > 6) {
                //subtract 6 add 20
                num+=14;
            } else {
                num+=10;
            }
            pairs.get(i).setRoomNumber(num);
            //System.out.println(letter + "" + num);
        }
    }
    
    /**
     * Sort all students that have a room preference. 
     */
    public void sortRoomPreferences() {
        ArrayList<Roommates> pairs = getPairsWithRoomPreference();
        //TODO add case quad
        for (Roommates roomies : pairs) {
            // holds the room number preference of the roommate
            int preference = -1;
            switch (roomies.getRoomPreference()) {
                case ("l-room"):
                    preference = getLRoom();
                    break;
                case ("hypoallergenic"):
                    preference = getHypoallergenic();
                    break;
            }
            //desired preference is available
            if (preference > 0) {
                //convert to index
                int index = preference > 20 ? preference - 15 : preference -11;
                if (rooms[index] != null) {
                    rooms[index] = roomies;
                }
            }
        }
    }
    
    /**
     * Add a roommate pair to a specific room.
     * @param roomies The roommates to add.
     * @param index The index of the room to add the roommate to. 
     * @return true if pair was successfully added to the desired room.
     */
    public boolean addRoommateToRoom(Roommates roomies, int index) {
        boolean noError = false;
        if (rooms[index] != null) {
            rooms[index] = roomies;
            noError = true;
        }
        return noError;
    }
    
    /**
     * Get all the roommate pairs with a room preference. 
     * @return An arraylist with all the rooommate pairs who have a room preference. 
     */
    public ArrayList<Roommates> getPairsWithRoomPreference() {
        ArrayList<Roommates> pairs = new ArrayList<>();
        for (Roommates pair : roommatePairs) {
            if (pair.getRoomPreference() != null) {
                pairs.add(pair);
                System.out.println(pairs.size());
            }
        }
        return pairs;
    }
    
    /**
     * Check the eligibility of a roommate pair into this wing.
     * A roommate pair is eligible if their gender (AFAB or AMAB) matches
     * the gender of this wing and if they are less than or equal to the fourth (TODO change if extra implementation)
     * roommate pair in their grade in this wing. 
     * 
     * @param roomies The roomate pair to check.
     * @return True if the roommate pair is eligible.
     */
    public boolean checkEligibility(Roommates roomies) {
        boolean eligible = false;
        if (roomies.getGender().equals(gender) && 
            roommatePairs.size() < 12 &&
            //TODO change percentage (4 to 5 or more idk) as needed
            getNumPairsInGrade(roomies.getGrade()) < 4) {
            eligible = true;
        }
        return eligible;
    }
    
    /**
     * Check if there is any available space in this wing. 
     * @return true if there is.
     */
    public boolean isSpace() {
        boolean space = false;
        if (roommatePairs.size() < 12) {
            space = true;
        }
        return space;
    }
    
    /**
     * Get the next available l-room in this wing. 
     * @return the room number of the next available L-Room (Note: NOT index), -1 if there are none available
     */
    public int getLRoom() {
        int[] lrooms = {LROOM_1, LROOM_2};
        int roomNum = getSpecialRooms(lrooms);
        return roomNum;
    }
    
    /**
     * Get the next available hypoallergenic room in this wing. 
     * @return the room number of the next available hypoallergenic room(Note: NOT index), -1 if there are none available
     */
    public int getHypoallergenic() {
        int[] hypoallergenic = {HYPOALLERGENIC};
        int roomNum = getSpecialRooms(hypoallergenic);
        return roomNum;
    }
    
    /**
     * Get the next available l-room in this wing. 
     * @return the room numbers of the available quad, null if there are none available
     */
    public int[] getQuad() {
        //check if any of the quads are potentially available
        //note: if a hypoallergenic room is taken, then downquad will not be available
        //fill upquad first in order to save hypoallergenic for those who need it
        int[] quads = {UPQUAD_1, DOWNQUAD_1};
        int[] availableRooms = new int[2];
        int roomNum = getSpecialRooms(quads);
        if (roomNum == UPQUAD_1) {
            //get the index of the second up quad room
            int index = (UPQUAD_2)-15; 
            if (rooms[index] == null) {
                //upquad is available
                availableRooms[0] = UPQUAD_1;
                availableRooms[1] = UPQUAD_2;
            }
        } else if (roomNum == DOWNQUAD_1) {
            //follow same method above
            int index = (DOWNQUAD_2)-11;
            if (rooms[index] == null) {
                //down quad is available
                availableRooms[0] = DOWNQUAD_1;
                availableRooms[1] = DOWNQUAD_2;
            }
        } else {
            availableRooms = null;
        }
        return availableRooms;
    }
    
    /**
     * The code that checks for availability in each of the given room numbers in this wing.
     * @param roomsToCheck An array of the potential room numbers to check if they are available. 
     * @return the room number of the first available room number given in the array (Note: NOT index), -1 if there are none available
     */
    private int getSpecialRooms(int[] roomsToCheck) {
        int roomNum = -1;
        for (int num : roomsToCheck) {
            //convert values to an index
            int index = -1;
            if (num > 10 && num < 20) {
                index = num-11;
            } else {
                //if on the second floor
                index = num-15;
            }
            if (rooms[index] == null) {
                roomNum = num;
            }
        }
        return roomNum;
    }
}
