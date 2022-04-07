package lesson3;

import java.util.*;

public class CollectionsApp {

    /*
    Создать массив с набором слов (10-20 слов, должны встречаться повторяющиеся). Найти и
    вывести список уникальных слов, из которых состоит массив (дубликаты не считаем).
    Посчитать, сколько раз встречается каждое слово.
    */

    public static void main(String[] args) {
        String[] srt = {"January", "February", "March", "May", "June",
                "July", "August", "September", "October", "November",
                "December", "June", "July", "January", "January"};

        System.out.println(Arrays.asList(srt));
        
//        Map<String, Integer> wordsFromSrt = new LinkedHashMap<>();
//        wordsFromSrt = quantityOfUniqueWordInStr(wordsFromSrt, srt);

        Set<Map.Entry<String, Integer>> set = quantityOfUniqueWordInStr(new LinkedHashMap<>(), srt).entrySet(); // как так работает без указания типа K и V?
        for (Map.Entry<String, Integer> entry : set) {
            System.out.println("Слово " + entry.getKey() + " встречается " + entry.getValue() + " раз.");
        }
    }

    private static Map quantityOfUniqueWordInStr(Map map, String[] srt) {
        for (int i = 0; i < srt.length; i++) {
            map.put(srt[i], map.containsKey(srt[i]) ? (int) map.get(srt[i]) + 1 : 1);
        }
        return map;
    }


}
