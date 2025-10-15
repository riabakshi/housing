import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.io.PrintWriter;

/**
 * This Analyzer class initalizes the process of sorting. 
 * It first reads student data from the preferences.txt file
 * and creates associated halls. 
 * It then sorts students into halls, 
 * which are then sorted into wings (Halls class), 
 * which are then sorted into rooms (Wings clas). 
 *
 * @author Ria Bakshi
 * @version 12/14/24
 */
public class Analyzer
{
    // all roommate pairs
    private ArrayList<Roommates> pairs;
    private Hall[] halls;
    private int[] hallPreferenceCounts;
    
    /**
     * Constructor for objects of class Analyzer
     */
    public Analyzer()
    {
        //randomInfo();
        pairs = new ArrayList<>();
        // create seven halls
        //index is one less than hall number
        halls = new Hall[7];
        for (int i = 0; i < 7; i++) {
            String hallNum = "0" + (i+1);
            //default is all gender halls
            String gender = "A";
            if (i==1 || i==5) {
                //set afab halls
                gender = "F";
            } else if (i==3 || i==4) {
                //set amab halls
                gender = "M";
            }
            halls[i] = new Hall(hallNum, gender);
        }
        populate();
        //sort all student information into halls
        sortIntoHall();
        for (int i = 0; i < halls.length; i++) {
            //sort the roommate pairs into wings
            //this automatically initaties organizing roommate pairs into rooms
            halls[i].sortIntoWings();
        }
        printStatistics();
    }
    
    /**
     * Main method of this analyzer. This runs the analyzer method,
     * which sorts students into rooms. Then, it prints the resulting rooming
     * assigments. 
     */
    public static void main(String args[]) {
        Analyzer analyzer = new Analyzer();
        analyzer.printRoomAssignments();
    }
    
    /**
     * Print the resulting room assignments to a text file.
     * This is intended to be called after rooms have been sorted.
     */
    public void printRoomAssignments() {
        try {
            FileWriter writer = new FileWriter("housing-assignments.txt");
            PrintWriter printWriter = new PrintWriter(writer);
            writer.write("Roommate Names                          Hall   Wing  Room Number\n");   
            String[] wingLetters = {"A", "B", "C", "D"};
            for (Hall hall : halls) {
                Wing[] wings = hall.getWings();
                for (int i =0; i< 4;i++) {
                    Wing wing = wings[i];
                    ArrayList<Roommates> roommates = wing.getRoommatePairs();
                    //System.out.println(roommates.size());
                    for (Roommates roomies : roommates) {
                        String sHall = roomies.getHall().getHallNumber();
                        String sWing = wingLetters[i];
                        //System.out.println(sWing);
                        String room = roomies.getNum() + "";
                        String[] names = roomies.getNames();
                        //writer.write(names[0] + ", " + names[1] + "\t" + sHall + " " + sWing + room + "\n");
                        printWriter.printf("%35s %10s %5s%3s \n", names[0] + ", " + names[1], sHall, sWing, room);
                    }
                }
            }
            System.out.println("Successfully printed housing assignments to housing-assignments.txt");
            printWriter.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
    /**
     * Print ranking statistics to a file.
     * @param header The header to print above the list of statistics
     * @param array The array of statistics to print
     */
    public void printStatistics() {
        try {
            FileWriter writer = new FileWriter("statistics.txt");
            writer.write("Those who got their desired rankings: \n");
            String[] rank = {"first", "second", "third", "fourth", "fifth"};
            for (int i = 1; i < hallPreferenceCounts.length; i++) {
                writer.write(hallPreferenceCounts[i] + " people got their " + rank[i-1] + " hall choice.\n");
            }
            writer.write("\n");
            writer.write("Hall\tWing\tGender\tAmount of People in Wing");
            for (Hall hall : halls) {
                writer.write("\n");
                for (Wing wing : hall.getWings()) {
                    writer.write(hall.getHallNumber() + "\t" + wing.getLetter() + "\tA" + wing.getGender() + "AB\t"+ wing.getTotal() + "\n");
                }
            }
            System.out.println("Successfully printed statistics to statistics.txt");
            writer.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
    /**
     * Sort students into halls. Students are guaranteed to get into their top three halls.
     * This works by randomizing the order in which people are doled out their preferences. 
     * Students are placed into their top three halls. However, if they are unable to 
     * get these, they are sorted into an unranked array list. 
     * People in the unranked array list are then prioritized to enter their top three halls. 
     * This method removes people who got their first hall choice and moves them to their second or third hall choice. 
     * It then prints statistic results to a stats file. 
     */
    public void sortIntoHall() {
        //randomize the order
        ArrayList<Roommates> randomizedPairs = pairs;
        ArrayList<Roommates> unranked = new ArrayList<>();
        //check how many people are recieiving which of their desired hall ranks
        int[] rankCounts = new int[6];
        //learned how to randomize using https://stackoverflow.com/questions/16112515/how-to-shuffle-an-arraylist
        Collections.shuffle(randomizedPairs);
        for (Roommates roomies : randomizedPairs) {
            //check if they are eligible for their top 3 hall preferences
            boolean stop = false;
            int rank = 0;
            for (int i = 0; !stop && i < 2; i++) {
                int index = Integer.parseInt(roomies.getNthHallPreference(i+1))-1;
                Hall hall = halls[index];
                //check if a roommate is eligible to enter a hall
                if (hall.checkEligibility(roomies)) {
                    hall.addRoommatePair(roomies);
                    //sortedHalls.get(index).add(roomies);
                    rank = i+1;
                    stop = true;
                }
            }
            if (!stop) {
                unranked.add(roomies);
            }
            //update rankcounts
            rankCounts[rank]++;
            String hall = !stop ? "unranked" : roomies.getNthHallPreference(rank);
            //System.out.println(roomies.getNames()[0] + ", "+ rank + " choice hall: " + hall);
        }
        
        int totalRemovedFromUnranked = 0;
        
        for (Roommates roomies : unranked) {
            boolean valid = false;
            Hall hall = halls[Integer.parseInt(roomies.getNthHallPreference(1))-1];
            hall.randomizeRoommatePairs();
            //get a random index
            int rank = 2;
            boolean stop = false;
            // place someone who is unranked into their first choice hall, and move someone else down
            while (!stop && rank < 4) {
                for (int r = 0; !stop && r < hall.getTotal(); r++) {
                    Roommates remove = hall.getRoommatePairs().get(r);
                    //must remove a roommate of the same gender in order to avoid overloading all gender halls
                    if (remove.getGender().equals(roomies.getGender())) {
                        Hall newHall = halls[Integer.parseInt(remove.getNthHallPreference(rank))-1];
                        if (newHall.checkEligibility(remove)) {
                            hall.removeRoommatePair(remove);
                            rankCounts[rank-1]+=-1;
                            newHall.addRoommatePair(remove);
                            rankCounts[rank]++;
                            hall.addRoommatePair(roomies);
                            rankCounts[1]++;
                            totalRemovedFromUnranked++;
                            stop = true;
                        }
                    }
                }
                rank++;
            }
        }
        
        //Printing information
        System.out.println("Number of people in each hall:");
        for (int i = 0; i< halls.length; i++) {
            System.out.println("0" + (i+1) + ": " + halls[i].getTotal());
        }
        
        hallPreferenceCounts = rankCounts;
    }
    
    /**
     * Populate the ArrayList with student info from the preferences.txt file.
     */
    public void populate() {
        try {
            Scanner scanner = new Scanner(new File("preferences.txt"));
            while (scanner.hasNextLine()) {
                String[] info = scanner.nextLine().split("\t");
                String[] names = info[0].split(",");
                String gender = info[1];
                String[] hallPreferences = info[2].split(" ");
                String[] wingPreferences = info[3].split(" ");
                int grade = Integer.parseInt(info[4]);
                Roommates roomies = new Roommates(names, gender, grade, hallPreferences, wingPreferences);
                
                if (info.length > 5) {
                    roomies.setRoomPreference(info[5]);
                }
                
                pairs.add(roomies);
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
    /**
     * Write random information to the preferences.txt data file. 
     * This was only called to create random preference data.
     * TODO Update check eligibility methods to reflect different number of people in each grade. 
     */
    private void randomInfo() {
        ArrayList<String> names = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File("random-names.txt"));
            while (scanner.hasNextLine()) {
                names.add(scanner.nextLine());
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        
        try {
            FileWriter writer = new FileWriter("preferences.txt");
            
            // create 10 random data values
            Random randy = new Random();
            String[] HALLS = {"01", "02", "03", "04", "05", "06", "07"};
            String[] genders = {"F", "M"};
            String[] WINGS = {"A", "B", "C", "D"};
            int[] grade = {10, 11, 12};
            
            for (int i = 0; i < 336; i++) {
                // get random names
                int i1 = randy.nextInt(names.size());
                String name1 = names.get(i1);
                names.remove(i1);
                int i2 = randy.nextInt(names.size());
                String name2 = names.get(i2);
                names.remove(i2);
                //TODO create where there are same number of female and male genders, reflective of IMSA population
                String gender;
                if (i < 168) {
                    gender = "F";
                } else {
                    gender = "M";
                }
                //String gender = genders[randy.nextInt(2)];
                
                // get random preferences
                // KEEP IN MIND: Extra spacing after last hall and wing preferences
                ArrayList<String> halls = new ArrayList<>(Arrays.asList(HALLS));
                
                //remove any ineligible halls
                if (gender.equals("F")) {
                    halls.remove("04");
                    halls.remove("05");
                } else {
                    halls.remove("02");
                    halls.remove("06");
                }
                
                //get five ranked hall preferences
                String[] hallPreferences = new String[5];
                for (int j = 0; j < 5; j++) {
                    int index = randy.nextInt(halls.size());
                    hallPreferences[j] = halls.get(index);
                    halls.remove(index);
                }
                
                ArrayList<String> wings = new ArrayList<>(Arrays.asList(WINGS));   
                String[] wingPreferences = new String[4];
                for (int w = 0; w < WINGS.length; w++) {
                    int index = randy.nextInt(wings.size());
                    wingPreferences[w] = wings.get(index);
                    wings.remove(index);
                }
                
                writer.write(name1 + "," + name2 + "\t");
                writer.write(gender + "\t");
                for (String hall : hallPreferences) {
                    writer.write(hall + " ");
                }
                writer.write("\t");
                
                for (String wing : wingPreferences) {
                    writer.write(wing + " ");
                }
                writer.write("\t");
                
                writer.write(grade[randy.nextInt(3)] + "\t" + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

}
