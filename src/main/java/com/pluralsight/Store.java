package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.SQLOutput;
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
            BufferedReader reader = new BufferedReader(new FileReader("products.csv")); // created a buffer reader.
            reader.readLine(); // start reading after skipping the first line.
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
        ArrayList<String> displayedSku = new ArrayList<>();
        boolean isDone = false;
        if (cart.isEmpty()) {  // if the cart is empty, then return to menu and show this message.
            System.out.println("The cart is empty");
            return;
        }
        double totalAmount = 0;
        System.out.printf("%-35s %-10s %-12s %-12s%n", "Product", "Quantity", "Price", "Subtotal");


        // outer loop selects every item
        for (Product product : cart) {
            // if the sku of the product is in displayed, it gets skipped
            if (displayedSku.contains(product.getSku())) {//started as an empty list
                continue;// skip this iteration // if the sku are in the list, this line executes and the iteration is skipped.
            }
            //counter for the quantity
            int quantity = 0;


            //a nested loop, to check each item against the selected product of the outer loop.
            for (Product item : cart) {
                if (product.getSku().equals(item.getSku())) {  // if the sku matches, then add 1 to quantity.
                    quantity++;

                }
            }
            double subTotal = quantity * product.getPrice();
            totalAmount += subTotal; //add the subtotal to the total while looping

            // then make sure to add all products that are to be displayed.
            displayedSku.add(product.getSku());
            System.out.printf("%-35s %-10d $%-10.2f $%-10.2f%n", product.getName(), quantity, product.getPrice(), subTotal);  // then printf to make it organized

        }
        System.out.printf("Cart Total: $%.2f%n", totalAmount); //this prints the total of the cart


        while (!isDone) {
            System.out.println("Would like to checkout?");
            System.out.println("C ==> Checkout, return (X)");

            String checkoutChoice = scanner.nextLine();

            if (checkoutChoice.equalsIgnoreCase("C")) {
                System.out.println("Proceeding to checkout");
                checkOut(cart, totalAmount, scanner);
                isDone = true;
                System.out.println();

            } else if (checkoutChoice.equalsIgnoreCase("X")) {
                return;
            } else {
                System.out.println("Invalid entry,Please Choose C or X!");
                System.out.println();

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
         //displaying the total at first
        System.out.printf("Cart Total: $%.2f%n", totalAmount);
        // Asking for payment
        System.out.print("Enter the amount you would like to pay: ");
        double payment = scanner.nextDouble();
        scanner.nextLine();
         // if payment is less than the amount owed.
        while(payment < totalAmount){
            System.out.println("Insufficient payment. Please try again.");

            System.out.print("Enter the required amount: ");
            payment = scanner.nextDouble();
            scanner.nextLine();
        }
        double change = payment - totalAmount;
        System.out.println();

       // making the receipt
        System.out.println("======== SALES RECEIPT =========");
        // creating another arraylist to hold the already displayed sku, like the cart
        ArrayList<String> receiptSku = new ArrayList<>();
         // outer loop to go over each product
        for (Product product : cart) {
          // if its in the receipt list, we skip the iteration.
            if (receiptSku.contains(product.getSku())) {
                continue;

            }
            // counter for the quantity of products
            int quantity = 0;
             // inner loop for comparing each one against the product on the outer loop
            for (Product item : cart) {
                if (product.getSku().equals(item.getSku())) { // if the skus are a match, then we add one to quantity.
                    quantity++;
                }
            }
            // calculating the subtotal of a product.
            double subtotal = quantity * product.getPrice();
           // then add the product to the receipt sku list
            receiptSku.add(product.getSku());
           // printf and add spaces to make the receipt more cleaner.
            System.out.printf("%-30s %2d x $%-8.2f $%.2f%n", product.getName(), quantity, product.getPrice(), subtotal);



        }
        //
        System.out.println("-----------------------------------");
        System.out.printf("Total:       $%.2f%n", totalAmount);
        System.out.printf("Cash:        $%.2f%n", payment);
        System.out.printf("Change Given: $%.2f%n", change);

        System.out.println("==============================");
        System.out.println("Thank you for shopping with us!");

        cart.clear(); // to clear the items from the cart, so that they don't stay after the transaction.
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


}