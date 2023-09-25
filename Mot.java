import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Mot {
    public static void main(String [] args){
        ArrayList<String> tableauDeMots = new ArrayList<>();
int count=0;
        Scanner diff = new Scanner(System.in);
        String level;

        while (true) {
            System.out.println("Choisissez la difficulté :");
            System.out.println("Facile pour un mot de 5 lettres (appuyez sur 'f')");
            System.out.println("Moyen pour un mot de 8 lettres (appuyez sur 'm')");
            System.out.println("Difficile pour un mot de 10 lettres (appuyez sur 'd')");

            level = diff.nextLine();

            if (level.equals("f") || level.equals("m") || level.equals("d")) {
                break; // Exit the loop when a valid level is entered
            } else {
                System.out.println("Choix invalide. Veuillez choisir 'f', 'm' ou 'd'.");
            }
        }

        // You can now use the 'level' variable to proceed with the selected difficulty.
        System.out.println("Vous avez choisi la difficulté : " + level);


        try {
            String url = "https://fr.wiktionary.org/wiki/Wiktionnaire:Liste_de_1750_mots_fran%C3%A7ais_les_plus_courants";
            Document document = Jsoup.connect(url).get();
            Elements links = document.select(" p:contains(Noms) > a[href], p:contains(Adverbes) > a[href], p:contains(Adjectifs) > a[href] ,p:contains(Verbes) > a[href],p:contains(Prépositions) > a[href]");

            for (Element link : links) {
                String linkText = link.text();
if (level =="f"){
                if(linkText.length()==5) {
                    tableauDeMots.add(linkText);
                }}
                if (level =="m"){
                    if(linkText.length()==8) {
                        tableauDeMots.add(linkText);
                    }}
                if (level =="d"){
                    if(linkText.length()==10) {
                        tableauDeMots.add(linkText);
                    }}



                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] tableauFinal = tableauDeMots.toArray(new String[0]);

        for (String Text : tableauFinal) {

          // System.out.println(Text);
        }
        System.out.println(count);

        Random random = new Random();
        int index = random.nextInt(tableauFinal.length);
       String randomValue = tableauFinal[index];
        System.out.println(randomValue);
        String example=tableauFinal[index];
        // String example= "mouiller";

        int[] couleurMot = new int[example.length()];
        Scanner mot = new Scanner(System.in);
        System.out.println("entrer votre mot");

        String mot_entre = mot.nextLine();
        System.out.println("le nom entrer : " + mot_entre);

        for(int i=0;i<=mot_entre.length()-1;i++)
        {
            for (int j = 0; j <= example.length() - 1; j++) {


                //vert
                if (mot_entre.charAt(i) == example.charAt(j) && i == j) {
                    couleurMot[i] = 2;

                    break;
                }

            }}
        for(int i=0;i<=mot_entre.length()-1;i++)
        {
            for (int j = 0; j <= example.length() - 1; j++) {
                //vert
                if (mot_entre.charAt(i) == example.charAt(j) && i != j && couleurMot[i]!=2  && couleurMot[j]!=2 ) {
                    couleurMot[i] = 1;
                }

            }}
        for(int i=0;i<=mot_entre.length()-1;i++)
        {
            int currentCharColor = couleurMot[i];
            if (currentCharColor == 1  ){
                System.out.println("orange"+i);}
            else if (currentCharColor == 2 ){
                System.out.println("vert"+i);}
            else
            {System.out.println("gris"+i);}

        }



    }


}
