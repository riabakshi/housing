import java.util.Scanner;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;

/**
 * Writes random information to files.
 * 
 * preferences.txt
 * fn1 ln1,fn2 ln2  gender  grade   importance friend1 friend2 h1 w1 w2    h2 w1 w2    h3  h4  roomType q1, q2
 * 
 * roomtype: 
 * standard, l-room, quad, hypoallergenic
 *
 * @author Ria Bakshi
 * @version 2/16/25
 */
public class Writer
{
    // instance variables - replace the example below with your own
    private int x;

    /**
     * Constructor for objects of class Writer
     */
    public Writer()
    {
        // initialise instance variables
        x = 0;
    }

    
    /**
     * Write random information to the preferences.txt data file. 
     * This was only called to create random preference data.
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
