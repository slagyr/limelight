package limelight;

import limelight.commands.*;

import java.util.HashMap;
import java.util.Map;

public class CmdLineMain
{
  public static Map<String, Class<? extends Command>> commands = new HashMap<String, Class<? extends Command>>();
  public static String mainCmd = "java -jar limelight.jar";

  static
  {
    commands.put("help", HelpCommand.class);
    commands.put("open", OpenCommand.class);
    commands.put("pack", PackCommand.class);
    commands.put("unpack", UnpackCommand.class);
    commands.put("create", CreateCommand.class);
  }

  private Command command;

  public static void main(String[] args)
  {
    new CmdLineMain().run(args);
  }

  private Arguments arguments;

  public void run(String... args)
  {
    buildArguments();
    final Map<String, String> options = arguments.parse(args);
    if(options.containsKey("help"))
      command = new HelpCommand();
    else
      command = buildCommand(options);

    command.execute(arguments.leftOverArgs());
  }

  private Command buildCommand(Map<String, String> options)
  {
    String commandName = options.get("command");
    if(commandName == null)
      return new HelpCommand("Command missing");  

    Class<? extends Command> commandClass = commands.get(commandName);
    if(commandClass == null)
     return new HelpCommand("Unrecognized command: " + commandName);

    return Command.instance(commandClass);
  }

  public Arguments getArguments()
  {
    if(arguments == null)
      arguments = buildArguments();
    return arguments;
  }

  private Arguments buildArguments()
  {
    arguments = new Arguments();
    arguments.addParameter("command", "The name of the command to execute. Use --help for a listing of command.");
    arguments.addSwitchOption("h", "help", "Prints this help message");
    return arguments;
  }

  public Command getCommand()
  {
    return command;
  }

  public static void addCommand(String commandName, Class<? extends Command> commandClass)
  {
    commands.put(commandName, commandClass);
  }
}
