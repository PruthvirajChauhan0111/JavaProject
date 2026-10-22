
import java.util.*;

public class MiniBank {

    record BankInfo(String name, String branch) {}

    enum MenuOption {
        OPEN_ACCOUNT,
        DEPOSIT,
        WITHDRAW,
        TRANSFER,
        EXIT
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        BankInfo bank = new BankInfo("MiniBank", "Main Branch");

        
        System.out.println("       " + bank.name());
        System.out.println("       " + bank.branch());
      

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

            String message = switch (option) {
                case OPEN_ACCOUNT ->
                    "Open Account - to be implemented in a later lab.";
                case DEPOSIT ->
                    "Deposit - to be implemented in a later lab.";
                case WITHDRAW ->
                    "Withdraw - to be implemented in a later lab.";
                case TRANSFER ->
                    "Transfer - to be implemented in a later lab.";
                case EXIT ->
                    "Thank you for using MiniBank!";
            };

            System.out.println(message);

        } while (choice != 5);

        sc.close();
    }
}
