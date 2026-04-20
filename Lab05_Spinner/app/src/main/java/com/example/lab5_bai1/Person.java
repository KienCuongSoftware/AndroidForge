package com.example.lab5_bai1;

public class Person {
    private final int imagePerson;
    private final String namePerson;

    public Person(int imagePerson, String namePerson) {
        this.imagePerson = imagePerson;
        this.namePerson = namePerson;
    }

    public int getImagePerson() {
        return imagePerson;
    }

    public String getNamePerson() {
        return namePerson;
    }
}
