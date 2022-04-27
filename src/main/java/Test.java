import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(1);
            }
        });
        Thread t1 = new Thread(t);
        t1.start();
        ArrayList<Collection> integers = new ArrayList<Collection>();

        int y = 1;
        int u = 2;
        String s= "j";
        String s1 = new String("j");
        System.out.println(s == s1);
        ArrayList<String> list = new ArrayList<>(Arrays.asList("A", "B,","A"));
        System.out.println(list);
        list.remove("A");
        System.out.println(list);



        int[] a = new int[10];
        a[20] = 10;
        a[5] = a[2]/0;

    }



}
