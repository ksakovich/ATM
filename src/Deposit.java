
// Represents a deposit ATM transaction
public class Deposit extends Transaction {
    private double amount;
    private Keypad keypad;
    private DepositSlot depositSlot; // this is referring to depositSlot class
    private final static int CANCELED = 0; // option to cancel transaction

    // This is the Deposit constructor
    public Deposit(int userAccountNumber, Screen atmScreen, BankDatabase atmBankDatabase, Keypad atmKeypad,
                   DepositSlot atmDepositSlot) {
        // initialize superclass
        super(userAccountNumber, atmScreen, atmBankDatabase);
        keypad = atmKeypad;
        depositSlot = atmDepositSlot;
    }
    // execute the transaction
    @Override
    public void execute() {
        BankDatabase bankDatabase = getBankDatabase();
        Screen screen = getScreen();
        amount = promptForDepositAmount(); // get deposit amount from the user

        // check if user entered a deposit amount or canceled
        if (amount != CANCELED) {

            screen.displayMessage("\nPlease insert your deposit envelope with :  " + amount + "dollars");
            //screen.displayDollarAmount(amount);

            // checking if  deposit envelope has been received
            boolean envelopeReceived = depositSlot.isEnvelopeReceived();

            if (envelopeReceived) {
                screen.displayMessageLine("\nYour envelope has been "
                        + "accepted.\nThe money just deposited will not "
                        + "be available right away." + " We are verifying available founds. ");
     // credit account--> comes from data base and reflects the founds after deposit
                bankDatabase.credit(getAccountNumber(), amount);
            }
            else
            {
                screen.displayMessageLine(
                        "\n Error: You did not insert an envelope."+"\n ATM terminated your transaction.");
            }
        }
        // might need another screen with Canceling transaction message
        }
    }

    // ask user to enter the deposit amount
   private double promptForDepositAmount() {
// Code here for prompt user for the deposit
       //input from screen
       // check whether the user canceled transaction or entered a valid amount

    }
}
