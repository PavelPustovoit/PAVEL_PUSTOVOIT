package Lab;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        
        System.out.println("Task 1:");
        System.out.println(Task1.getIntegersFromList(Arrays.asList(1, "a", 2, "b")));
        System.out.println(Task1.getIntegersFromList(Arrays.asList(1,  2, "a", "b", 0, 15)));
        System.out.println(Task1.getIntegersFromList(Arrays.asList(1,  2, "a", "b", "asdf", "15", 15)));
        
        System.out.println("\nTask 2:");
        String[] str_for_task2 = {"stress", "sTress", "sttreress"};
        for (String string : str_for_task2) {
            System.out.println(Task2.first_non_repeating_letter(string));
        }

        System.out.println("\nTask 3:");
        Integer[] int_for_task3 = {16, 942, 132189, 493193};
        for (Integer integer : int_for_task3) {
            System.out.println(Task3.digital_root(integer));
        }
        
        System.out.println("\nTask 4:");
        int[] arr = new int[]{1, 3, 6, 2, 2, 0, 4, 5};
        System.out.println(Task4.sum_pair(arr, 5)) ;
        System.out.println(Task4.sum_pair(arr, 4)) ;
        
        System.out.println("\nTask 5:");
        Task5.meeting("Fred:Corwill;Wilfred:Corwill;Barney:Tornbull;Betty:Tornbull;Raphael:Corwill;Alfred:Corwill");
    }
}
