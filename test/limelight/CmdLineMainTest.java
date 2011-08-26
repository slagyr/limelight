//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight;

import limelight.commands.Arguments;
import limelight.commands.Command;
import limelight.commands.FakeCommand;
import limelight.commands.HelpCommand;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CmdLineMainTest
{
  private CmdLineMain main;
  private ByteArrayOutputStream output;

  @Before
  public void setUp() throws Exception
  {
    main = new CmdLineMain();
    output = new ByteArrayOutputStream();
    Command.setOutput(new PrintStream(output));
    CmdLineMain.addCommand("test_command", FakeCommand.class);
  }

  @Test
  public void hasHelpOption() throws Exception
  {
    Arguments.Option option = main.getArguments().findOption("help");

    assertNotNull(option);
    assertEquals("h", option.getShortName());
    assertEquals(false, option.requiresValue());
  }
  
  @Test
  public void hasCommandParameter() throws Exception
  {
    assertEquals(1, main.getArguments().parameterCount());
    assertEquals(true, main.getArguments().hasParameter("command"));
  }

  @Test
  public void helpCommandUsingName() throws Exception
  {
    main.run("help");
    assertNotNull(main.getCommand());
    assertEquals(HelpCommand.class, main.getCommand().getClass());
    assertEquals(true, main.getCommand().executed());
  }

  @Test
  public void helpCommandUsingOption() throws Exception
  {
    main.run("--help");
    assertNotNull(main.getCommand());
    assertEquals(HelpCommand.class, main.getCommand().getClass());
    assertEquals(true, main.getCommand().executed());
  }
  
  @Test
  public void unrecognizedCommand() throws Exception
  {
    main.run("blah");
    assertNotNull(main.getCommand());
    assertEquals(HelpCommand.class, main.getCommand().getClass());
    assertEquals(true, main.getCommand().executed());
  }
}
