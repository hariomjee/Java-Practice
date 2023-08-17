package org.example;

import java.util.Scanner;

import static org.example.StringCompession.compession;

public class Main {
    public static void main(String[] args) {
       // System.out.println("Hello world!");
        Scanner sc= new Scanner(System.in);
        String input= sc.nextLine();
        System.out.println("output is ######"+" "+compession(input));
    }
}