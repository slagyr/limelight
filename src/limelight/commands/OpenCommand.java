//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.commands;

import limelight.Boot;
import limelight.Context;

import java.util.Map;

public class OpenCommand extends Command
{
  private static Arguments arguments;
  private boolean shouldBoot = true;

  public static Arguments arguments()
  {
    if(arguments == null)
    {
      arguments = new Arguments();
      arguments.addSwitchOption("r", "rigger", "Open the 'Rigger' scene too");
      arguments.addOptionalParameter("production", "Path to production directory, .llp file, or .lll file.  If none is provided, the Playbills production is opened.");
    }
    return arguments;
  }

  @Override
  public void doExecute(Map<String, String> args)
  {
    String production = args.get("production");
    if(production == null)
      production = defaultProduction();

    if(shouldBoot)
      Boot.boot();

    if(args.containsKey("rigger"))
      Context.instance().studio.utilitiesProduction().openRigger();

    Context.instance().studio.open(production);
  }

  @Override
  public String description()
  {
    return "Opens a Limelight Production";
  }

  @Override
  public String name()
  {
    return "open";
  }

  @Override
  public Arguments getArguments()
  {
    return arguments();
  }

  public String defaultProduction()
  {
    return Context.fs().join(Context.instance().limelightHome, "productions", "playbills.lll");
  }

  public void setShouldBoot(boolean value)
  {
    shouldBoot = value;
  }
}
