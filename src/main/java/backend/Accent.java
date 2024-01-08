package backend;
import java.text.Normalizer;

public class Accent {
    public static String removeAccents(CharSequence input) {
        //Normalize the input text using NFC(une forme de normalisaiton pour l'unicode ) it standarize the unicode character in it normalized form
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFC);
        // Replace specific accented characters with their unaccented counterparts
        normalized = normalized.replaceAll("[éèêë]", "e");
        normalized = normalized.replaceAll("[àâ]", "a");
        normalized = normalized.replaceAll("[ôö]", "o");
        normalized = normalized.replaceAll("[îï]", "i");
        normalized = normalized.replaceAll("[ûü]", "u");
//return the character without the accent
        return normalized;
    }

}
