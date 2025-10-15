import java.util.ArrayList;
import java.util.Collections;

/**
 * An abstract class that represents a grouping of any roommate pairs. 
 * It provides basic functions such as getting the total number of roommate pairs in this grouping, 
 * adding roommate pairs. 
 * 
 * How to use an implement an abstract class:
 * https://www.baeldung.com/java-abstract-class
 * 
 *
 * @author Ria Bakshi
 * @version 12/15/24
 */
public abstract class Grouping
{
    //how to use the protected modifier https://www.geeksforgeeks.org/access-modifiers-java/
    protected ArrayList<Roommates> roommatePairs;
    protected String gender;
    
    /**
     * Constructor for objects of class Grouping
     */
    public Grouping()
    {
        roommatePairs = new ArrayList<>();
    }
    
    /**
     * Add a roomate pair to this grouping of students. 
     * @param roomies The roommate pair to add. 
     */
    public void addRoommatePair(Roommates roomies) {
        roommatePairs.add(roomies);
    }
    
    /**
     * Get the roommate pairs in this grouping. 
     * @return An ArrayList of Roommates with each roommate pair
     */
    public ArrayList<Roommates> getRoommatePairs() {
        return roommatePairs;
    }
    
    /**
     * Randomize the roommate pairs in this grouping. 
     */
    public void randomizeRoommatePairs() {
        Collections.shuffle(roommatePairs);
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
     * Remove a roomate pair from this grouping of students. 
     * @param roomie The roommate pair to remove.
     */
    public void removeRoommatePair(Roommates roomies) {
        roommatePairs.remove(roomies);
    }
    
    /**
     * Get the number of roommate pairs in a certain grade. 
     * @param grade The grade to check for. (10, 11, 12)
     * @return The number of students in that grade.
     */
    public int getNumPairsInGrade(int grade) {
        int pairs = 0;
        for (Roommates roomies : roommatePairs) {
            if (roomies.getGrade()==grade) {
                pairs+=1;
            }
        }
        return pairs;
    }
    
    /**
     * Set the gender of this grouping.
     * @param The gender to be used ("F" for AFAB, "M" for AMAB, "A" for all gender)
     */
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    /**
     * Get the gender of this grouping. 
     * @return The gender of this grouping.
     */
    public String getGender() {
        return gender;
    }
    
    /**
     * Get the total number of roommate pairs in a certain gender. 
     * @param the gender to check for ("F" or "M")
     * @return the total number of roommate pairs of that gender in this grouping. 
     */
    public int getNumPairsOfGender(String gender) {
        int pairs = 0; 
        for (Roommates roomies : roommatePairs) {
            if (roomies.getGender().equals(gender)) {
                pairs+=1;
            }
        }
        return pairs;
    }
    
    /**
     * Get the total number of roommate pairs in this grouping.
     * @return The total number of roommate pairs in this grouping. 
     */
    public int getTotal()
    {
        return roommatePairs.size();
    }
}
