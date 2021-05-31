import java.util.*;
import java.io.*;

public class BM {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Please call this program with " +
                               "two arguments which is the input file name " +
                               "and the string to search.");
        } else {
            try {
                Scanner s = new Scanner(new File(args[0]));

                // Read the entire file into one String.
                StringBuilder fileText = new StringBuilder();
                while (s.hasNextLine()) {
                    fileText.append(s.nextLine() + "\n");
                }

                System.out.println(search(fileText.toString(), args[1]));
            } catch (FileNotFoundException e) {
                System.out.println("Unable to find file called " + args[0]);
            }
        }
    }

    /**
     * Perform BM substring search on the given text with the given pattern.
     * 
     * This should return the starting index of the first substring match if it
     * exists, or -1 if it doesn't.
     */
    public static int search(String pattern, String text) {
        // TODO
        return -1;
    }
}
