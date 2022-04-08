package lesson3.phone_book;

public class Contact {
    private String surname;
    private String phoneNum;

    public Contact(String surname, String phoneNum) {
        this.surname = surname;
        this.phoneNum = phoneNum;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "surname='" + surname + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                "}";
    }
}
