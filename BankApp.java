import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

// ---------------- TRANSACTION CLASS ----------------
class Transaction implements Serializable {
    private String type;
    private double amount;
    private String dateTime;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
        this.dateTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String toString() {
        return type + " | Amount: ₹" + String.format("%.2f", amount) + " | Date: " + dateTime;
    }
}

// ---------------- BANK ACCOUNT CLASS ----------------
class BankAccount implements Serializable {
    private double balance;
    private double minBalance;
    private ArrayList<Transaction> transactions;

    public BankAccount(double initialBalance, double minBalance) {
        this.balance = initialBalance;
        this.minBalance = minBalance;
        this.transactions = new ArrayList<>();
    }

    // Deposit
    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("❌ Invalid deposit amount!");
            return;
        }
        balance += amount;
        transactions.add(new Transaction("Deposit", amount));
        System.out.println("✅ Deposited successfully!");
    }

    // Withdraw
    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("❌ Invalid withdrawal amount!");
            return;
        }
        if (balance - amount < minBalance) {
            System.out.println("❌ Cannot withdraw! Minimum balance must be maintained.");
            return;
        }
        balance -= amount;
        transactions.add(new Transaction("Withdraw", amount));
        System.out.println("✅ Withdrawn successfully!");
    }

    // View balance
    public void showBalance() {
        System.out.printf("💰 Current Balance: ₹%.2f\n", balance);
    }

    // Transaction history
    public void showTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }
        System.out.println("\n📜 Transaction History:");
        for (Transaction t : transactions) {
            System.out.println(t);
        }
    }

    // Interest calculation
    public void applyInterest(double rate, int time) {
        if (rate <= 0 || time <= 0) {
            System.out.println("❌ Invalid rate or time!");
            return;
        }
        double interest = (balance * rate * time) / 100;
        balance += interest;
        transactions.add(new Transaction("Interest", interest));
        System.out.println("✅ Interest added: ₹" + String.format("%.2f", interest));
    }

    // Save to file
    public void saveToFile(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
            System.out.println("💾 Account saved successfully!");
        } catch (IOException e) {
            System.out.println("❌ Error saving file!");
        }
    }

    // Load from file
    public static BankAccount loadFromFile(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            System.out.println("📂 Account loaded successfully!");
            return (BankAccount) ois.readObject();
        } catch (Exception e) {
            System.out.println("⚠️ No previous data found. Creating new account.");
            return new BankAccount(0, 500);
        }
    }
}

// ---------------- MAIN CLASS ----------------
public class BankApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String file = "account.dat";

        BankAccount account = BankAccount.loadFromFile(file);

        while (true) {
            System.out.println("\n===== BANK MENU =====");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. View Balance");
            System.out.println("4. Transaction History");
            System.out.println("5. Apply Interest");
            System.out.println("6. Save Account");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = sc.nextInt();
            } catch (Exception e) {
                System.out.println("❌ Invalid input!");
                sc.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter amount: ");
                    account.deposit(sc.nextDouble());
                    break;

                case 2:
                    System.out.print("Enter amount: ");
                    account.withdraw(sc.nextDouble());
                    break;

                case 3:
                    account.showBalance();
                    break;

                case 4:
                    account.showTransactions();
                    break;

                case 5:
                    System.out.print("Enter rate (%): ");
                    double rate = sc.nextDouble();
                    System.out.print("Enter time (years): ");
                    int time = sc.nextInt();
                    account.applyInterest(rate, time);
                    break;

                case 6:
                    account.saveToFile(file);
                    break;

                case 7:
                    System.out.print("Are you sure you want to exit? (yes/no): ");
                    String confirm = sc.next();
                    if (confirm.equalsIgnoreCase("yes")) {
                        account.saveToFile(file);
                        System.out.println("👋 Exiting... Thank you!");
                        sc.close();
                        return;
                    }
                    break;

                default:
                    System.out.println("❌ Invalid choice!");
            }
        }
    }
}