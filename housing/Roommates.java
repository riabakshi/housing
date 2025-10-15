
/**
 * A class which represents two roommate pairs.
 * This holds information about preferences and 
 * their rooming assignments when they are assigned. 
 *
 * @author Ria Bakshi
 * @version 12/13/24
 * 
 */
public class Roommates
{
    //necessary information
    private String[] names;
    private String gender;
    private int grade;
    
    // top 5 hall preferences
    private String[] hallPreferences;
    // A, B, C, D wing preferences for top ranked hall
    private String[] wingPreferences;
    
    //extra roommate information
    private String roomPreference; 
    
    //rooming information
    private Hall hall;
    private Wing wing;
    private int roomNum;
    
    private String letter;
    /**
     * @param names The full names of each roommate pair. 
     * @param gender The gender of the roommate pair ("F" for AFAB or "M" for AMAB)
     * @param grade The grade of the roommate pair (10,11,12).
     * @param hallPreferences The ranked hallPreferences of the Roommate pair.
     * @param wingPreferences The ranked wingPreferences of the Roommate pair.
     */
    public Roommates(String[] names, String gender,  int grade, String[] hallPreferences, 
        String[] wingPreferences)
    {
        this.names = names;
        this.gender = gender;
        this.grade = grade;
        this.hallPreferences = hallPreferences;
        this.wingPreferences = wingPreferences;
    }
    
    /**
     * Add rooming preference information. 
     * @param roomPreference Extra rooming information (quad, l-room, hypoallergenic)
     */
    public void setRoomPreference(String roomPreference) {
        this.roomPreference = roomPreference;
    }
    
    /**
     * Get rooming preference information. 
     * @return This roommate pair's room preference. Returns an null if there is none.
     */
    public String getRoomPreference() {
        return roomPreference;
    }
    
    /**
     * Get the hall of this roommate.
     */
    public Hall getHall() {
        return hall;
    }
    
    /**
     * Get the wing of this roommate.
     */
    public Wing getWing() {
        return wing;
    }
    
    /**
     * Get the room number of this roommate.
     */
    public int getNum() {
        return roomNum;
    }
    
    /**
     * Get housing information. This comes in the form of the hall number
     * and the wing and the room number concatenated as one
     * @return An array containing the hall number,
     * the wing, and the room number.
     */
    public String[] getHousing() {
        String[] housing = new String[1];
        housing[0] = hall.getHallNumber();
        //System.out.println(wing + " " + roomNum);
        //housing[1] = wing.getLetter() + roomNum;
        return housing;
    }
    
    /**
     * Set the hall for this roommate pair.
     * @param hall The hall that this roommate pair lives in. 
     */
    public void setHall(Hall hall) {
        this.hall = hall;
    }
    
    /**
     * Set the wing for this roommate pair.
     * @param wing The wing that this roommate pair lives in. 
     */
    public void setWing(Wing newWing) {
        wing = newWing;
    }
    
    /**
     * Set the roomNumber for this roommate pair.
     * @param hall The hall that this roommate pair lives in. 
     */
    public void setRoomNumber(int num) {
        this.roomNum = num;
    }
    
    /**
     * Get the grade of the roommate pair.
     * @return The grade of the roommate pair. (F for AFAB or M for AMAB)
     */
    public int getGrade() {
        return grade;
    }
    
    /**
     * Get the grade of the roommate pair.
     * @return The grade of the roommate pair.
     */
    public String getGender() {
        return gender;
    }
    
    /**
     * Get the names of the roomate pairs
     * @return The names of roomate pairs in a String[] of two
     */
    public String[] getNames() {
        return names;
    }
    
    /**
     * Get a certain ranked hall preference of the roommate pair.
     * @param rank The desired ranked hall (1-5)(first choice would be 1, second would be 2, etc.)
     */
    public String getNthHallPreference(int rank) {
        int index = rank-1;
        return hallPreferences[index];
    }
    
    /**
     * Get a certain ranked wing preference of the roommate pair.
     * @param rank The desired ranked hall (1-4)(first choice would be 1, second would be 2, etc.)
     */
    public String getNthWingPreference(int rank) {
        int index = rank-1;
        return wingPreferences[index];
    }
    
    /**
     * Get the hall preferences of the roommate pair.
     * @return The ranked hall preferences of the roommate pair as a String[] of length 5
     */
    public String[] getHallPreferences() {
        return hallPreferences;
    }

    /**
     * Get the wing preferences of the roommate pair.
     * @return The ranked wing preferences of the roommate pair as a String[] of length 5
     */
    public String[] getWingPreferences() {
        return wingPreferences;
    }
}
