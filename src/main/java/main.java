import backend.Hint;
import backend.Partie;

// --module-path "./javafx/lib" --add-modules javafx.controls,javafx.fxml
// Dans run / edit configuration / edit vm truc

public class main {
    public static void main(String [] args){

        //Example of hint request
        //Don't forget to boot the server though if you're expecting an answer lol
        System.out.println(Hint.getOneHint("arbre"));

        Partie game = new Partie();
        game.jouer();




    }


}
