package com.roxana.salonoop.DEV;

import com.roxana.salonoop.repository.UserRepository;

public class initialize1 {
    public static void main(String[] args) {

        UserRepository repo = new UserRepository();

        repo.createUser("Marginean Ramona", "Ramona123", "stylist");
    }
}

