package BankManagmentSystem;

import java.util.*;

// creating interface for user to hide implementation
interface UserOperations {
    void register(ArrayList<User> users, Scanner sc);
    void login(ArrayList<User> users, Scanner sc);
}

// Abstract class for User
abstract class User implements UserOperations {
    private String name;
    private String email;
    private String password;
    private long phone;   // it may go beyond int range
    private int pin; 
    private Account account;

    // getter function
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public long getPhone() {
        return phone;
    }

    public int getPin() {
        return pin;
    }
    
    // setter function

    public Account getAccount() {
        return account;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}

// child class for user class 
class ConcreteUser extends User {
    private static int accountNumberCounter = 1000; // initial value for account id
    
    @Override
    public void register(ArrayList<User> users, Scanner sc) {
    	// name

    	System.out.print("Enter name: ");
        setName(sc.next());
        
        // Email validation
        String emailPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        boolean validEmail = false;
        while (!validEmail) {
            System.out.print("Enter email: ");
            String inputEmail = sc.next();
            if (inputEmail.matches(emailPattern)) {
                validEmail = true;
                setEmail(inputEmail);
            } else {
                System.out.println("Invalid email format. Please try again.");
                System.out.println();
            }
        }


        // Password validation
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        boolean validPassword = false;
        while (!validPassword) {
            System.out.print("Enter password: ");
            String inputPassword = sc.next();
            if (inputPassword.matches(passwordPattern)) {
                validPassword = true;
                setPassword(inputPassword);
            } else {
                System.out.println("Please enter password with atleast one uppercase, one lowercase, one digit, one special character with minimum of 8 letters...!");
                System.out.println();
            }
        }
        
        // Phone number validation
        String phonePattern = "^\\d{10}$"; 
        boolean validPhone = false;
        while (!validPhone) {
            System.out.print("Enter phone number: ");
            String inputPhone = sc.next();
            if (inputPhone.matches(phonePattern)) {
                validPhone = true;
                setPhone(Long.parseLong(inputPhone));
            } else {
                System.out.println("Invalid phone number. Please enter a 10-digit number.");
                System.out.println();
            }
        }
        
     // PIN number setup
        boolean validPin = false;
        while (!validPin) {
            System.out.print("Set a 4-digit confidential PIN: ");
            String inputPin = sc.next();
            if (inputPin.matches("\\d{4}")) {
                validPin = true;
                setPin(Integer.parseInt(inputPin));
            } else {
                System.out.println("Invalid PIN format.");
                System.out.println();
            }
        }

        // Check if user already exists
        boolean userExists = false;
        for (User user : users) {
            if (user.getEmail().equals(getEmail()) || user.getPhone() == getPhone()) {
                System.out.println("User already exists. Please try to log in...");
                System.out.println();
                userExists = true;
                break;
            }
        }

        // If no existing user, assign an account and add new user
        if (!userExists) {
            String accountNumber = "ACC" + accountNumberCounter++; // Generate unique account number
            this.setAccount(new ConcreteAccount(accountNumber)); // Create account with account number
            users.add(this);
            System.out.println("User registered successfully! Your account number is: " + accountNumber);
            System.out.println();
        }
    }



    @Override
    public void login(ArrayList<User> users, Scanner sc) {
        System.out.print("Enter email: ");
        String inputEmail = sc.next();

        boolean userExists = false;
        User foundUser = null;

        for (User user : users) {
            if (user.getEmail().equals(inputEmail)) {
                userExists = true;
                foundUser = user;
                break;
            }
        }

        if (userExists) {
            boolean passwordCorrect = false;
            while (!passwordCorrect) {
                System.out.print("Enter password: ");
                String inputPassword = sc.next();

                // Check if the entered password matches the user's stored password
                if (foundUser.getPassword().equals(inputPassword)) {
                    System.out.println("Logged in successfully");
                    System.out.println();
                    passwordCorrect = true;
                    BankingApp.setLoggedInUser(foundUser); // Set the logged-in user
                } else {
                    System.out.println("Incorrect password, please try again.");
                    System.out.println();
                }
            }
        } else {
            System.out.println("It seems like there is no account associated with this email. Kindly register.");
            System.out.println();
        }
    }

}


// Interface for Account operations
interface AccountOperations {
    void deposit(double amount);
    void withdraw(double amount);
    double getBalance();
}

abstract class Account implements AccountOperations {
    private double balance;
    private String accountNumber; // Add accountNumber

    public Account(String accountNumber) {
        this.accountNumber = accountNumber;
        this.balance = 0.0; // Initialize balance to zero
    }

    public double getBalance() {
        return balance;
    }

    // Getter for accountNumber
    public String getAccountNumber() {
        return accountNumber;
    }

    // Setter for accountNumber (if needed)
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    // Encapsulation: setter method for private field balance
    public void setBalance(double balance) {
        this.balance = balance;
    }
}


// Concrete class for Account implementation
class ConcreteAccount extends Account {
    public ConcreteAccount(String accountNumber) {
        super(accountNumber); // Pass account number to parent class constructor
    }

    @Override
    public void deposit(double amount) {
        setBalance(getBalance() + amount);
        System.out.println("Deposit successful. New balance: " + getBalance());
        System.out.println();
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= getBalance()) {
            setBalance(getBalance() - amount);
            System.out.println("Withdrawal successful. New balance: " + getBalance());
            System.out.println();
        } else {
            System.out.println("Insufficient balance.");
            System.out.println();
        }
    }
}


public class BankingApp {
    private static ArrayList<User> users = new ArrayList<>();
    private static User loggedInUser;

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    public static void main(String[] args) {
        System.out.println("----Welcome to Bank Management System----");
        System.out.println();

        Scanner sc = new Scanner(System.in);

        boolean valid = true;
        while (valid) {
            System.out.println("Please Enter Your Choice");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.println();

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    User user = new ConcreteUser();
                    user.register(users, sc);
                    break;
                case 2:
                    User loginUser = new ConcreteUser();
                    loginUser.login(users, sc);
                    if (loggedInUser != null) {
                        accountMenu(sc);
                    }
                    break;
                case 3:
                	System.out.println("Thank you for using the Bank Management System. Goodbye!");
                    sc.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Please Enter the Correct Option");
                    System.out.println();
            }
        }
    }

    private static void accountMenu(Scanner sc) {
        boolean accountValid = true;
        while (accountValid) {
            System.out.println("Please Enter Your Choice");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Check Balance");
            System.out.println("4. Transfer");
            System.out.println("5. Logout");
            System.out.println();

            int choice = sc.nextInt();

            ConcreteAccount account = (ConcreteAccount) getLoggedInUser().getAccount();

            switch (choice) {
                case 1:
                    if (!authenticatePin(sc)) {
                        System.out.println("Authentication failed.");
                        System.out.println();
                        continue;
                    }
                    System.out.print("Enter amount to deposit: ");
                    double depositAmount = sc.nextDouble();
                    account.deposit(depositAmount);
                    break;
                case 2:
                    if (!authenticatePin(sc)) {
                        System.out.println("Authentication failed.");
                        System.out.println();
                        continue;
                    }
                    System.out.print("Enter amount to withdraw: ");
                    double withdrawAmount = sc.nextDouble();
                    account.withdraw(withdrawAmount);
                    break;
                case 3:
                    if (!authenticatePin(sc)) {
                        System.out.println("Authentication failed.");
                        System.out.println();
                        continue;
                    }
                    System.out.println("Current balance: " + account.getBalance());
                    System.out.println();
                    break;
                case 4:
                    if (!authenticatePin(sc)) {
                        System.out.println("Authentication failed.");
                        System.out.println();
                        continue;
                    }
                    System.out.print("Enter recipient's account number: ");
                    String recipientAccountNumber = sc.next();

                    // Find the recipient's account by account number
                    User recipientUser = null;
                    for (User user : users) {
                        if (user.getAccount().getAccountNumber().equals(recipientAccountNumber)) {
                            recipientUser = user;
                            break;
                        }
                    }

                    if (recipientUser != null) {
                        System.out.print("Enter amount to transfer:");
                        double amount = sc.nextDouble();

                        Account recipientAccount = recipientUser.getAccount(); // Get recipient's account
                        if (recipientAccount != null && account.getBalance() >= amount) {
                            account.withdraw(amount);
                            recipientAccount.deposit(amount);
                            System.out.println("Transfer successful.");
                            System.out.println();
                        } else {
                            System.out.println("Recipient account not found.");
                            System.out.println();
                        }
                    } else {
                        System.out.println("Recipient's account not found.");
                        System.out.println();
                    }
                    break;

                case 5:
                    // No need to authenticate PIN when logging out
                    accountValid = false;
                    loggedInUser = null;
                    System.out.println("Logged out successfully.");
                    System.out.println();
                    break;
                default:
                    System.out.println("Please Enter the Correct Option");
                    System.out.println();
            }
        }
    }

    private static boolean authenticatePin(Scanner sc) {
        System.out.println("Enter your PIN:");
        int inputPin = sc.nextInt();
        return inputPin == getLoggedInUser().getPin();
    }
}
