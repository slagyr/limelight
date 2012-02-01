//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.commands;

import limelight.CmdLineMain;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class HelpCommandTest
{
  private HelpCommand command;
  private ByteArrayOutputStream output;

  @Before
  public void setUp() throws Exception
  {
    command = new HelpCommand();
    output = new ByteArrayOutputStream();
    Command.setOutput(new PrintStream(output));
  }

  @Test
  public void outputContainsDescription() throws Exception
  {
    command.execute();

    assertEquals(true, output.toString().contains(command.description()));
  }

  @Test
  public void outputContainsDescriptionOfEachCommand() throws Exception
  {
    command.execute();

    String printedOutput = output.toString();
    for(Class<? extends Command> commandClass : CmdLineMain.commands.values())
    {
      Command command = Command.instance(commandClass);
      assertEquals(true, printedOutput.contains(command.name()));
      assertEquals(true, printedOutput.contains(command.description()));
    }
  }

  @Test
  public void outputContainsCommandStructure() throws Exception
  {
    CmdLineMain.mainCmd = "java -jar limelight.jar";
    command.execute();
    String expected = "java -jar limelight.jar [limelight options] <command> [command options]";
    assertEquals(output.toString(), true, output.toString().contains(expected));
  }

  @Test
  public void outputContainsComandStructureWithMainScriptConfigured() throws Exception
  {
    CmdLineMain.mainCmd = "limelight";
    command.execute();
    String expected = "limelight [limelight options] <command> [command options]";
    assertEquals(true, output.toString().contains(expected));
  }

  @Test
  public void helpWithUnknownCommand() throws Exception
  {
    command.execute("blah");

    final String content = output.toString();
    assertEquals(true, content.contains("Unrecognized command: blah"));
  }

  @Test
  public void helpWithTheHelpCommand() throws Exception
  {
    command.execute("help");
    assertEquals(true, output.toString().contains("[limelight options] <command> [command options]"));
  }
}
