package ru.greenatom;

import ru.greenatom.DAO.Dao;
import ru.greenatom.DAO.PostgreSqlDao;
import ru.greenatom.model.User;


import java.sql.Timestamp;
import java.util.Collection;
import java.util.Optional;

public class Main {

    private static final Dao<User, Integer> USER_DAO = new PostgreSqlDao();

    public static void main(String[] args) {

        // Добавление в базу
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        User firstUser =
                new User("Petr", "Petrov", "petrov@mail.com", 28,  timestamp);
        User secondUser =
                new User("Vladimir", "Sidorov", "sidorov@gmail.com", 30, timestamp);
        User thirdUser =
                new User("Vasya", "Petrov", "pv@gmail.com", 30, timestamp);
        User fourthUser =
                new User("Nikolay", "Belov", "pv@gmail.com", 28, timestamp);
        addUser(firstUser).ifPresent(firstUser::setId);
        addUser(secondUser).ifPresent(secondUser::setId);
        addUser(thirdUser).ifPresent(thirdUser::setId);
        addUser(fourthUser).ifPresent(fourthUser::setId);

        // Чтение из бд
        getAllUsers().forEach(System.out::println);
        getAllUsers("FirstName", "'Vasya'").forEach(System.out::println);
        getAllUsers("age", 28).forEach(System.out::println);

        // Обновление данных
        firstUser.setFirstName("Ivan");
        firstUser.setLastName("Ivanov");
        firstUser.setEmail("ivanov@yandex.ru");
        updateUser(firstUser);

        // Удаление из бд
        deleteCustomer(secondUser);

    }

    public static Optional<Integer> addUser(User user) {
        return USER_DAO.create(user);
    }

    public static void updateUser(User user) {
        USER_DAO.update(user);
    }

    public static Collection<User> getAllUsers() {
        return USER_DAO.getAll();
    }

    public static <F, V> Collection<User> getAllUsers(F field, V value) {
        return USER_DAO.getAll(field, value);
    }

    public static void deleteCustomer(User user) {
        USER_DAO.delete(user);
    }
}