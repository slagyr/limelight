package limelight.util;

import limelight.io.FileUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static junit.framework.Assert.assertEquals;

public class ResourceLoaderTest
{
  private String rootPath;
  private ResourceLoader loader;
  private String absoluteRootPath;

  @Before
  public void setUp() throws Exception
  {
    rootPath = TestUtil.tmpDirPath("resourceLoader");
    absoluteRootPath = new File(rootPath).getAbsolutePath();
    FileUtil.deleteFileSystemDirectory(rootPath);
    loader = ResourceLoader.forRoot(rootPath);
  }

  @After
  public void tearDown() throws Exception
  {
    FileUtil.deleteFileSystemDirectory(rootPath);
  }

  @Test
  public void hasRoot() throws Exception
  {
    assertEquals(rootPath, loader.getRoot());
  }

  @Test
  public void pathToAbsolutePath() throws Exception
  {
    assertEquals("/", loader.pathTo("/"));  
    assertEquals("/Users/micahmartin", loader.pathTo("/Users/micahmartin"));
  }

  @Test
  public void pathToRelativePath() throws Exception
  {
    assertEquals(absoluteRootPath + "/foo", loader.pathTo("foo"));
    assertEquals(absoluteRootPath + "/foo/bar.gif", loader.pathTo("foo/bar.gif"));
  }

  @Test
  public void knowsIfTheFileExists() throws Exception
  {
    FileUtil.establishFile(TestUtil.tmpDirPath("resourceLoader/foo.txt"), "blah");

    assertEquals(true, loader.exists("foo.txt"));
    assertEquals(false, loader.exists("bar.txt"));
  }

  @Test
  public void readsText() throws Exception
  {
    FileUtil.establishFile(TestUtil.tmpDirPath("resourceLoader/foo.txt"), "blah");

    assertEquals("blah", loader.readText("foo.txt"));
  }
}
