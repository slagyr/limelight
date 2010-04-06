//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.io;

import junit.framework.TestCase;

import java.io.File;

public class TempDirectoryTest extends TestCase
{
  private TempDirectory tempDirectory;

  public void setUp() throws Exception
  {
    tempDirectory = new TempDirectory();
  }

  public void tearDown() throws Exception
  {
    tempDirectory.cleanup(); 
  }

  public void testLocations() throws Exception
  {
    String systemTempDir = System.getProperty("java.io.tmpdir");
    String limelightTempDir = FileUtil.pathTo(systemTempDir, "limelight");
    assertEquals(limelightTempDir, tempDirectory.getRoot().getAbsolutePath());
    assertEquals(true, tempDirectory.getRoot().exists());
  }
  
  public void testCleanup() throws Exception
  {
    FileUtil.makeDir(FileUtil.pathTo(tempDirectory.getRoot().getPath(), "blah"));
    FileUtil.makeDir(FileUtil.pathTo(tempDirectory.getRoot().getPath(), "foo"));
    FileUtil.makeDir(FileUtil.pathTo(tempDirectory.getRoot().getPath(), "bar"));

    tempDirectory.cleanup();

    assertEquals(false, new File(FileUtil.pathTo(tempDirectory.getRoot().getPath(), "blah")).exists());
    assertEquals(false, new File(FileUtil.pathTo(tempDirectory.getRoot().getPath(), "foo")).exists());
    assertEquals(false, new File(FileUtil.pathTo(tempDirectory.getRoot().getPath(), "bar")).exists());
    assertEquals(false, tempDirectory.getRoot().exists());
  }

  public void testNewTempDir() throws Exception
  {
    File newDirectory = tempDirectory.createNewDirectory();

    assertNotNull(newDirectory);
    assertEquals(true, newDirectory.exists());
    assertEquals(tempDirectory.getRoot(), newDirectory.getParentFile());
  }
  
  public void testMultipleNewTempDirs() throws Exception
  {
    File temp1 = tempDirectory.createNewDirectory();
    File temp2 = tempDirectory.createNewDirectory();

    assertEquals(true, temp1.exists());
    assertEquals(tempDirectory.getRoot(), temp1.getParentFile());
    assertEquals(true, temp2.exists());
    assertEquals(tempDirectory.getRoot(), temp2.getParentFile());
    assertEquals(false, temp1.getPath().equals(temp2.getPath()));
  }

  public void testDownloadsDirectory() throws Exception
  {
    File downloadsDirectory = tempDirectory.getDownloadsDirectory();

    assertEquals(true, downloadsDirectory.exists());
    assertEquals(true, downloadsDirectory.isDirectory());
    assertEquals(tempDirectory.getRoot(), downloadsDirectory.getParentFile());
  }
}
