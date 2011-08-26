//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.commands;

import limelight.Context;
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
    assertEquals(Context.fs().workingDir(), packer.unpackDestination);
  }

  @Test
  public void unpackingWithOptionalDestination() throws Exception
  {
    command.execute("/path/to/blah.llp", "-d", "/unpack/dir");

    assertEquals("/path/to/blah.llp", packer.unpackPackagePath);
    assertEquals("/unpack/dir", packer.unpackDestination);
  }
}
