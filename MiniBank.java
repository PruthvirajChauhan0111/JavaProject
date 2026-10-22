
import java.util.Scanner;

public class MiniBank {

    record BankInfo(String name, String branch) {}

    enum MenuOption {
        OPEN_ACCOUNT,
        DEPOSIT,
        WITHDRAW,
        TRANSFER,
        EXIT
    }

    static class Customer {
        private String name;
        private String email;
        private String mobile;
        private final String customerId;

        private static long customerCounter = 100;

        private static String generateCustomerId() {
            customerCounter++;
            return "CUST" + customerCounter;
        }

        public Customer(String name, String email, String mobile) {
            this.name = name;
            this.email = email;
            this.mobile = mobile;
            this.customerId = generateCustomerId();
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getMobile() {
            return mobile;
        }

        public String getCustomerId() {
            return customerId;
        }
    }

    static class Account {
        private final String accountNumber;
        private String ownerName;
        private long balance;
        private boolean active;

        private static long accountCounter = 0;

        private static String generateAccountNumber() {
            accountCounter++;
            return String.format("AC%04d", accountCounter);
        }

        public Account(String ownerName, long balance) {
            this.accountNumber = generateAccountNumber();
            this.ownerName = ownerName;
            this.balance = balance;
            this.active = true;
        }

        public Account(String ownerName) {
            this(ownerName, 0);
        }

        public void deposit(long amount) {
            if (amount > 0 && active) {
                balance += amount;
                System.out.println("Deposit successful.");
            } else {
                System.out.println("Invalid amount or inactive account.");
            }
        }

        public boolean withdraw(long amount) {
            if (amount > 0 && balance >= amount && active) {
                balance -= amount;
                return true;
            }
            return false;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public String getOwnerName() {
            return ownerName;
        }

        public long getBalance() {
            return balance;
        }

        public boolean isActive() {
            return active;
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        BankInfo bank = new BankInfo("MiniBank", "Main Branch");

        System.out.println("==============================");
        System.out.println("       " + bank.name());
        System.out.println("       " + bank.branch());
        System.out.println("==============================");

        Customer[] customers = new Customer[100];
        Account[] accounts = new Account[100];
        int customerCount = 0;
        int accountCount = 0;

        int choice;

        do {
            System.out.println("\n===== BANK MENU =====");
            System.out.println("1. Open Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            if (choice < 1 || choice > 5) {
                System.out.println("Invalid choice! Please try again.");
                continue;
            }

            MenuOption option = switch (choice) {
                case 1 -> MenuOption.OPEN_ACCOUNT;
                case 2 -> MenuOption.DEPOSIT;
                case 3 -> MenuOption.WITHDRAW;
                case 4 -> MenuOption.TRANSFER;
                default -> MenuOption.EXIT;
            };

            switch (option) {
                case OPEN_ACCOUNT -> {
                    if (accountCount >= accounts.length) {
                        System.out.println("Account limit reached.");
                        break;
                    }

                    System.out.print("Enter customer name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter email: ");
                    String email = sc.nextLine();

                    System.out.print("Enter mobile: ");
                    String mobile = sc.nextLine();

                    System.out.print("Enter opening balance: ");
                    long balance = sc.nextLong();
                    sc.nextLine();

                    if (balance < 0) {
                        System.out.println("Opening balance cannot be negative.");
                        break;
                    }

                    Customer customer = new Customer(name, email, mobile);
                    Account account = new Account(name, balance);

                    customers[customerCount++] = customer;
                    accounts[accountCount++] = account;

                    System.out.println("Account created successfully.");
                    System.out.println("Customer ID: " + customer.getCustomerId());
                    System.out.println("Account Number: " + account.getAccountNumber());
                }

                case DEPOSIT -> {
                    System.out.print("Enter account number: ");
                    String accNo = sc.nextLine();

                    Account account = null;

                    for (int i = 0; i < accountCount; i++) {
                        if (accounts[i].getAccountNumber().equals(accNo)) {
                            account = accounts[i];
                            break;
                        }
                    }

                    if (account == null) {
                        System.out.println("Account not found.");
                    } else {
                        System.out.print("Enter deposit amount: ");
                        long amount = sc.nextLong();
                        sc.nextLine();

                        account.deposit(amount);
                        System.out.println("Balance: Rs. " + account.getBalance());
                    }
                }

                case WITHDRAW -> {
                    System.out.print("Enter account number: ");
                    String accNo = sc.nextLine();

                    Account account = null;

                    for (int i = 0; i < accountCount; i++) {
                        if (accounts[i].getAccountNumber().equals(accNo)) {
                            account = accounts[i];
                            break;
                        }
                    }

                    if (account == null) {
                        System.out.println("Account not found.");
                    } else {
                        System.out.print("Enter withdrawal amount: ");
                        long amount = sc.nextLong();
                        sc.nextLine();

                        if (account.withdraw(amount)) {
                            System.out.println("Withdrawal successful.");
                        } else {
                            System.out.println("Insufficient balance or invalid amount.");
                        }

                        System.out.println("Balance: Rs. " + account.getBalance());
                    }
                }

                case TRANSFER ->
                    System.out.println("Transfer - to be implemented in a later lab.");

                case EXIT ->
                    System.out.println("Thank you for using MiniBank!");
            }

        } while (choice != 5);

        sc.close();
    }
}
