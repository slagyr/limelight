//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.io;

import limelight.util.TestUtil;
import org.junit.Test;

import java.io.File;

import static junit.framework.Assert.assertEquals;

public class FileUtilTest
{
  @Test
  public void buildPathEmpty() throws Exception
	{
		assertEquals("", FileUtil.buildPath());
	}

  @Test
	public void buildPathOneElement() throws Exception
	{
		assertEquals("a", FileUtil.buildPath("a"));
	}

  @Test
	public void buildPathThreeElements() throws Exception
	{
		String separator = System.getProperty("file.separator");
		assertEquals("a" + separator + "b" + separator + "c", FileUtil.buildPath("a", "b", "c"));
	}

  @Test
  public void currentPath() throws Exception
  {
    String currentPath = FileUtil.currentPath();
    File currentPathFile = new File(FileUtil.buildPath(currentPath, "."));
    File dotFile = new File(".");
    assertEquals(dotFile.getAbsolutePath(), currentPathFile.getAbsolutePath());
  }

  @Test
  public void createDir() throws Exception
	{
		File dir = FileUtil.makeDir(FileUtil.pathTo(TestUtil.TMP_DIR, "temp"));
		assertEquals(true, dir.exists());
		assertEquals(true, dir.isDirectory());
		FileUtil.deleteFileSystemDirectory(dir);
	}

  @Test
  public void createFile() throws Exception
  {
    String filePath = FileUtil.pathTo(TestUtil.TMP_DIR, "test.txt");

    FileUtil.createFile(filePath, "blah");
    assertEquals(true, new File(filePath).exists());
    assertEquals("blah", FileUtil.getFileContent(filePath));

    FileUtil.deleteFile(filePath);
    assertEquals(false, new File(filePath).exists());
  }

  @Test
  public void baseName() throws Exception
  {
    assertEquals("foo", FileUtil.baseName("foo"));
    assertEquals("foo", FileUtil.baseName("bar/foo"));
    assertEquals("foo", FileUtil.baseName("bar/foo.txt"));  
  }
}
