package lulu;

import command.Command;
import exceptions.InvalidCommandException;
import exceptions.InvalidDateException;
import exceptions.InvalidSlashParameterException;
import exceptions.LuluException;
import parser.Parser;
import storage.Storage;
import tasklist.TaskList;
import ui.UI;

/**
 * The main class for lulu.Lulu, a simple chatbot application.
 * lulu.Lulu interacts with the user through a command-line interface (CLI)
 * to perform tasks such as adding, listing, and deleting tasks.
 */
public class Lulu {

    /** The storage component for managing data persistence. */
    private Storage storage;

    /** The task list for storing and managing tasks. */
    private TaskList tasks;

    /** The parser for interpreting user input and generating commands. */
    private Parser parser;
    /**
     * Constructs a new instance of lulu.Lulu with default configurations.
     * Initializes the storage, task list, and parser.
     */
    public Lulu() {
        this.storage = new Storage(System.getProperty("user.home")
                + "/cs2103t/ip/src/main/resources/data/", "lulu.txt");
        this.tasks = new TaskList(this.storage.retrieveLines());
        this.parser = new Parser();
    }

    /**
     * Listens for user input and responds accordingly until the user exits.
     * Handles various user commands and exceptions during interaction.
     */
    public void respond() {
        while (true) {
            String input = UI.nextLine();

            try {
                if (input.toLowerCase().equals("bye")) {
                    break;
                } else if (input.toLowerCase().equals("list")) {
                    UI.printTasks(this.tasks);
                } else {
                    Command command = parser.parse(input);
                    command.execute(this.tasks, this.storage);
                }
            } catch (InvalidCommandException e) {
                UI.print("Sorry, I don't think I quite understood what you meant...");
            } catch (InvalidDateException e) {
                UI.print("Please ensure that you are inputting valid start and end dates.");
            } catch (InvalidSlashParameterException e) {
                UI.print("Please ensure that you are inputting valid date parameters.");
            } catch (LuluException e) {
                UI.print(e.getMessage());
            }
        }
    }

    /**
     * Processes user input and generates a response based on the given command.
     *
     * @param input The user input to be processed.
     * @return A response generated based on the user input.
     *         If the input is "bye", returns a farewell message.
     *         If the input is "list", returns a formatted string of tasks.
     *         For other inputs, parses the input into a command and executes it,
     *         returning the result of the command execution.
     *         If the input is not recognized or an exception occurs during processing,
     *         returns an appropriate error message.
     */
    public String getResponse(String input) {
        try {
            if (input.toLowerCase().equals("bye")) {
                return "Bye. Hope to see you again soon!";
            } else if (input.toLowerCase().equals("list")) {
                return UI.printTasks(this.tasks);
            } else {
                Command command = parser.parse(input);
                return command.execute(this.tasks, this.storage);
            }
        } catch (InvalidCommandException e) {
            return "Sorry, I don't think I quite understood what you meant...";
        } catch (InvalidDateException e) {
            return "Please ensure that you are inputting valid start and end dates.";
        } catch (InvalidSlashParameterException e) {
            return "Please ensure that you are inputting valid date parameters.";
        } catch (LuluException e) {
            return e.getMessage();
        }
    }

    /**
     * The main method to run the lulu.Lulu chatbot application.
     * Creates an instance of lulu.Lulu, starts the application, and handles exceptions.
     * Exits the application with a goodbye message.
     * @param args The command-line arguments (not used).
     */
    public static void main(String[] args) {
        Lulu chatbot = new Lulu();
        UI.start();
        try {
            chatbot.respond();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        UI.exit();
    }
}
