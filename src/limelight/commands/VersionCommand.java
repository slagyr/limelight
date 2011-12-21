package limelight.commands;

import limelight.About;

import java.util.Map;

public class VersionCommand extends Command
{
  private static Arguments arguments = new Arguments();

  @Override
  public void doExecute(Map<String, String> args)
  {
    say("limelight " + About.version);
  }

  @Override
  public String description()
  {
    return "Prints current version";
  }

  @Override
  public String name()
  {
    return "version";
  }

  @Override
  public Arguments getArguments()
  {
    return arguments;
  }
}
