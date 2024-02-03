package command;

import exceptions.InvalidStatusUpdateException;
import storage.Storage;
import task.Task;
import tasklist.TaskList;
import ui.UI;

/**
 * The ManageCommand class represents a command to manage tasks, such as marking, unmarking, or deleting.
 * It extends the Command class and implements the execute method to perform the specified manage action.
 */
public class ManageCommand extends Command {

    /**
     * The type of manage command (MARK, UNMARK, DELETE).
     */
    private Command.Types type;

    /**
     * The index of the task to be managed.
     */
    private int index;

    /**
     * Constructs a ManageCommand with the specified command type and index.
     *
     * @param type  The type of manage command (MARK, UNMARK, DELETE).
     * @param index The index of the task to be managed.
     */
    public ManageCommand(Command.Types type, int index) {
        this.type = type;
        this.index = index;
    }

    /**
     * Executes the ManageCommand, performing the specified manage action on the given task list and storage.
     *
     * @param tasks   The TaskList on which the manage action is performed.
     * @param storage The Storage where changes are saved.
     */
    @Override
    public String execute(TaskList tasks, Storage storage) {
        switch (this.type) {
        case MARK:
            return mark(tasks, storage);
        case UNMARK:
            return unmark(tasks, storage);
        case DELETE:
            return delete(tasks, storage);
        default:
            return "";
        }
    }

    /**
     * Retrieves an empty string as test data associated with ManageCommand.
     *
     * @return An empty string used for testing.
     */
    @Override
    public String getTestData() {
        return "";
    }

    /**
     * Marks the task at the specified index as done, updating its status in the task list and storage.
     *
     * @param tasks   The TaskList from which the task is marked.
     * @param storage The Storage where changes are saved.
     */
    public String mark(TaskList tasks, Storage storage) {
        if (index >= tasks.getSize() || index < 0) {
            UI.print("Oops! You did not give a valid index.");
            return "Oops! You did not give a valid index.";
        }
        Task task = tasks.getTask(this.index);
        try {
            task.updateStatus(true);
            UI.print("Nice! I've marked this task as done:");
            UI.print(task);

            String result = "Nice! I've marked this task as done:\n";
            result += task;

            String line = storage.readLine(index);
            String newLine = line.substring(0, line.length() - 5) + "true";
            storage.updateLine(index, newLine);
            storage.updateLine(index, newLine);

            return result;
        } catch (InvalidStatusUpdateException e) {
            UI.print("This task was already marked!");
            UI.print(task);

            String result = "This task was already marked!\n";
            result += task;
            return result;
        }
    }

    /**
     * Unmarks the task at the specified index, updating its status in the task list and storage.
     *
     * @param tasks   The TaskList from which the task is unmarked.
     * @param storage The Storage where changes are saved.
     */
    public String unmark(TaskList tasks, Storage storage) {
        if (index >= tasks.getSize() || index < 0) {
            UI.print("Oops! You did not give a valid index.");
            return "Oops! You did not give a valid index.";
        }
        Task task = tasks.getTask(index);
        try {
            task.updateStatus(false);
            UI.print("OK, I've marked this task as not done yet:");
            UI.print(task);

            String result = "OK, I've marked this task as not done yet:\n";
            result += task;

            String line = storage.readLine(index);
            String newLine = line.substring(0, line.length() - 4) + "false";
            storage.updateLine(index, newLine);

            return result;
        } catch (InvalidStatusUpdateException e) {
            UI.print("This task was already unmarked!");
            UI.print(task);

            String result = "This task was already unmarked!\n";
            result += task;
            return result;
        }
    }

    /**
     * Deletes the task at the specified index, removing it from the task list and storage.
     *
     * @param tasks   The TaskList from which the task is deleted.
     * @param storage The Storage where changes are saved.
     */
    public String delete(TaskList tasks, Storage storage) {
        if (index >= tasks.getSize() || index < 0) {
            UI.print("Oops! You did not give a valid index.");
            return "Oops! You did not give a valid index.";
        }
        Task removed = tasks.deleteTask(index);
        storage.deleteLine(index);

        UI.print("Noted. I've removed this task:");
        UI.print("\t" + removed);
        UI.print(String.format("Now you have %d tasks in the list.", tasks.getSize()));

        String result = "Noted. I've removed this task:\n";
        result += "\t" + removed + "\n";
        result += String.format("Now you have %d tasks in the list.", tasks.getSize());
        return result;
    }
}
