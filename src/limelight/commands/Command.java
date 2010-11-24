//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.commands;

import limelight.CmdLineMain;
import limelight.LimelightException;

import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.util.Map;

public abstract class Command
{
  private static PrintStream output = System.out;
  private boolean executed;

  public abstract void doExecute(Map<String, String> args);

  public abstract String description();

  public abstract String name();

  public abstract Arguments getArguments();

  public static void setOutput(PrintStream printStream)
  {
    output = printStream;
  }

  public boolean executed()
  {
    return executed;
  }

  public void execute(String... args)
  {
    Map<String,String> options = getArguments().parse(args);
    if(getArguments().success())
      doExecute(options);
    else
    {
      sayError(getArguments().getMessage());
      help();
    }
    executed = true;
  }

  protected void say(String message)
  {
    output.println(message);
  }

  protected void sayError(String message)
  {
    say("");
    say("ERROR! " + message);
  }

  public static Command instance(Class<? extends Command> commandClass)
  {
    try
    {
      final Constructor<? extends Command> constructor = commandClass.getConstructor();
      return constructor.newInstance();
    }
    catch(Exception e)
    {
      throw new LimelightException("Failed to instantiate Command: " + commandClass.getName(), e);
    }
  }

  public void help()
  {
    say("");
    say(name() + ":  " + description());
    say("");
    say("Usage: " + CmdLineMain.mainCmd + " " + name() + " " + getArguments().argString());
    say("");
    if(getArguments().hasParameters())
    {
      say("parameters:");
      say(getArguments().parametersString());
    }
    if(getArguments().hasOptions())
    {
      say("options:");
      say(getArguments().optionsString());
    }
  }

  protected String getArgOrDefault(Map<String, String> args, String argName, String defaultValue)
  {
    String value = args.get(argName);
    if(value == null)
      value = defaultValue;
    return value;
  }
}
