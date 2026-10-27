
import java.util.Scanner;

class Customer implements Cloneable {
    private String name;
    private String email;
    private String mobile;
    private final String customerId;
    private static int counter = 101;
    private Address address;

    public Customer(String name, String email, String mobile, Address address) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
        this.customerId = "CUST" + counter++;
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
            return (Customer) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
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

        @Override
        public String toString() {
            return line + ", " + city + " - " + pincode;
        }
    }
}

class Account {
    private final String accountNumber;
    private String ownerName;
    private long balance;
    private boolean active;
    private static int counter = 1;

    public Account(String ownerName, long balance) {
        this.accountNumber = String.format("AC%04d", counter++);
        this.ownerName = ownerName;
        this.balance = balance;
        this.active = true;
    }

    public Account(String ownerName) {
        this(ownerName, 0);
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

    public void deposit(long amount) {
        if (active && amount > 0) {
            balance += amount;
        }
    }

    public boolean withdraw(long amount) {
        if (active && amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Account Number: " + accountNumber
                + "\nOwner Name: " + ownerName
                + "\nBalance: " + balance
                + "\nActive: " + active;
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
        return accountNumber.hashCode();
    }
}

public class MiniBank {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter Customer Details");

        System.out.print("Enter customer name: ");
        String name = sc.nextLine();

        System.out.print("Enter email: ");
        String email = sc.nextLine();

        System.out.print("Enter mobile number: ");
        String mobile = sc.nextLine();

        System.out.print("Enter address line: ");
        String line = sc.nextLine();

        System.out.print("Enter city: ");
        String city = sc.nextLine();

        System.out.print("Enter pincode: ");
        String pincode = sc.nextLine();

        Customer.Address address =
                new Customer.Address(line, city, pincode);

        Customer customer =
                new Customer(name, email, mobile, address);

        System.out.println("\nEnter Account 1 Details");

        System.out.print("Enter account holder name: ");
        String owner1 = sc.nextLine();

        System.out.print("Enter initial balance: ");
        long balance1 = sc.nextLong();
        sc.nextLine();

        Account a1 = new Account(owner1, balance1);

        System.out.println("\nEnter Account 2 Details");

        System.out.print("Enter account holder name: ");
        String owner2 = sc.nextLine();

        System.out.print("Enter initial balance: ");
        long balance2 = sc.nextLong();
        sc.nextLine();

        Account a2 = new Account(owner2, balance2);

        System.out.println("\nCustomer Details");
        System.out.println("Customer ID: " + customer.getCustomerId());
        System.out.println("Name: " + customer.getName());
        System.out.println("Email: " + customer.getEmail());
        System.out.println("Mobile: " + customer.getMobile());
        System.out.println("Address: " + customer.getAddress());

        System.out.println("\nAccount 1 Details");
        System.out.println(a1.toString());

        System.out.println("\nAccount 2 Details");
        System.out.println(a2.toString());

        System.out.println("\nCompare Accounts");
        System.out.println("Are accounts equal? " + a1.equals(a2));

        System.out.println("Account 1 hash code: " + a1.hashCode());
        System.out.println("Account 2 hash code: " + a2.hashCode());

        Object obj = a1;
        System.out.println("Is object an Account? " + (obj instanceof Account));

        Customer clonedCustomer = customer.clone();

        System.out.println("\nCloned Customer Details");
        System.out.println("Customer ID: " + clonedCustomer.getCustomerId());
        System.out.println("Name: " + clonedCustomer.getName());
        System.out.println("Email: " + clonedCustomer.getEmail());
        System.out.println("Mobile: " + clonedCustomer.getMobile());
        System.out.println("Address: " + clonedCustomer.getAddress());

        sc.close();
    }
}