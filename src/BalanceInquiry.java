public class BalanceInquiry extends Transaction
{
    public BalanceInquiry(int currentAccountNumber, Screen screen, BankDatabase bankDatabase)
    {
        super( currentAccountNumber, screen, bankDatabase);
    }

    @Override
    public void execute()
    {

    }
}
