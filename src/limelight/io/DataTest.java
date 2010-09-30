package limelight.io;

import limelight.Context;
import limelight.os.MockOS;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataTest
{
  private MockOS os;

  @Before
  public void setUp()
  {
    Data.reset();
    os = new MockOS();
    Context.instance().os = os;
  }

  @After
  public void tearDown() throws Exception
  {
    FileUtil.deleteFileSystemDirectory(Data.downloadsDir());
    FileUtil.deleteFileSystemDirectory(Data.productionsDir());
  }

  @Test
  public void rootPath() throws Exception
  {
    assertEquals(os.dataRoot(), Data.getRoot().getPath());
  }
  
  @Test
  public void downloadsDir() throws Exception
  {
    final String expectedPath = FileUtil.absolutePath(FileUtil.pathTo(Data.getRoot().getAbsolutePath(), "Downloads"));
    assertEquals(expectedPath, Data.downloadsDir().getAbsolutePath());
  }

  @Test
  public void productionsDir() throws Exception
  {
    final String expectedPath = FileUtil.absolutePath(FileUtil.pathTo(Data.getRoot().getAbsolutePath(), "Productions"));
    assertEquals(expectedPath, Data.productionsDir().getAbsolutePath());
  }

  @Test
  public void establishDirs() throws Exception
  {
    assertEquals(false, Data.downloadsDir().exists());
    assertEquals(false, Data.productionsDir().exists());

    Data.establishDirs();
    assertEquals(true, Data.downloadsDir().exists());
    assertEquals(true, Data.productionsDir().exists());
  }
}
