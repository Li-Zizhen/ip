package zhen;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import zhen.command.AddCommand;
import zhen.command.Command;
import zhen.command.DeleteCommand;
import zhen.command.DontknowCommand;
import zhen.command.ExitCommand;
import zhen.command.FindCommand;
import zhen.command.ListCommand;
import zhen.command.MarkCommand;
import zhen.command.ShowErrorCommand;
import zhen.command.TagCommand;
import zhen.command.UnmarkCommand;

import zhen.task.Deadline;
import zhen.task.Event;
import zhen.task.Todos;
/**
 * Parser class parses the input from user to commands.
 */
public class Parser {
    /**
     * Parses the data in string format to the specified LocalDate format.
     *
     * @param date String representation of a date.
     * @return LocalDate representation of the data inputted by the user.
     */
    public static LocalDate parseDate(String date) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate formattedTime = LocalDate.parse(date, timeFormatter);
        return formattedTime;
    }

    /**
     * Parses the string format command from the user to command that could be executed by the program.
     *
     * @param userInput The input from users.
     * @return Command object that represent the action to be performed.
     */
    public static Command parse(String userInput) {
        if (parseAddCommand(userInput) != null) {
            return parseAddCommand(userInput);
        }
        if (parseMarkingCommands(userInput) != null) {
            return parseMarkingCommands(userInput);
        }
        if (parseDeleteCommand(userInput) != null) {
            return parseDeleteCommand(userInput);
        }
        if (parseFindCommand(userInput) != null) {
            return parseFindCommand(userInput);
        }
        if (parseTagCommand(userInput) != null) {
            return parseTagCommand(userInput);
        }
        if (parseSingleWordCommands(userInput) != null) {
            return parseSingleWordCommands(userInput);
        } else {
            return new DontknowCommand();
        }
    }

    /**
     * Parses user's adding task request.
     *
     * @param userInput The input from users.
     * @return Command that can add task to the task list when executed.
     */
    private static Command parseAddCommand(String userInput) {
        if (userInput.length() >= 5 && userInput.substring(0, 5).equals("todo ")) {
            return new AddCommand(new Todos(userInput.substring(4)));
        } else if (userInput.length() >= 9 && userInput.substring(0, 9).equals("deadline ")) {
            String[] deadlineInfo = processDeadlineMsg(userInput.substring(8));
            try {
                return new AddCommand(new Deadline(deadlineInfo[0], parseDate(deadlineInfo[1])));
            } catch (Exception e) {
                return new AddCommand(new Deadline(deadlineInfo[0], deadlineInfo[1]));
            }
        } else if (userInput.length() >= 6 && userInput.substring(0, 6).equals("event ")) {
            String[] eventInfo = processEventMsg(userInput.substring(5));
            try {
                return new AddCommand(new Event(eventInfo[0], parseDate(eventInfo[1]), parseDate(eventInfo[2])));
            } catch (Exception e) {
                return new AddCommand(new Event(eventInfo[0], eventInfo[1], eventInfo[2]));
            }
        }
        return null;
    }

    /**
     * Parses user's marking/unmarking task request.
     *
     * @param userInput The input from users.
     * @return Command that can mark or unmark a task with particular index.
     */
    private static Command parseMarkingCommands(String userInput) {
        if (userInput.length() >= 5 && userInput.substring(0, 5).equals("mark ")) {
            try {
                int number = Integer.parseInt(userInput.substring(5));
                return new MarkCommand(number);
            } catch (Exception E) {
                return new ShowErrorCommand("Please follow the input format: mark [task index]");
            }
        } else if (userInput.length() >= 7 && userInput.substring(0, 7).equals("unmark ")) {
            try {
                int number = Integer.parseInt(userInput.substring(7));
                return new UnmarkCommand(number);
            } catch (Exception e) {
                return new ShowErrorCommand("Please follow the input format: unmark [task index]");
            }
        }
        return null;
    }

    /**
     * Parses user's deleting task request.
     *
     * @param userInput The input from users.
     * @return Command that can delete a task with particular index.
     */
    private static Command parseDeleteCommand(String userInput) {
        if (userInput.length() >= 7 && userInput.substring(0, 7).equals("delete ")) {
            try {
                int number = Integer.parseInt(userInput.substring(7));
                return new DeleteCommand(number);
            } catch (Exception e) {
                return new ShowErrorCommand("Please follow the input format: delete [task index]");
            }
        }
        return null;
    }

    /**
     * Parses user's adding tag request.
     *
     * @param userInput The input from users.
     * @return Command that can add tag to the task with particular index.
     */
    private static Command parseTagCommand(String userInput) {
        if (userInput.length() >= 4 && userInput.substring(0, 4).equals("tag ")) {
            try {
                String[] tagInfo = processTagMsg(userInput);
                int number = Integer.parseInt(tagInfo[0]);
                String tagMessage = tagInfo[1];
                return new TagCommand(number, tagMessage);
            } catch (Exception e) {
                return new ShowErrorCommand("Please follow the input format: tag [task index] [tag] ");
            }
        }
        return null;
    }

    /**
     * Parses user's finding tasks request.
     *
     * @param userInput The input from users.
     * @return Command that find tasks with particular keywords.
     */
    private static Command parseFindCommand(String userInput) {
        if (userInput.length() >= 5 && userInput.substring(0, 5).equals("find ")) {
            try {
                String stringToFind = userInput.substring(5);
                return new FindCommand(stringToFind);
            } catch (Exception e) {
                return new ShowErrorCommand("Please Follow the input format: find [keyword]");
            }
        }
        return null;
    }
    /**
     * Parses user's single word input such as bye and list.
     *
     * @param userInput The input from users.
     * @return Command that lists all tasks or exits the program.
     */
    private static Command parseSingleWordCommands(String userInput) {
        switch (userInput) {
        case "bye":
            return new ExitCommand();
        case "list":
            return new ListCommand();
        default:
            return null;
        }
    }
    /**
     * Extracts important information in user's adding event request and organizes them into a list of String.
     *
     * @param userInput User's input with only informative parts, i.e. without the command part.
     * @return A list of String with three elements, the first element is the information about the event,
     * the second element is the starting time of the event, and the third element is the ending time of the event.
     */
    static String[] processEventMsg(String userInput) {
        String[] eventDetails = new String[3];
        String[] splitByFrom = userInput.split("/from");
        if (splitByFrom.length >= 2) {
            eventDetails[0] = splitByFrom[0].trim();
            String[] strArr = splitByFrom[1].split("/to");
            eventDetails[1] = strArr[0].trim();
            if (strArr.length == 2) {
                eventDetails[2] = strArr[1].trim();
            } else {
                eventDetails[2] = "";
            }
        } else {
            String[] strArr = splitByFrom[0].split("/to");
            if (strArr.length >= 2) {
                eventDetails[0] = strArr[0].trim();
                eventDetails[1] = "";
                eventDetails[2] = strArr[1].trim();
            } else {
                eventDetails[0] = strArr[0].trim();
                eventDetails[1] = "";
                eventDetails[2] = "";
            }
        }
        return eventDetails;
    }
    /**
     * Extracts important information in user's adding deadline request and organizes them into a list of String.
     *
     * @param userInput User's input with only informative parts, i.e. without the command part.
     * @return A list of String of two elements, the first element is the information about the event,
     * the second element is the deadline of the event.
     */
    static String[] processDeadlineMsg(String userInput) {
        String[] deadlineDetails = new String[2];
        String[] splitByBy = userInput.split("/by");
        if (splitByBy.length >= 2) {
            deadlineDetails[0] = splitByBy[0].trim();
            deadlineDetails[1] = splitByBy[1].trim();
        } else {
            deadlineDetails[0] = (splitByBy[0].trim());
            deadlineDetails[1] = "";
        }
        return deadlineDetails;
    }
    /**
     * Extracts important information in user's adding tag request and organizes them into a list of String.
     *
     * @param userInput User's full input.
     * @return A list of String of two elements, the first element is the index of task to be tagged,
     * the second element is the tag's message.
     */
    static String[] processTagMsg(String userInput) {
        String[] tagDetails = new String[2];
        String[] splitBySpace = userInput.split(" ");
        if (!splitBySpace[0].equals("tag")) {
            throw new RuntimeException();
        }
        if (splitBySpace.length >= 3) {
            tagDetails[0] = splitBySpace[1].trim();
            tagDetails[1] = splitBySpace[2].trim();
        } else {
            throw new RuntimeException();
        }
        return tagDetails;
    }
}
