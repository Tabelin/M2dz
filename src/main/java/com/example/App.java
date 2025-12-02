package com.example;

import com.example.dao.UserDao;
import com.example.dao.UserDaoImpl;
import com.example.entity.User;
import com.example.service.UserService;

import java.util.Scanner;
import java.util.List;
import java.util.Optional;

public class App {

    private static final Scanner scanner = new Scanner(System.in);
    private static final UserDao userDao = new UserDaoImpl();
    private static final UserService userService = new UserService(userDao);

    public static void main(String[] args) {
        System.out.println("=== User Management Console App ===");
        System.out.println("Using Hibernate + PostgreSQL (without Spring)");


        while (true) {
            showMenu();
            int choice = getIntInput("Выберите действие: ");

            switch (choice) {
                case 1 -> createUser();
                case 2 -> readUser();
                case 3 -> updateUser();
                case 4 -> deleteUser();
                case 5 -> listAllUsers();
                case 0 -> {
                    System.out.println("Выход из программы...");
                    return;
                }
                default -> System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private static void showMenu() {
        System.out.println("\n--- Меню ---");
        System.out.println("1. Создать пользователя");
        System.out.println("2. Прочитать пользователя по ID");
        System.out.println("3. Обновить пользователя");
        System.out.println("4. Удалить пользователя");
        System.out.println("5. Показать всех пользователей");
        System.out.println("0. Выход");
    }

    private static void createUser() {
        System.out.print("Введите имя: ");
        String name = scanner.nextLine();

        System.out.print("Введите email: ");
        String email = scanner.nextLine();

        System.out.print("Введите возраст: ");
        Integer age = getIntInput("Возраст: ");

        User user = new User(name, email, age);
        User saved = userService.createUser(user);
        System.out.println("Пользователь создан: " + saved);
    }

    private static void readUser() {
        Long id = getLongInput("Введите ID пользователя: ");
        Optional<User> userOpt = userService.findById(id);
        if (userOpt.isPresent()) {
            System.out.println("Найден пользователь: " + userOpt.get());
        } else {
            System.out.println("Пользователь с ID " + id + " не найден.");
        }
    }

    private static void updateUser() {
        Long id = getLongInput("Введите ID пользователя для обновления: ");
        Optional<User> userOpt = userService.findById(id);
        if (userOpt.isEmpty()) {
            System.out.println("Пользователь не найден.");
            return;
        }

        User user = userOpt.get();
        System.out.print("Новое имя (оставьте пустым, чтобы не менять): ");
        String name = scanner.nextLine();
        if (!name.trim().isEmpty()) user.setName(name);

        System.out.print("Новый email (оставьте пустым, чтобы не менять): ");
        String email = scanner.nextLine();
        if (!email.trim().isEmpty()) user.setEmail(email);

        System.out.print("Новый возраст (оставьте пустым, чтобы не менять): ");
        String ageStr = scanner.nextLine();
        if (!ageStr.trim().isEmpty()) {
            try {
                user.setAge(Integer.parseInt(ageStr));
            } catch (NumberFormatException e) {
                System.out.println("Некорректный возраст, оставлен прежний.");
            }
        }

        User updated = userService.updateUser(user);
        System.out.println("Пользователь обновлен: " + updated);
    }

    private static void deleteUser() {
        Long id = getLongInput("Введите ID пользователя для удаления: ");
        userService.deleteUser(id);
        System.out.println("Пользователь с ID " + id + " удален.");
    }

    private static void listAllUsers() {
        List<User> users = userService.findAll();
        if (users.isEmpty()) {
            System.out.println("Нет ни одного пользователя.");
        } else {
            System.out.println("Список всех пользователей:");
            users.forEach(System.out::println);
        }
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Введите корректное число.");
            }
        }
    }

    private static Long getLongInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Введите корректное число.");
            }
        }
    }
}