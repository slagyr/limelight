//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.io;

import limelight.Context;
import limelight.os.MockOS;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataTest
{
  private MockOS os;
  private FakeFileSystem fs;

  @Before
  public void setUp()
  {
    Data.reset();
    os = new MockOS();
    Context.instance().os = os;
    fs = FakeFileSystem.installed();
  }

  @Test
  public void rootPath() throws Exception
  {
    assertEquals(os.dataRoot(), Data.getRoot());
  }

  @Test
  public void downloadsDir() throws Exception
  {
    final String expectedPath = fs.absolutePath(fs.join(Data.getRoot(), "Downloads"));
    assertEquals(expectedPath, Data.downloadsDir());
  }

  @Test
  public void productionsDir() throws Exception
  {
    final String expectedPath = fs.absolutePath(fs.join(Data.getRoot(), "Productions"));
    assertEquals(expectedPath, Data.productionsDir());
  }
}
