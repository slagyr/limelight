package limelight.commands;

import limelight.io.FileUtil;
import limelight.io.MockPacker;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnpackCommandTest
{
  private UnpackCommand command;
  private MockPacker packer;

  @Before
  public void setUp() throws Exception
  {
    command = new UnpackCommand();
    packer = new MockPacker();
    command.setPacker(packer);
  }

  @Test
  public void argumentsAreNotNull() throws Exception
  {
    assertNotNull(command.getArguments());
  }
  
  @Test
  public void unpacking() throws Exception
  {
    command.execute("/path/to/blah.llp");

    assertEquals("/path/to/blah.llp", packer.unpackPackagePath);
    assertEquals(FileUtil.currentPath(), packer.unpackDestination);
  }

  @Test
  public void unpackingWithOptionalDestination() throws Exception
  {
    command.execute("/path/to/blah.llp", "-d", "/unpack/dir");

    assertEquals("/path/to/blah.llp", packer.unpackPackagePath);
    assertEquals("/unpack/dir", packer.unpackDestination);
  }
}
