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
            getScreen().displayMessageLine("6 - Other amount (multiple of 20)");
            getScreen().displayMessageLine("7 - Exit");
            return keypad.getInput();
        } catch (Exception e) {
            // is it an error if the wait is interrupted, by user?
            getScreen().displayMessageLine(e.getMessage());
        }
        return 6;
    }

    public int displayOtherAmountMenu() {

        boolean valid_user_input = false;
        int user_amount = 0;
        while (valid_user_input == false) {
            getScreen().displayMessageLine("Enter in multiples of $20");
            user_amount = keypad.getInput();
            if (user_amount == 0) {
                getScreen().displayMessageLine("Error! Null entered.");
            } else {
                if (user_amount % 20 == 0) {
                    valid_user_input = true;
                    return user_amount;
                } else {
                    getScreen().displayMessageLine("Error!");
                }
            }
        }
        return user_amount;
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
                        if(cashDispenser.isSufficientCashAvailable(amount_to_withdraw))
                        {
                            cashDispenser.dispenseCash(amount_to_withdraw);
                            valid = execute(amount_to_withdraw, currentAccountNumber);
                        }
                        else
                        {
                            getScreen().displayMessageLine("Not enough cash available in ATM.");
                        }
                        break;
                    }
                    // $40
                    case 2: {
                        this.amount_to_withdraw = 40;
                        if(cashDispenser.isSufficientCashAvailable(amount_to_withdraw))
                        {
                            cashDispenser.dispenseCash(amount_to_withdraw);
                            valid = execute(amount_to_withdraw, currentAccountNumber);
                        }
                        else
                        {
                            getScreen().displayMessageLine("Not enough cash available in ATM.");
                        }
                        break;
                    }
                    // $60
                    case 3: {
                        this.amount_to_withdraw = 60;
                        if(cashDispenser.isSufficientCashAvailable(amount_to_withdraw))
                        {
                            cashDispenser.dispenseCash(amount_to_withdraw);
                            valid = execute(amount_to_withdraw, currentAccountNumber);
                        }
                        else
                        {
                            getScreen().displayMessageLine("Not enough cash available in ATM.");
                        }
                        break;
                    }
                    // $100
                    case 4: {
                        this.amount_to_withdraw = 100;

                        if(cashDispenser.isSufficientCashAvailable(amount_to_withdraw))
                        {
                            cashDispenser.dispenseCash(amount_to_withdraw);
                            valid = execute(amount_to_withdraw, currentAccountNumber);
                        }
                        else
                        {
                            getScreen().displayMessageLine("Not enough cash available in ATM.");
                        }
                        break;
                    }
                    //$200
                    case 5: {
                        this.amount_to_withdraw = 200;
                        if(cashDispenser.isSufficientCashAvailable(amount_to_withdraw))
                        {
                            cashDispenser.dispenseCash(amount_to_withdraw);
                            valid = execute(amount_to_withdraw, currentAccountNumber);
                        }
                        else
                        {
                            getScreen().displayMessageLine("Not enough cash available in ATM.");
                        }
                        break;
                    }
                    // other amount
                    case 6: {
                        this.amount_to_withdraw = displayOtherAmountMenu();
                        if(this.amount_to_withdraw < 0)
                        {
                            getScreen().displayMessageLine("Amount must be greater than 0...");
                            break;
                        }
                        if(cashDispenser.isSufficientCashAvailable(amount_to_withdraw))
                        {
                            cashDispenser.dispenseCash(amount_to_withdraw);
                            valid = execute(amount_to_withdraw, currentAccountNumber);
                        }
                        else
                        {
                            getScreen().displayMessageLine("Not enough cash available in ATM.");
                        }
                        break;
                    }
                    // Exit or no response
                    case 7: {
                        getScreen().displayMessageLine("Goodbye.");
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
    }
}
