package lesson3.phone_book;

import java.util.*;

public class PhoneBook {
//public class PhoneBook extends HashMap {
    private Contact contact;
    private final List phoneBook = new ArrayList();

    public PhoneBook() {
    }

//    public void add(String surname, String phoneNum) {
//        this.put(phoneNum, surname);
//    }

    public void add(String surname, String phoneNum) {
        phoneBook.add(new Contact(surname, phoneNum));
    }
/*
    public void get(String surname) {
        ArrayList<String> phonesBySurname = new ArrayList<>();
        Set<Entry<String, String>> set = this.entrySet();
        for (Map.Entry<String, String> entry : set) {
            String someSurname = entry.getValue();
            if (someSurname == surname) {
                phonesBySurname.add(entry.getKey());
            }
        }
        if (phonesBySurname.isEmpty()) {
            System.out.println("Контактов по фамилиии " + name + " не найдено." );
        } else {
            System.out.println("Перечень телефонов контактов по фамилиии " + name + ":");
            System.out.println(phonesBySurname);
        }
    }
 */

    public void get(String surname) {
        ArrayList<String> phonesBySurname = new ArrayList<>();
        Iterator<Contact> iterator = phoneBook.iterator();
        while (iterator.hasNext()) {
            Contact contactFromPhoneBook = iterator.next();
            String surnameFromPhoneBook = contactFromPhoneBook.getSurname();
            if (surnameFromPhoneBook == surname) {
                phonesBySurname.add(contactFromPhoneBook.getPhoneNum());
            }
        }
            if (phonesBySurname.isEmpty()) {
                System.out.println("Контактов по фамилиии " + surname + " не найдено." );
            } else {
                System.out.println("Перечень телефонов контактов по фамилиии " + surname + ":");
                System.out.println(phonesBySurname);
            }
    }

    @Override
    public String toString() {
        String printList = "!";
//        this.forEach(contact -> System.out.println(contact.toString()));
        Iterator<Contact> iterator = phoneBook.iterator();
        while (iterator.hasNext()) {
            printList = printList + iterator.next().toString() + "\n";
        }
        return printList;
    }
}
