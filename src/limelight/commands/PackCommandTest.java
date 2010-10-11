package limelight.commands;

import limelight.io.MockPacker;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PackCommandTest
{
  private PackCommand command;
  private MockPacker packer;

  @Before
  public void setUp() throws Exception
  {
    command = new PackCommand();
    packer = new MockPacker();
    command.setPacker(packer);
  }

  @Test
  public void argumentsAreNotNull() throws Exception
  {
    assertNotNull(command.getArguments());
  }

  @Test
  public void packWithDefaultName() throws Exception
  {
    command.execute("/path/to/blah");

    assertEquals("/path/to/blah", packer.packProductionPath);
    assertEquals("blah", packer.packLlpName);
  }
  
  @Test
  public void packWithOptionalName() throws Exception
  {
    command.execute("/path/to/blah", "-n", "foo");

    assertEquals("/path/to/blah", packer.packProductionPath);
    assertEquals("foo", packer.packLlpName);
  }
}
