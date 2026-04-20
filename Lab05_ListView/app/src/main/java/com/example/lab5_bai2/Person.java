package com.example.lab5_bai2;

public class Person {
    private final String name;
    private final int avatarResId;

    public Person(String name, int avatarResId) {
        this.name = name;
        this.avatarResId = avatarResId;
    }

    public String getName() {
        return name;
    }

    public int getAvatarResId() {
        return avatarResId;
    }
}
