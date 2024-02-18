package Duke.command;
import Duke.*;

public class UnmarkCommand extends Command {
    private int unmarkIndex;
    public UnmarkCommand(int index) {
        this.unmarkIndex = index;
    }

    @Override
    public String execute(TaskList taskList, Ui ui, Storage storage) {
        if (unmarkIndex > taskList.accessNumberTask() || unmarkIndex < 1) {
            throw new IndexOutOfBoundsException("Please only input index shown in the list");
        }
        String unmarkedTaskInfo = taskList.unmark(unmarkIndex);
        String replyMessage = "OK, I've marked this task as not done yet:\n  " + unmarkedTaskInfo;
        Ui.print_message(replyMessage);
        storage.writeDisk(taskList.accessList());
        return replyMessage;
    }
    @Override
    public boolean isExit() {
        return false;
    }

}
