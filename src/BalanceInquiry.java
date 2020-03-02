public class BalanceInquiry extends Transaction
{
    public BalanceInquiry(int currentAccountNumber, Screen screen, BankDatabase bankDatabase)
    {
        super( currentAccountNumber, screen, bankDatabase);
    }

    @Override
    public void execute()
    {
        BankDatabase database = getBankDatabase();
        Screen screen = getScreen();
        int currentUserAccountNum = getAccountNumber();
        double availableBalance = database.getAvailableBalance(currentUserAccountNum);
        double totalBalance = database.getTotalBalance(currentUserAccountNum);
        if(availableBalance != totalBalance)
        {
            screen.displayMessage("Your available balance is: \n");
            screen.displayDollarAmount(availableBalance);
            screen.displayMessage("\n Your total balance is: \n");
            screen.displayDollarAmount(totalBalance);
            screen.displayMessage("\n");

        }
        else
        {
            screen.displayMessage("Your balance is: \n");
            screen.displayDollarAmount(totalBalance);
            screen.displayMessage("\n");
        }
    }
}
