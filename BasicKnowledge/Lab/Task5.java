package Lab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Task5 {
    public static void meeting(String s){
        System.out.printf("Input: %s\n", s);
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(s.toUpperCase().split(";")));
        System.out.printf("Output: %s\n", list.stream().map(i -> convert((String)i)).sorted().collect(Collectors.joining()));   
    }
    public static String convert(String s){
        String[] name = s.split(":");
        return String.format("(%s,%s)", name[1], name[0]);
    }
}
