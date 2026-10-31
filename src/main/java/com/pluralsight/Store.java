package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

/**
 * Starter code for the Online Store workshop.
 * Learners will complete the TODO sections to make the program work.
 * <p>
 * What is already done for you: the two lists, the Home Screen loop and
 * the method headers. Each TODO says what that method has to do.
 * Product.java is complete; products.csv sits at the project root.
 * You will need extra imports (for example java.io.BufferedReader,
 * java.io.FileReader, java.io.IOException) as you fill the TODOs in.
 */
public class Store {

    public static void main(String[] args) {

        // Create lists for inventory and the shopping cart
        ArrayList<Product> inventory = new ArrayList<>();
        ArrayList<Product> cart = new ArrayList<>();
        ArrayList<Product> counter = new ArrayList<>();

        // Load inventory from the data file (pipe-delimited: SKU|Product Name|Price|Department)
        loadInventory("products.csv", inventory);

        // Main menu loop
        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        while (choice != 3) {
            System.out.println("\nWelcome to the Online Store!");
            System.out.println("1. Show Products");
            System.out.println("2. Show Cart");
            System.out.println("3. Exit");
            System.out.print("Your choice: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Please enter 1, 2, or 3.");
                scanner.nextLine();                 // discard bad input
                continue;
            }
            choice = scanner.nextInt();
            scanner.nextLine();                     // clear newline

            switch (choice) {
                case 1 -> displayProducts(inventory, cart, scanner);
                case 2 -> displayCart(cart, scanner);
                case 3 -> System.out.println("Thank you for shopping with us!");
                default -> System.out.println("Invalid choice!");
            }
        }
        scanner.close();
    }

    /**
     * Reads product data from a file and populates the inventory list.
     * File format (pipe-delimited), the first line is a header row:
     * SKU|Product Name|Price|Department
     * <p>
     * Example line:
     * AV1051|JBL Bluetooth Speaker|89.95|Audio Video
     */
    public static void loadInventory(String fileName, ArrayList<Product> inventory) {
        // TODO: skip the header line, then read each line, split on "|",
        //       create a Product object, and add it to the inventory list
        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("products.csv"));
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("\\|");
                String sku = tokens[0];
                String name = tokens[1];
                double price = Double.parseDouble(tokens[2]);
                String department = tokens[3];
                inventory.add(new Product(sku, name, price, department));

                //  inventory.put(id, new Product(id, name, price));
                //inventory.put
            }

        } catch (Exception e) {
            System.err.println(e);
            System.out.println(e.getCause());
        }

    }

    /**
     * Displays all products and lets the user add one to the cart.
     * Typing X returns to the main menu.
     */
    public static void displayProducts(ArrayList<Product> inventory, ArrayList<Product> cart, Scanner scanner) {
        // TODO: show each product (id, name, price),
        //       prompt for an id, find that product, add to cart
        System.out.println("========================= INVENTORY =========================");
        //// looked this format up online to set the table in this organized format
        System.out.printf("%-14s %-38s %-9s %-15s%n", "Product ID", "Product Name", "Price", "Department");

        System.out.println("-----------------------------------------------------------------------");
        /// a for loop to get through every item in inventory
        for (Product product : inventory) {
            // printf statement to organize everything
            System.out.printf("%-14s %-38s $%-8.2f %-15s%n", product.getSku(), product.getName(), product.getPrice(), product.getDepartment());

        }

        System.out.println("================================================================");
        ///after organizing, asking what to do next
        boolean isDone = false;
        while (!isDone) {
            System.out.print("S - Search Product ==> A - Add to Cart ==> B - Back: ");
            String input = scanner.nextLine();
            //if they choose A, then ask the sku
            if (input.equalsIgnoreCase("S")) {
                System.out.println("Enter the SKU of the product you want to find");
                String id = scanner.nextLine();
                ///run the method by the input, if its finds it in the inventory, we will know
                findProductById(id, inventory);
            }
            //if they choose , then we can add items by the sku to the cart
            else if (input.equalsIgnoreCase("A")) {
                System.out.println("Enter the SKU of a product to add to your cart");
                String cartItem = scanner.nextLine();

                boolean itemFound = false;
                // first check to see if the input sku is correct, if it is, then add to cart
                for (Product product : inventory) {
                    if (product.getSku().equalsIgnoreCase(cartItem)) {
                        cart.add(product);
                        System.out.printf("%s was added to the cart\n", product.getName());
                        System.out.println();
                        itemFound = true;
                        break; // break the loop so it doesn't continue
                    }
                }
                /// out side the loop, if there is no item by that sku, we print this.
                if (!itemFound) {
                    System.out.println("Product not found in inventory.");
                }
            }
            // this returns us to the choices
            else if (input.equalsIgnoreCase("B")) {
                return;
            }
        }

    }

    /**
     * Shows the contents of the cart, calculates the total,
     * and offers the option to check out.
     * }
     */
    public static void displayCart(ArrayList<Product> cart, Scanner scanner) {
        // TODO:
        //   • list each product in the cart
        //   • compute the total cost
        //   • ask the user whether to check out (C) or return (X)
        //   • if C, call checkOut(cart, totalAmount, scanner)
        double totalAmount = 0;
        boolean isDone = false;
        if (cart.isEmpty()) {
            System.out.println("The cart is empty");
            return;
        }
        for (Product product : cart) {
            System.out.printf("%32s  %30.2f%n", product.getName(),product.getPrice());
            totalAmount += product.getPrice();
        }

        System.out.printf("The total is %.2f%n: " , totalAmount);

        while (!isDone) {
            System.out.println("Would like to checkout?");
            System.out.println("C ==> Checkout, return (X)");

            String checkoutChoice = scanner.nextLine();

            if (checkoutChoice.equalsIgnoreCase("C")) {
                checkOut(cart, totalAmount, scanner);
                isDone = true;
            }
            else if (checkoutChoice.equalsIgnoreCase("X")) {
                return;
            }else{
                System.out.println("Invalid entry,Please Choose C or X!");
            }
        }
    }

    /**
     * Handles the checkout process:
     * 1. Confirm that the user wants to buy.
     * 2. Accept payment and calculate change.
     * 3. Display a simple receipt.
     * 4. Clear the cart.
     */
    public static void checkOut(ArrayList<Product> cart, double totalAmount, Scanner scanner) {
        // TODO: implement steps listed above


    }

    /**
     * Searches a list for a product by its id.
     *
     * @return the matching Product, or null if not found
     */
    public static Product findProductById(String id, ArrayList<Product> inventory) {
        // TODO: loop over the list and compare ids
        for (Product product : inventory) {
            if (product.getSku().equalsIgnoreCase(id)) {
                System.out.println("Product found: " + product.getName());
                break;
            }
        }
        return null;
    }

    public static void productCount(ArrayList<Product> cart, int counter) {

    }

}