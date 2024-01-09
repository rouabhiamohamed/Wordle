package backend;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Dico {
    public String[] lireFichier() {
        //array to put the lines of the files
        ArrayList<String> lignes = new ArrayList<>();
        try {
            //create a File object representing the file "liste.txt"
            File fichier = new File("./src/main/resources/liste.txt");
            //read from the file
            Scanner scanner = new Scanner(fichier);
            //read each line from the file and add it to the ArrayList
            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                lignes.add(ligne);
            }
//close the scanner
            scanner.close();
            // Print the stack trace if an exception occurs.
        } catch (Exception e) {
            e.printStackTrace();
        }
        // convert the arrayList to an array of strings and return it
        return lignes.toArray(new String[0]);
    }
}




