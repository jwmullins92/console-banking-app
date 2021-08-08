package com.revature.presentation;

import com.revature.collection.RevArrayList;
import com.revature.model.Account;
import com.revature.model.User;
import com.revature.service.AccountService;
import com.revature.service.AccountServiceImpl;

import java.util.Scanner;

public class AccountPresentationImpl implements AccountPresentation{

    private AccountService service;

    public AccountPresentationImpl() {
        service = new AccountServiceImpl();
    }

    public void openNewAccountMenu(User user) {
        Scanner sc = new Scanner(System.in);
        System.out.println("CREATING NEW ACCOUNT FOR " + user.getUsername() + "\n");
        System.out.println("What type of account would you like to create?");
        System.out.println("1) Checking");
        System.out.println("2) Savings");
        System.out.println("0) Return to User Menu");

        if(sc.hasNextInt()) {
            String accountType;
            int choice = sc.nextInt();
            switch (choice) {
                case 0:
                    return;
                case 1:
                    accountType = "Checking";
                    service.createAccount(user, accountType);
                    break;
                case 2:
                    accountType = "Savings";
                    service.createAccount(user, accountType);
                    break;
                case 3:
                    //TODO fix feedback
                    System.out.println("Invalid Input. Please enter 0, 1, or 2");
                    openNewAccountMenu(user);
                    break;
            }
        } else {
            //TODO fix feedback
            System.out.println("Invalid Input");
            openNewAccountMenu(user);
        }
        System.out.println("Successfully created new account!");
    }

    @Override
    public void loadUserAccountsIndex(User user) {
        Scanner sc = new Scanner(System.in);
        RevArrayList<Account> accounts = service.getAccountByUser(user);

        if(accounts.size() == 0) {
            System.out.println("You do not have any accounts");
            return;
        }
        System.out.println("Which account would you like to view?");
        for(int i = 1; i < accounts.size() + 1; i++) {
            System.out.println(i + ") " + accounts.get(i - 1).getType() + " " + accounts.get(i - 1).getAccountNumber());
        }
        System.out.println("0) Return to user menu");

        if(sc.hasNextInt()) {
            int choice = sc.nextInt();

            if(choice == 0) {
                System.out.println("Returning to User Menu");
                return;
            }

            for(int i = 1; i < accounts.size() + 1; i++) {
                if(choice == i) {
                    loadAccountMenu(accounts.get(i - 1));
                }
            }
        } else {
            //TODO Clean up feedback
            System.out.println("Invalid Input");
        }
        loadUserAccountsIndex(user);
    }

    public void loadAccountMenu(Account account) {
        Scanner sc = new Scanner(System.in);
        System.out.println(account.getType() + " account menu\n");
        System.out.println("What would you like to do?");
        System.out.println("1) Check Balance");
        System.out.println("2) Make Deposit");
        System.out.println("3) Make Withdrawal");
        System.out.println("0) Return to all accounts");

        if(sc.hasNextInt()) {
            int choice = sc.nextInt();
            switch(choice) {
                case 0:
                    System.out.println("Returning to all accounts");
                    return;
                case 1:
                    //TODO getting the account balance may not work correctly
                    System.out.printf("Current Balance: $%,.2f %n", service.getBalanceByAccount(account));
                    break;
                case 2:
                    System.out.println("How much would you like to deposit?");
                    if(sc.hasNextDouble()) {
                        double depositAmount = sc.nextDouble();
                        service.makeDeposit(account, depositAmount);
                    } else {
                        //TODO clean up error feedback
                        System.out.println("Invalid deposit amount");
                    }
                    break;
                case 3:
                    System.out.println("How much would you like to withdrawal");
                    if(sc.hasNextDouble()) {
                        double wAmount = sc.nextDouble();
                        service.makeWithdrawal(account, wAmount);
                    } else {
                        //TODO clean up error feedback
                        System.out.println("Invalid withdrawal amount");
                    }
                    break;
                default:
                    System.out.println("Invalid input");
                    break;
            }
        } else {
            //TODO clean up feedback
            System.out.println("Invalid account menu choice");
        }

        loadAccountMenu(account);
    }

}
