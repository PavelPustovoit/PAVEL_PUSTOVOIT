package Lab;

import java.util.ArrayList;
import java.util.stream.IntStream;


public class Task4 {
    public static int sum_pair(int[] arr, Integer target){
        ArrayList<Integer> pairs = new ArrayList<Integer>();
        IntStream.range(0,  arr.length)
            .forEach(i -> IntStream.range(0,  arr.length)
            .filter(j -> i != j && arr[i] + arr[j] == target)
            .forEach(j -> pairs.add(arr[j]))
                );
        int count = pairs.size()/2;
        return count;
    }
}
