import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

public class Test {
    public static void main(String[] args){
        Hashtable<Integer,Integer> table=new Hashtable();
        ArrayList<Integer> result=new ArrayList<Integer>();
        Random random=new Random();
        while(result.size()<=80){
            int a=random.nextInt(81);
            if(table.getOrDefault(a,0)==0){
                result.add(a);
                table.put(a,1);
            }
        }
        for(Object i:result.toArray()){
            System.out.println((int)i);
        }
        System.out.println(result.toString());
    }
}
