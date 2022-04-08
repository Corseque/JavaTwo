package lesson3.phone_book;

public class PhoneBookApp {

    /*
    Написать простой класс Телефонный Справочник, который хранит в себе список фамилий и
    телефонных номеров. В этот телефонный справочник с помощью метода add() можно
    добавлять записи, а с помощью метода get() искать номер телефона по фамилии. Следует
    учесть, что под одной фамилией может быть несколько телефонов (в случае однофамильцев),
    тогда при запросе такой фамилии должны выводиться все телефоны. Желательно не добавлять
    лишний функционал (дополнительные поля (имя, отчество, адрес), взаимодействие с пользователем
    через консоль и т.д). Консоль использовать только для вывода результатов проверки телефонного
    справочника.
    */

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        phoneBook.add("Ivanov", "+7-000-333-22-11");
        phoneBook.add("Petrov", "+7-123-999-22-11");
        phoneBook.add("Sidorov", "+7-000-383-89-11");
        phoneBook.add("Smirnov", "+7-111-999-22-99");
        phoneBook.add("Baranov", "+7-235-888-27-18");
        phoneBook.add("Ivanov", "+7-222-555-66-44");
        phoneBook.add("Ivanov", "+7-222-555-66-44");

        System.out.println(phoneBook);

        String someName = "Ivanov";
        phoneBook.get(someName);

        System.out.println();
        someName = "Sidorov";
        phoneBook.get(someName);

        System.out.println();
        someName = "Nikolaev";
        phoneBook.get(someName);
    }
}
