// atm transaction type withdrawal
public class Withdrawal extends Transaction {
    private int amount_to_withdraw; // the amount the user wants to withdraw
    private int currentAccountNumber;
    private Keypad keypad; // form of input from user
    private CashDispenser cashDispenser;

    public Withdrawal(int currentAccountNumber, Screen screen, BankDatabase bankDatabase, Keypad keypad, CashDispenser cashDispenser) {
        super(currentAccountNumber, screen, bankDatabase);
        this.currentAccountNumber = currentAccountNumber;
        this.keypad = keypad;
        this.cashDispenser = cashDispenser;
        // we aren't setting amount until we know the amount the user wants to withdraw
    }

    public int displayWithdrawalMenu() {
        try {
            //wait(50000); // wait 5 seconds before timing out
            getScreen().displayMessageLine("\nWithdraw:");
            getScreen().displayMessageLine("1 - $20");
            getScreen().displayMessageLine("2 - $40");
            getScreen().displayMessageLine("3 - $60");
            getScreen().displayMessageLine("4 - $100");
            getScreen().displayMessageLine("5 - $200");
            getScreen().displayMessageLine("6 - Exit");
            return keypad.getInput();
        } catch (Exception e) {
            // is it an error if the wait is interrupted, by user?
            getScreen().displayMessageLine(e.getMessage());
        }
        return 6;
    }

    @Override
    public void execute() {
        BankDatabase database = getBankDatabase();
        Screen screen = getScreen();
        int currentUserAccountNum = getAccountNumber();
        double availableBalance = database.getAvailableBalance(currentUserAccountNum);
        // Check if a user available balance < 20. $20 is minimum bill, ATM has.
        if (availableBalance < 20) {
            screen.displayMessage("Withdrawal option is unavailable, because your available balance is lower $20 ");
        } else {    // validate input from user
            boolean valid = false; // flag for user input validation
            while (!valid) {
                int userInput = displayWithdrawalMenu();
                switch (userInput) {
                    // $20
                    case 1: {
                        this.amount_to_withdraw = 20;
                        valid = execute(amount_to_withdraw, currentAccountNumber);
                        break;
                    }
                    // $40
                    case 2: {
                        this.amount_to_withdraw = 40;
                        valid = execute(amount_to_withdraw, currentAccountNumber);
                        break;
                    }
                    // $60
                    case 3: {
                        this.amount_to_withdraw = 60;
                        valid = execute(amount_to_withdraw, currentAccountNumber);
                        break;
                    }
                    // $100
                    case 4: {
                        this.amount_to_withdraw = 100;
                        valid = execute(amount_to_withdraw, currentAccountNumber);
                        break;
                    }
                    //$200
                    case 5: {
                        this.amount_to_withdraw = 200;
                        valid = execute(amount_to_withdraw, currentAccountNumber);
                        break;
                    }
                    // Exit or no response
                    case 6: {
                        this.keypad = null; // don't allow further input
                        valid = true; // exit withdrawal
                        break;
                    }
                    // Wrong response
                    default: {
                        getScreen().displayMessageLine("Please enter a valid option."); // user entered an invalid response
                        break;
                    }
                }
            }
        }
    }

    // if the user entered a valid amount to withdraw, debit account, then dispense cash
    private boolean execute(int amount, int currentUserAccountNum) {
        BankDatabase database = getBankDatabase();
        Screen screen = getScreen();

        if (cashDispenser.isSufficientCashAvailable(amount_to_withdraw)) {
            if (amount <= getBankDatabase().getAvailableBalance(currentAccountNumber)) {
                getBankDatabase().debit(currentAccountNumber, amount); // remove money from user's balance
                cashDispenser.dispenseCash(amount); // dispense cash

                // print results
                screen.displayMessage("\nWithrawal: " + amount_to_withdraw + "\n");
                screen.displayMessage("Your available balance is: \n");
                screen.displayDollarAmount(database.getAvailableBalance(currentUserAccountNum));
                screen.displayMessage("\n");
                screen.displayMessage("Please take your money!");
                screen.displayMessage("\n");
                return true;

            } else {
                getScreen().displayMessageLine("Amount exceeds available funds. Please choose another amount.");
                return false;
            }
        } else {
            getScreen().displayMessageLine("Not enough cash available in ATM.");
            return false;
        }
    }
}
