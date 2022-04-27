package lesson8;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamApp {
    public static void main(String[] args) {
        Random random = new Random();
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(random.nextInt(100));
        }
        System.out.println(list);

        Stream<Integer> stream = list.stream(); // по аналогии с итератором
        long count = list.stream()
                .skip(50)
                //.peek(integer -> System.out.println(integer))
                .filter(integer -> integer % 2 == 0)
                .count();
        System.out.println(count);

        list.stream()
                .filter(integer -> integer % 2 == 0)
                .max(Comparator.naturalOrder()).get(); //почитать про компаратор


        List<Integer> list2 = list.stream()
                .filter(integer -> integer % 2 == 1)
                .map(integer -> integer * 1000)
                .collect(Collectors.toList());
        System.out.println(list2);
                //.forEach(listElem -> System.out.println(listElem));

        String str = list.stream()
                .filter(integer -> integer % 2 == 0)
                .sorted() //тяжёлые для памяти
                .distinct() //тяжёлые для памяти
                .map(integer -> integer * 1000)
                .map(integer -> String.valueOf(integer))
                .collect(Collectors.joining("<->"));
        System.out.println(str);

        List<Integer> list3 = new ArrayList<>();
    }
}
