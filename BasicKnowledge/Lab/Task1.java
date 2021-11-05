package Lab;
import java.util.ArrayList;
import java.util.List;

public class Task1{
    public static List<Integer> getIntegersFromList(List<Object> collection){
        List<Integer> list = new ArrayList<Integer>();
        collection.stream().filter(o -> ((Object) o).getClass().equals(Integer.class)).forEach(o -> list.add((int)o));
        return list;
    }
}
