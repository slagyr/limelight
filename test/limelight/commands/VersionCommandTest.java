package limelight.commands;

import limelight.About;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class VersionCommandTest
{
  private VersionCommand command;
  private ByteArrayOutputStream output;

  @Before
  public void setUp() throws Exception
  {
    command = new VersionCommand();
    output = new ByteArrayOutputStream();
    Command.setOutput(new PrintStream(output));
  }

  @Test
  public void outputContainsDescription() throws Exception
  {
    command.execute();

    assertEquals("limelight " + About.version, output.toString().trim());
  }
}
