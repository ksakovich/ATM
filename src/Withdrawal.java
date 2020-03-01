// atm transaction type withdrawal
public class Withdrawal extends Transaction
{
    private int amount; // the amount the user wants to withdraw
    private int currentAccountNumber;
    private Keypad keypad; // form of input from user
    private CashDispenser cashDispenser;

    public Withdrawal(int currentAccountNumber, Screen screen, BankDatabase bankDatabase, Keypad keypad, CashDispenser cashDispenser)
    {
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
            getScreen().displayMessageLine("4 - $80");
            getScreen().displayMessageLine("5 - Other amount (multiple of 20)");
            getScreen().displayMessageLine("6 - Exit");
            return keypad.getInput();
        }
        catch(Exception e) {
            // is it an error if the wait is interrupted, by user?
            getScreen().displayMessageLine(e.getMessage());
        }
        return 6;
    }

    public int displayOtherAmountMenu() {
        try {
            wait(5000); // wait 5 seconds before timing out
            getScreen().displayMessageLine("Enter in multiples of $20");
            return keypad.getInput();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    @Override
    public void execute() {
        // validate input from user
        boolean valid = false; // flag for user input validation
        while (!valid) {
            int userInput = displayWithdrawalMenu();
            switch (userInput) {
                // $20
                case 1: {
                    this.amount = 20;
                    valid = execute(amount);
                    break;
                }
                // $40
                case 2: {
                    this.amount = 40;
                    valid = execute(amount);
                    break;
                }
                // $60
                case 3: {
                    this.amount = 60;
                    valid = execute(amount);
                    break;
                }
                // $80
                case 4: {
                    this.amount = 80;
                    valid = execute(amount);
                    break;
                }
                // other amount
                case 5: {
                    this.amount = displayOtherAmountMenu();
                    if (this.amount == 0) {
                        getScreen().displayMessageLine("Goodbye."); // user took too long to respond
                        this.keypad = null; // disable further input
                    } else {
                        valid = execute(amount);
                    }
                    break;
                }
                // Exit or no response
                case 6: {
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
    // if the user entered a valid amount to withdraw, debit account, then dispense cash
    // if for some reason the transaction couldn't be executed return control to user
    private boolean execute(int amount) {
        if (amount < getBankDatabase().getAvailableBalance(currentAccountNumber)) {
            getBankDatabase().debit(currentAccountNumber, amount); // remove money from user's balance
            cashDispenser.dispenseCash(amount); // dispense cash
            return true;
        } else {
            getScreen().displayMessageLine("Amount exceeds available funds."); // unable to remove
            return false;
        }
    }
}
