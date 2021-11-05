package Lab;
import java.util.stream.IntStream;

public class Task3 {
    public static Integer digital_root(Integer number){
        int sum = number;
        do{
            int[] numbers = IntStream.iterate(sum, i -> i > 0, i -> i / 10).map(i -> i % 10).toArray();
            sum = IntStream.of(numbers).sum();
        }while (sum > 9);
        return sum;
    }
}
