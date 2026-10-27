import java.util.Scanner;
import java.util.Objects;
import java.util.regex.Pattern;

public class MiniBank {

    record BankInfo(String name, String branch) {}

    enum MenuOption {
        OPEN_ACCOUNT, DEPOSIT, WITHDRAW, TRANSFER, EXIT
    }

    enum TransactionType {
        DEPOSIT, WITHDRAW, TRANSFER
    }

    record Command(TransactionType type, String accountNumber, long amount) {}

    static class Validator {

        private static final Pattern MOBILE_PATTERN =
                Pattern.compile("^[6-9][0-9]{9}$");

        private static final Pattern EMAIL_PATTERN =
                Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

        private static final Pattern PAN_PATTERN =
                Pattern.compile("^[A-Z]{5}[0-9]{4}[A-Z]$");

        private static final Pattern IFSC_PATTERN =
                Pattern.compile("^[A-Z]{4}0[A-Z0-9]{6}$");

        public static boolean isValidMobile(String mobile) {
            return mobile != null && MOBILE_PATTERN.matcher(mobile).matches();
        }

        public static boolean isValidEmail(String email) {
            return email != null && EMAIL_PATTERN.matcher(email).matches();
        }

        public static boolean isValidPan(String pan) {
            return pan != null && PAN_PATTERN.matcher(pan).matches();
        }

        public static boolean isValidIfsc(String ifsc) {
            return ifsc != null && IFSC_PATTERN.matcher(ifsc).matches();
        }
    }

    static class CommandParser {

        public static Command parse(String line) {
            if (line == null || line.trim().isEmpty()) {
                throw new IllegalArgumentException("Command cannot be empty.");
            }

            String[] parts = line.trim().split("\\s+");

            if (parts.length != 3) {
                throw new IllegalArgumentException(
                        "Format: DEPOSIT AC0001 500");
            }

            TransactionType type =
                    TransactionType.valueOf(parts[0].toUpperCase());

            String accountNumber = parts[1];
            long amount = Long.parseLong(parts[2]);

            if (amount <= 0) {
                throw new IllegalArgumentException(
                        "Amount must be greater than zero.");
            }

            return new Command(type, accountNumber, amount);
        }
    }

    static class StatementFormatter {

        public static String buildStatement(Account account) {
            StringBuilder statement = new StringBuilder();

            statement.append("\n===== ACCOUNT STATEMENT =====\n");
            statement.append("Account Number: ")
                    .append(account.getAccountNumber()).append("\n");
            statement.append("Owner Name: ")
                    .append(account.getOwnerName()).append("\n");
            statement.append("Balance: Rs. ")
                    .append(account.getBalance()).append("\n");
            statement.append("Status: ")
                    .append(account.isActive() ? "Active" : "Inactive")
                    .append("\n");

            return statement.toString();
        }
    }

    static class Customer implements Cloneable {

        private String name;
        private String email;
        private String mobile;
        private final String customerId;
        private Address address;

        private static long customerCounter = 100;

        private static String generateCustomerId() {
            return "CUST" + (++customerCounter);
        }

        public static class Address {

            private String line;
            private String city;
            private String pincode;

            public Address(String line, String city, String pincode) {
                this.line = line;
                this.city = city;
                this.pincode = pincode;
            }

            public String getLine() {
                return line;
            }

            public String getCity() {
                return city;
            }

            public String getPincode() {
                return pincode;
            }
        }

        public Customer(String name, String email, String mobile,
                        Address address) {
            this.name = name;
            this.email = email;
            this.mobile = mobile;
            this.customerId = generateCustomerId();
            this.address = address;
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

        public Address getAddress() {
            return address;
        }

        @Override
        public Customer clone() {
            try {
                Customer copy = (Customer) super.clone();

                if (address != null) {
                    copy.address = new Address(
                            address.getLine(),
                            address.getCity(),
                            address.getPincode());
                }

                return copy;
            } catch (CloneNotSupportedException e) {
                throw new AssertionError(e);
            }
        }
    }

    static class Account {

        private final String accountNumber;
        private String ownerName;
        private long balance;
        private boolean active;

        private static long accountCounter = 0;

        private static String generateAccountNumber() {
            return String.format("AC%04d", ++accountCounter);
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
            if (amount > 0 && amount <= balance && active) {
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

        @Override
        public String toString() {
            return "Account Number: " + accountNumber
                    + ", Owner: " + ownerName
                    + ", Balance: Rs. " + balance;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof Account)) {
                return false;
            }

            Account other = (Account) obj;
            return accountNumber.equals(other.accountNumber);
        }

        @Override
        public int hashCode() {
            return Objects.hash(accountNumber);
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        BankInfo bank = new BankInfo("MiniBank", "Main Branch");

        System.out.println("===== " + bank.name() + " =====");
        System.out.println("Branch: " + bank.branch());

        System.out.println("\n===== VALIDATOR TESTING =====");

        System.out.print("Enter mobile number: ");
        String mobile = sc.nextLine();
        System.out.println("Mobile valid: "
                + Validator.isValidMobile(mobile));

        System.out.print("Enter email: ");
        String email = sc.nextLine();
        System.out.println("Email valid: "
                + Validator.isValidEmail(email));

        System.out.print("Enter PAN number: ");
        String pan = sc.nextLine().toUpperCase();
        System.out.println("PAN valid: "
                + Validator.isValidPan(pan));

        System.out.print("Enter IFSC code: ");
        String ifsc = sc.nextLine().toUpperCase();
        System.out.println("IFSC valid: "
                + Validator.isValidIfsc(ifsc));

        System.out.println("\n===== COMMAND PARSER =====");
        System.out.println("Example: DEPOSIT AC0001 500");
        System.out.print("Enter command: ");
        String line = sc.nextLine();

        try {
            Command command = CommandParser.parse(line);

            System.out.println("Transaction Type: " + command.type());
            System.out.println("Account Number: " + command.accountNumber());
            System.out.println("Amount: Rs. " + command.amount());

        } catch (IllegalArgumentException e) {
            System.out.println("Invalid command: " + e.getMessage());
        }

        System.out.println("\n===== CREATE CUSTOMER =====");

        System.out.print("Enter customer name: ");
        String name = sc.nextLine();

        System.out.print("Enter address line: ");
        String addressLine = sc.nextLine();

        System.out.print("Enter city: ");
        String city = sc.nextLine();

        System.out.print("Enter pincode: ");
        String pincode = sc.nextLine();

        Customer.Address address =
                new Customer.Address(addressLine, city, pincode);

        Customer customer =
                new Customer(name, email, mobile, address);

        Customer clonedCustomer = customer.clone();

        System.out.println("Customer ID: " + customer.getCustomerId());
        System.out.println("Customer Name: " + customer.getName());
        System.out.println("Customer City: "
                + customer.getAddress().getCity());
        System.out.println("Cloned Customer: " + clonedCustomer.getName());

        System.out.println("\n===== CREATE ACCOUNTS =====");

        Account[] accounts = new Account[3];

        for (int i = 0; i < accounts.length; i++) {
            System.out.print("Enter owner name for account "
                    + (i + 1) + ": ");
            String owner = sc.nextLine();

            System.out.print("Enter opening balance: ");
            long balance = Long.parseLong(sc.nextLine());

            if (balance < 0) {
                System.out.println("Negative balance not allowed.");
                balance = 0;
            }

            accounts[i] = new Account(owner, balance);
            System.out.println("Account created: "
                    + accounts[i].getAccountNumber());
        }

        System.out.println("\n===== ACCOUNT DETAILS =====");

        for (Account account : accounts) {
            System.out.println(account);
        }

        System.out.println("\nAccounts equal: "
                + accounts[0].equals(accounts[1]));

        Object obj = accounts[0];
        System.out.println("Is object an Account? "
                + (obj instanceof Account));

        System.out.println(StatementFormatter.buildStatement(accounts[0]));

        int choice;

        do {
            System.out.println("\n===== BANK MENU =====");
            System.out.println("1. Open Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            if (!sc.hasNextInt()) {
                System.out.println("Please enter a valid number.");
                sc.nextLine();
                choice = 0;
                continue;
            }

            choice = sc.nextInt();
            sc.nextLine();

            MenuOption option = switch (choice) {
                case 1 -> MenuOption.OPEN_ACCOUNT;
                case 2 -> MenuOption.DEPOSIT;
                case 3 -> MenuOption.WITHDRAW;
                case 4 -> MenuOption.TRANSFER;
                case 5 -> MenuOption.EXIT;
                default -> null;
            };

            if (option == null) {
                System.out.println("Invalid choice. Please try again.");
                continue;
            }

            switch (option) {
                case OPEN_ACCOUNT ->
                    System.out.println("Open Account - coming in a later lab.");

                case DEPOSIT ->
                    System.out.println("Deposit - coming in a later lab.");

                case WITHDRAW ->
                    System.out.println("Withdraw - coming in a later lab.");

                case TRANSFER ->
                    System.out.println("Transfer - coming in a later lab.");

                case EXIT ->
                    System.out.println("Thank you for using MiniBank!");
            }

        } while (choice != 5);

        sc.close();
    }
}