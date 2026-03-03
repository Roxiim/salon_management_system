package com.roxana.salonoop.DEV;

import com.roxana.salonoop.repository.UserRepository;

public class DBInitialize {

    public static void main(String[] args) {

        UserRepository repo = new UserRepository();

        repo.createUser("Roxana", "Roxana123", "admin");
        repo.createUser("Maria", "Maria123", "customer");
        repo.createUser("Anca", "Anca123", "customer");
        repo.createUser("Marian", "Marian123", "customer");
        repo.createUser("Mihai Tudor", "abc123", "stylist");
        repo.createUser("Costean Razvan", "abc123", "stylist");
        repo.createUser("Florian Cristina", "abc123", "stylist");
        repo.createUser("Morar Ana", "abc123", "stylist");
        repo.createUser("Popescu Alexandru", "abc123", "stylist");
        repo.createUser("Muresan Andreea", "abc123", "stylist");
        repo.createUser("Popa Corina", "abc123", "stylist");
        repo.createUser("Pop Denisa", "abc123", "stylist");
        repo.createUser("Malai Ionela", "abc123", "stylist");


        System.out.println("Users inserted successfully!");
    }
}
