// Keypad.java
// Represents the keypad of the ATM
import java.lang.String;
import java.util.Scanner; // program uses Scanner to obtain user input
public class Keypad {
    private Screen screen;
    private Scanner input; // reads data from the command line
    // no-argument constructor initializes the Scanner
    public Keypad()
    {
        input = new Scanner(System.in);
        screen = new Screen();
    }
    private boolean caughtExcepttion = false;
    private boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            caughtExcepttion = true;
            return false;
        }
    }

    // return an integer value entered by user
    public int getInput()
    {
        String userInput = input.nextLine();
        if(tryParseInt(userInput))
        {
            return Integer.parseInt(userInput);
        }
        if(caughtExcepttion)
        {
            screen.displayMessageLine("\nOnly integers are allowed to input");
            screen.displayMessageLine("\nTerminating the program...");
            java.lang.System.exit(1);
        }
        //String userInput = input.nextInt();
        return -1; // we assume that user enters an integer
    }
}