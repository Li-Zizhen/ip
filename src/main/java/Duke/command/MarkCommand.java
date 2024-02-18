package Duke.command;
import Duke.*;

public class MarkCommand extends Command {
    private final int markIndex;
    public MarkCommand (int index) {
        this.markIndex = index;
    }

    @Override
    public String execute(TaskList taskList, Ui ui, Storage storage) throws IndexOutOfBoundsException {
        if (markIndex > taskList.accessNumberTask() || markIndex < 1) {
            throw new IndexOutOfBoundsException("Please only input index shown in the list");
        }
        String markedTaskInfo = taskList.mark(markIndex);
        String replyMessage = "Nice! I've marked this task as done:\n  " + markedTaskInfo;
        Ui.print_message(replyMessage);
        storage.writeDisk(taskList.accessList());
        return replyMessage;
    }
    @Override
    public boolean isExit() {
        return false;
    }
}
