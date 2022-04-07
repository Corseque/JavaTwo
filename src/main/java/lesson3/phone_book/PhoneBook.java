package lesson3.phone_book;

import java.util.*;

public class PhoneBook extends HashMap {

    public PhoneBook() {
    }

    public void add(String name, String phoneNum) {
        this.put(phoneNum, name);
    }

    public void get(String name) {
        ArrayList<String> phonesByName = new ArrayList<>();
        Set<Entry<String, String>> set = this.entrySet();
        for (Map.Entry<String, String> entry : set) {
            String someName = entry.getValue();
            if (someName == name) {
                phonesByName.add(entry.getKey());
            }
        }
        if (phonesByName.isEmpty()) {
            System.out.println("Контактов по фамилиии " + name + " не найдено." );
        } else {
            System.out.println("Перечень телефонов контактов по фамилиии " + name + ":");
            System.out.println(phonesByName);
        }

    }
}
