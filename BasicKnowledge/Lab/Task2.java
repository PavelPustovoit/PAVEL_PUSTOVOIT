package Lab;

import java.util.ArrayList;
import java.util.Arrays;


public class Task2 {
    public static String first_non_repeating_letter(String word){
        ArrayList<String> latter_array = new ArrayList<String>(Arrays.asList(word.split("")));
        for (String string : latter_array) {
            if (latter_array.stream().filter(string::equals).count() == 1){
                return string;
            }
        }
        return "None";
    }
}
