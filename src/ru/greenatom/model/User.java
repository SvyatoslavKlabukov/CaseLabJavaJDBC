package ru.greenatom.model;

import java.sql.Timestamp;
import java.time.Instant;

public class User {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private Integer age;
    private Timestamp eventDate;

    public User(String firstName) {
        this.firstName = firstName;
    }

    public User(String firstName, String lastName, String email, Integer age, Timestamp eventDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
        this.eventDate = eventDate;
    }

    public User(Integer id, String firstName, String lastName, String email, Integer age, Timestamp eventDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
        this.eventDate = eventDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Timestamp getEventDate() {
        return eventDate;
    }

    public void setEventDate(Timestamp eventDate) {
        this.eventDate = eventDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", eventDate=" + eventDate +
                '}';
    }
}
