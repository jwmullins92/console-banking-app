package com.revature.presentation;

import com.revature.collection.RevArrayList;
import com.revature.model.User;
import com.revature.service.UserService;
import com.revature.service.UserServiceImpl;

import java.util.Scanner;

public class UserPresentationImpl implements UserPresentation{

    private UserService service;

    public UserPresentationImpl() {
        service = new UserServiceImpl();
    }

    public void loadMainMenu() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("\nMAIN MENU");
        System.out.println("==========");
        System.out.println("What would you like to do?");
        System.out.println("1) Login");
        System.out.println("2) Register User Account");
        System.out.println("0) Close App");

        if(scanner.hasNextInt()) {
            int choice = scanner.nextInt();
            switch (choice) {
                case 0:
                    System.out.println("Goodbye");
                    return;
                case 1:
                    User user = login();
                    if(user != null) {
                        System.out.println("\nWelcome " + user.getUsername() + "!");
                        loadUserMenu(user);
                    } else {
                        System.out.println("Returning to Main Menu");
                    }
                    break;
                case 2:
                    registerNewUser();
                    break;
                default:
                    //TODO Better feedback when app built out
                    System.out.println("ERROR FEEDBACK");
                    break;
            }
        } else {
            //TODO Better feedback when app built out
            System.out.println("ERROR FEEDBACK");
        }
        loadMainMenu();
    }

    public void loadUserMenu(User user) {

        AccountPresentation accountP = new AccountPresentationImpl();

        Scanner sc = new Scanner(System.in);
        System.out.println("\nUSER MENU");
        System.out.println("==========");
        System.out.println("What would you like to do?");
        System.out.println("1) View Accounts");
        System.out.println("2) Open a new Bank Account");
        System.out.println("3) Delete User Account");
        System.out.println("0) Logout");

        if(sc.hasNextInt()) {
            int choice = sc.nextInt();
            switch(choice){
                case 0:
                    return;
                case 1:
                    accountP.loadUserAccountsIndex(user);
                    break;
                case 2:
                    accountP.openNewAccountMenu(user);
                    break;
                case 3:
                    boolean success = loadDeletePath(user);
                    if(success) {
                        return;
                    }
                    break;
                default:
                    System.out.println("Invalid input");
                    break;
            }
        } else {
            //TODO fix error feedback with default above as well
            System.out.println("Invalid input");
        }
        loadUserMenu(user);
    }

    //TODO Create better exit options
    //TODO Move to User Service
    public User login() {
        User user = null;
        RevArrayList<User> allUsers = service.getAllUsers();
        String username = null;
        String password = null;

        Scanner sc = new Scanner(System.in);
        System.out.println("\nPLEASE ENTER YOUR CREDENTIALS OR ENTER '0' TO RETURN TO MAIN MENU");

        System.out.print("\nEnter your username: ");

        if(sc.hasNextInt()) {
            if(sc.nextInt() == 0) return null;
            //TODO clean up feedback
            System.out.println("Invalid input");
            login();
        }

        if(sc.hasNextLine()) {
            username = sc.nextLine();
            user = service.getUserByUsername(username);
        } else {
            //TODO cleanup error feedback
            System.out.println("Invalid input");
            login();
        }

        System.out.print("Enter your password: ");
        if(sc.hasNextLine()) {
            password = sc.nextLine();
        } else {
            //TODO cleanup error feedback
            System.out.println("Invalid input");
            login();
        }

        if(user != null && user.getPassword().equals(password)) {
            return user;
        } else {
            //TODO cleanup feedback
            System.out.println("Incorrect username or password");
            login();
        }

        return null;
    }

    //TODO Move to User Service
    //TODO NEEDS TO VERIFY USERNAME IS UNIQUE. Currently getting exception
    public boolean registerNewUser() {

        RevArrayList<User> allUsers = service.getAllUsers();

        String username = null;
        String password = null;

        Scanner scanner = new Scanner(System.in);
        System.out.println("\nREGISTERING NEW USER");
        System.out.print("Enter a username: ");


        if(scanner.hasNextLine()) {
            //TODO Verify username is unique
            username = scanner.nextLine();
            for(int i = 0; i < allUsers.size(); i++) {
                if(username.equals(allUsers.get(i).getUsername())) {
                    System.out.println("\nSorry, that username is already taken.");
                    return false;
                }
            }
        } else {
            //TODO Give better error feedback when more fully built out
            System.out.println("Unexpected Error");
            return false;
        }

        System.out.print("Enter a password: ");

        if(scanner.hasNextLine()) {
            password = scanner.nextLine();
        } else {
            //TODO Give better error feedback when more fully built out
            System.out.println("Unexpected Error");
            return false;
        }
        //TODO two-arg User constructor used
        User newUser = new User(username, password);
        service.createUser(newUser);
        return true;
    }

    public void displayAllUsers() {
        RevArrayList<User> allUsers = service.getAllUsers();
        for(int i = 0; i < allUsers.size(); i++) {
            System.out.println("User: " + allUsers.get(i).getUsername());
        }
    }

    public boolean loadDeletePath(User user) {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n***WARNING: Deleting your User Profile will permanently delete all your accounts and transaction history.");
        System.out.println("\nDo you still wish to proceed? y/n");

        if(sc.hasNextLine()) {
            String choice = sc.nextLine();
            switch (choice) {
                case "y":
                    System.out.print("Enter your username to permanently delete your profile: ");
                    if(sc.hasNextLine()) {
                        String username = sc.nextLine();
                        if(username.equals(user.getUsername())) {
                            service.deleteUser(user);
                            System.out.println("Successfully deleted User");
                            return true;
                        } else {
                            //TODO clean up feedback
                            System.out.println("Could not delete user");
                        }
                    } else {
                        //TODO clean up feedback
                        System.out.println("Invalid input");
                    }
                    break;
                case "n":
                    System.out.println("Returning to User Menu...");
                    return false;
                default:
                    //TODO clean up feedback
                    System.out.println("Invalid input");
                    break;
            }
        } else {
            //TODO clean up feedback
            System.out.println("Invalid input");
        }
        return false;
    }

    @Override
    public void displayUser(String username) {

    }
}
