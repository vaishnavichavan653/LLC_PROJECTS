import java.util.*;
import java.io.*;

// Item class (OOP concept)
class Item implements Serializable {
    String name;
    String category;
    double price;
    boolean purchased;

    Item(String name, String category, double price) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.purchased = false;
    }

    public String toString() {
        String status = purchased ? "✓ Done" : "Pending";
        return String.format("%-15s %-15s ₹%-8.2f %-10s", name, category, price, status);
    }
}

public class ShoppingListManager {

    static Scanner sc = new Scanner(System.in);
    static HashMap<String, ArrayList<Item>> allLists = new HashMap<>();
    static final String FILE_NAME = "shopping.dat";

    public static void main(String[] args) {

        loadFromFile();

        System.out.print("Enter shopping list name: ");
        String listName = sc.nextLine();

        allLists.putIfAbsent(listName, new ArrayList<>());
        ArrayList<Item> currentList = allLists.get(listName);

        int choice;

        do {
            System.out.println("\n===== Shopping List Manager =====");
            System.out.println("1. Add Item");
            System.out.println("2. Remove Item");
            System.out.println("3. Mark as Purchased");
            System.out.println("4. View Items");
            System.out.println("5. Sort by Category");
            System.out.println("6. Show Total Cost");
            System.out.println("7. Save & Exit");
            System.out.print("Enter choice: ");

            while (!sc.hasNextInt()) {
                System.out.print("Invalid input! Enter number: ");
                sc.next();
            }

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1: addItem(currentList); break;
                case 2: removeItem(currentList); break;
                case 3: markPurchased(currentList); break;
                case 4: viewItems(currentList); break;
                case 5: sortByCategory(currentList); break;
                case 6: showTotalCost(currentList); break;
                case 7: saveToFile();
                        System.out.println("Data saved. Exiting...");
                        break;
                default: System.out.println("Invalid choice!");
            }

        } while (choice != 7);
    }

    // Add Item
    static void addItem(ArrayList<Item> list) {
        System.out.print("Enter item name: ");
        String name = sc.nextLine();

        System.out.print("Enter category: ");
        String category = sc.nextLine();

        System.out.print("Enter price: ");
        double price = sc.nextDouble();
        sc.nextLine();

        list.add(new Item(name, category, price));
        System.out.println("Item added successfully!");
    }

    // Remove Item
    static void removeItem(ArrayList<Item> list) {
        viewItems(list);

        if (list.isEmpty()) return;

        System.out.print("Enter item number to remove: ");
        int index = sc.nextInt();
        sc.nextLine();

        if (index >= 1 && index <= list.size()) {
            list.remove(index - 1);
            System.out.println("Item removed!");
        } else {
            System.out.println("Invalid index!");
        }
    }

    // Mark as Purchased
    static void markPurchased(ArrayList<Item> list) {
        viewItems(list);

        if (list.isEmpty()) return;

        System.out.print("Enter item number to mark as purchased: ");
        int index = sc.nextInt();
        sc.nextLine();

        if (index >= 1 && index <= list.size()) {
            list.get(index - 1).purchased = true;
            System.out.println("Item marked as purchased!");
        } else {
            System.out.println("Invalid index!");
        }
    }

    // View Items
    static void viewItems(ArrayList<Item> list) {
        if (list.isEmpty()) {
            System.out.println("No items in list.");
            return;
        }

        System.out.println("\n--- Shopping List ---");
        System.out.printf("%-5s %-15s %-15s %-10s %-10s\n", "No", "Name", "Category", "Price", "Status");

        int i = 1;
        for (Item item : list) {
            System.out.printf("%-5d %s\n", i++, item);
        }
    }

    // Sort by Category
    static void sortByCategory(ArrayList<Item> list) {
        Collections.sort(list, (a, b) -> a.category.compareToIgnoreCase(b.category));
        System.out.println("Items sorted by category!");
    }

    // Total Cost
    static void showTotalCost(ArrayList<Item> list) {
        double total = 0;
        for (Item item : list) {
            total += item.price;
        }
        System.out.println("Total Cost: ₹" + total);
    }

    // Save to File
    static void saveToFile() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME));
            oos.writeObject(allLists);
            oos.close();
        } catch (Exception e) {
            System.out.println("Error saving data.");
        }
    }

    // Load from File
    static void loadFromFile() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME));
            allLists = (HashMap<String, ArrayList<Item>>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            // First run - no file
        }
    }
}