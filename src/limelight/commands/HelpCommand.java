package limelight.commands;

import limelight.CmdLineMain;
import limelight.util.Util;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HelpCommand extends Command
{
  private static Arguments arguments;
  public static Arguments arguments()
  {
    if(arguments == null)
    {
      arguments = new Arguments();
      arguments.addOptionalParameter("command", "prints help message for this command");
    }
    return arguments;
  }

  @Override
  public Arguments getArguments()
  {
    return arguments();
  }

  private List<Command> commands;

  public HelpCommand()
  {
  }

  public HelpCommand(String errorMessage)
  {
    sayError(errorMessage);
  }

  @Override
  public void doExecute(Map<String, String> options)
  {
    Command command = getCommandToHelp(options);
    if(command != null)
      command.help();
    else
    {
      sayError("Unrecognized command: " + options.get("command"));
      help();
    }
  }

  private Command getCommandToHelp(Map<String, String> options)
  {
    loadCommands();
    String commandName = options.get("command");
    if(commandName == null)
      return this;
    for(Command command : commands)
    {
      if(command.name().equals(commandName))
        return command;
    }
    return null;
  }

  @Override
  public void help()
  {
    loadCommands();
    StringBuffer buffer = new StringBuffer();
    buffer.append(Util.ENDL);
    buffer.append("usage: ");
    buffer.append(CmdLineMain.mainCmd).append(" [limelight options] <command> [command options]").append(Util.ENDL);
    buffer.append(Util.ENDL);
    buffer.append("commands:").append(Util.ENDL);
    buildCommandDescriptions(commands, buffer);
    buffer.append(Util.ENDL);
    buffer.append("For help with a specific command:").append(Util.ENDL);
    buffer.append("  ").append(CmdLineMain.mainCmd).append(" help <command>").append(Util.ENDL);
    say(buffer.toString());
  }

  private void buildCommandDescriptions(List<Command> commands, StringBuffer buffer)
  {
    int maxNameLength = findMaxNameLength(commands);

    for(Command command : commands)
    {
      buffer.append("  ").append(command.name());
      int spaces = maxNameLength - command.name().length();
      for(int i = 0; i < spaces; i++)
        buffer.append(" ");
      buffer.append("  ");
      buffer.append(command.description());
      buffer.append(Util.ENDL);
    }
  }

  private int findMaxNameLength(List<Command> commands)
  {
    int maxNameLength = 0;
    for(Command command : commands)
    {
      if(command.name().length() > maxNameLength)
        maxNameLength = command.name().length();
    }
    return maxNameLength;
  }

  private void loadCommands()
  {
    if(commands != null)
      return;

    commands = new LinkedList<Command>();
    for(Class<? extends Command> commandClass : CmdLineMain.commands.values())
      commands.add(Command.instance(commandClass));
  }

  public String description()
  {
    return "Prints this help message";
  }

  @Override
  public String name()
  {
    return "help";
  }
}
