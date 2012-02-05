//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.io;

import limelight.LimelightException;
import limelight.builtin.BuiltinBeacon;
import limelight.util.StringUtil;
import limelight.util.TestUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

public class FileSystemTest
{
  private FileSystem fs;
  private String tmpDir;
  private String jarPath;
  public String osRoot;
  public String SLASH;

  @Before
  public void setUp() throws Exception
  {
    fs = new FileSystem();
    jarPath = "jar:" + TestUtil.dataDirPath("calc.jar!");
    osRoot = new File("/").getAbsolutePath();
    SLASH = fs.separator();
  }

  @After
  public void tearDown() throws Exception
  {
    if(tmpDir != null)
      fs.delete(tmpDir);
  }

  private void withTmpDir()
  {
    tmpDir = fs.join(System.getProperty("java.io.tmpdir"), "fstest");
    fs.createDirectory(tmpDir);
  }

  @Test
  public void buildPathOneElement() throws Exception
  {
    assertEquals("a", fs.join("a"));
  }

  @Test
  public void buildPathThreeElements() throws Exception
  {
    assertEquals(StringUtil.join(fs.separator(), "a", "b", "c"), fs.join("a", "b", "c"));
  }

  @Test
  public void baseName() throws Exception
  {
    assertEquals("foo", fs.baseName("foo"));
    assertEquals("foo", fs.baseName(fs.join("bar", "foo")));
    assertEquals("foo", fs.baseName(fs.join("bar", "foo.txt")));
  }

  @Test
  public void filename() throws Exception
  {
    assertEquals("/", fs.filename("/"));
    assertEquals("C:\\", fs.filename("C:\\"));
    assertEquals("one", fs.filename(SLASH + "one"));
    assertEquals("two", fs.filename(SLASH + "one" + SLASH + "two"));
    assertEquals("two", fs.filename(fs.join("one", "two")));
    assertEquals("two.txt", fs.filename(fs.join("one", "two.txt")));
    assertEquals("two", fs.filename("one" + SLASH + "two" + SLASH));
  }

  @Test
  public void canTellFilesExist() throws Exception
  {
    withTmpDir();
    assertEquals(true, fs.exists(tmpDir));

    assertEquals(false, fs.exists(tmpDir + "/file.txt"));
    fs.createTextFile(tmpDir + "/file.txt", "some text");
    assertEquals(true, fs.exists(tmpDir + "/file.txt"));
  }

  @Test
  public void canTellFileExistsUsingFileProtocol() throws Exception
  {
    withTmpDir();
    final String file = fs.join(tmpDir, "file.txt");
    assertEquals(false, fs.exists(file));

    fs.createTextFile(file, "some text");

    assertEquals(true, fs.exists(file));
  }

  @Test
  public void canTellFileExistsUsingJarProtocol() throws Exception
  {
    assertEquals(false, fs.exists(jarPath + "blah.txt"));

    assertEquals(true, fs.exists(jarPath + "calculator.java/stages.xml"));
  }

  @Test
  public void createDirectory() throws Exception
  {
    withTmpDir();
    final String dir1 = fs.join(tmpDir + "newDir1");
    fs.createDirectory(dir1);
    assertEquals(true, fs.exists(dir1));
    assertEquals(true, fs.isDirectory(dir1));
  }

  @Test
  public void createDirectoryWithFileProtocol() throws Exception
  {
    withTmpDir();
    final String dir2 = fs.join(tmpDir, "newDir2");
    fs.createDirectory(dir2);
    assertEquals(true, fs.exists(dir2));
    assertEquals(true, fs.isDirectory(dir2));
  }

  @Test
  public void createDirectoryWithJarProtocol() throws Exception
  {
    try
    {
      fs.createDirectory(jarPath + "/newDir");
      fail("should have thrown an exception");
    }
    catch(LimelightException e)
    {
    }
  }

  @Test
  public void isDirectoryWithJarProtocol() throws Exception
  {
    assertEquals(true, fs.isDirectory(jarPath + "calculator.java/main"));
    assertEquals(false, fs.isDirectory(jarPath + "calculator.java/stages.xml"));
  }

  @Test
  public void canReadAndWriteUsingFileProtocol() throws Exception
  {
    withTmpDir();
    final String path = fs.join(tmpDir, "file.txt");

    fs.createTextFile(path, "I'm looking for a safe house.");

    assertEquals("I'm looking for a safe house.", fs.readTextFile(path));
  }

  @Test
  public void canReadUsingJarProtocol() throws Exception
  {
    assertEquals("<stages>", fs.readTextFile(jarPath + "calculator.java/stages.xml").substring(0, 8));
  }

  @Test
  public void fileListing() throws Exception
  {
    withTmpDir();
    assertArrayEquals(new String[0], fs.fileListing(tmpDir));
    fs.createTextFile(fs.join(tmpDir, "file.txt"), "blah");
    assertArrayEquals(new String[]{"file.txt"}, fs.fileListing(tmpDir));
  }

  @Test
  public void fileListingWithFileProtocol() throws Exception
  {
    withTmpDir();
    assertArrayEquals(new String[0], fs.fileListing(tmpDir));
    fs.createTextFile(fs.join(tmpDir, "file.txt"), "blah");
    assertArrayEquals(new String[]{"file.txt"}, fs.fileListing(tmpDir));
  }

  @Test
  public void fileListingWithJarProtocol() throws Exception
  {
    final String path = jarPath + "calculator.java/main";
    final String[] children = fs.fileListing(path);
    assertArrayEquals(new String[]{"players", "props.xml", "styles.xml"}, children);
  }

  @Test
  public void modificationTime() throws Exception
  {
    withTmpDir();
    final String path = fs.join(tmpDir, "file.txt");
    fs.createTextFile(path, "blah");

    final long millisSinceModified = System.currentTimeMillis() - fs.modificationTime(path);
    assertEquals(millisSinceModified + " millis ago", true, millisSinceModified < 1000);
  }

  @Test
  public void modificationTimeWithFileProtocol() throws Exception
  {
    withTmpDir();
    final String path = new File(tmpDir).toURI().toString() + "/file.txt";
    fs.createTextFile(path, "blah");

    final long millisSinceModified = System.currentTimeMillis() - fs.modificationTime(path);
    assertEquals(millisSinceModified + " millis ago", true, millisSinceModified < 1000);
  }

  @Test
  public void modificationTimeWithJarProtocol() throws Exception
  {
    long modTime = fs.modificationTime(jarPath + "calculator.java/stages.xml");
    GregorianCalendar date = new GregorianCalendar();
    date.setTime(new Date(modTime));
    assertEquals(2010, date.get(Calendar.YEAR));
  }

  @Test
  public void absolutePathWithFileProtocol() throws Exception
  {
    withTmpDir();
    final String expected = new File(tmpDir).toURI().toString();
    assertEquals(expected, fs.absolutePath(tmpDir));
  }

  @Test
  public void absolutePathWithJarProtocol() throws Exception
  {
    String result = fs.absolutePath(jarPath + "calculator.java/stages.xml");
    String expected = "jar:" + fs.absolutePath(TestUtil.dataDirPath("calc.jar")) + "!/calculator.java/stages.xml";

    assertEquals(expected, result);
  }

  @Test
  public void isAbsolute() throws Exception
  {
    assertEquals(true, fs.isAbsolute("file:C:/"));
    assertEquals(true, fs.isAbsolute("file:/"));
    assertEquals(true, fs.isAbsolute("file:/foo"));
    assertEquals(true, fs.isAbsolute("file:C:/foo"));
    assertEquals(true, fs.isAbsolute("jar:file:/foo.jar!/bar"));
    assertEquals(true, fs.isAbsolute("jar:file:C:/foo.jar!/bar"));
    if(fs.windows)
    {
      assertEquals(false, fs.isAbsolute("/foo"));
      assertEquals(true, fs.isAbsolute("C:\\foo"));
    }
    else
    {
      assertEquals(true, fs.isAbsolute("/foo"));
      assertEquals(false, fs.isAbsolute("C:\\foo"));
    }
    assertEquals(false, fs.isAbsolute("foo"));
    assertEquals(false, fs.isAbsolute("../foo"));
  }

  @Test
  public void isRoot() throws Exception
  {
    assertEquals(true, fs.isRoot("/"));
    assertEquals(true, fs.isRoot("jar:file:/foo!"));
    assertEquals(false, fs.isRoot("."));
    assertEquals(false, fs.isRoot("foo"));
    assertEquals(false, fs.isRoot("/foo"));
    assertEquals(false, fs.isRoot("jar:file:/foo!/bar"));
  }

  @Test
  public void parentPath() throws Exception
  {
    if(fs.windows)
    {
      assertEquals("file:/C:/", fs.parentPath("C:/"));
      assertEquals(fs.workingDir(), fs.parentPath("foo"));
      assertEquals("file:/C:/", fs.parentPath("C:/foo"));
      assertEquals("file:/C:/", fs.parentPath("C:/foo"));
      assertEquals("file:/C:/foo", fs.parentPath("C:/foo/bar"));
      assertEquals("file:/C:/foo", fs.parentPath("file:/C:/foo/bar"));
      assertEquals("file:/C:/", fs.parentPath("file:/C:/foo"));
      assertEquals("jar:file:/C:/foo!/", fs.parentPath("jar:file:/C:/foo!/bar"));
    }
    else
    {
      assertEquals("file:/", fs.parentPath("/"));
      assertEquals(fs.workingDir(), fs.parentPath("foo"));
      assertEquals("file:/", fs.parentPath("/foo"));
      assertEquals("file:/", fs.parentPath("/foo"));
      assertEquals("file:/foo", fs.parentPath("/foo/bar"));
      assertEquals("file:/foo", fs.parentPath("file:/foo/bar"));
      assertEquals("file:/", fs.parentPath("file:/foo"));
      assertEquals("jar:file:/foo!/", fs.parentPath("jar:file:/foo!/bar"));
    }
  }

  @Test
  public void pathTo() throws Exception
  {
    assertEquals("file:/source/destination", fs.pathTo("file:/source", "destination"));
    assertEquals("file:/destination", fs.pathTo("file:/source", "file:/destination"));
  }

  @Test
  public void relativePathTo() throws Exception
  {
    assertEquals(".", fs.relativePathBetween("file:/", "file:/"));
    assertEquals(".", fs.relativePathBetween("file:/origin", "file:/origin"));
    assertEquals("target", fs.relativePathBetween("file:/", "file:/target"));
    assertEquals(".." + fs.separator() + "target", fs.relativePathBetween("file:/origin", "file:/target"));
    assertEquals(".." + fs.separator() + ".." + fs.separator() + "target", fs.relativePathBetween("file:/origin/child", "file:/target"));
    assertEquals("child" + fs.separator() + "target", fs.relativePathBetween("file:/origin", "file:/origin/child/target"));
  }

  @Test
  public void relativePathToWithFakeFileSystem() throws Exception
  {
    final FakeFileSystem fake = new FakeFileSystem();
    fake.setWorkingDirectory("/working/dir");
    fs = fake;
    assertEquals("../dir/target", fs.relativePathBetween("/working/foo", "target"));
    assertEquals("../working/dir/target", fs.relativePathBetween("/origin", "target"));
    assertEquals("child", fs.relativePathBetween("origin", "origin/child"));
  }

  @Test
  public void URIPaths() throws Exception
  {
    assertEquals("file:/Projects", fs.absolutePath("file:/Projects"));
    assertEquals("file:/Projects", fs.absolutePath("file:Projects"));
    assertEquals("file:/C:/Projects", fs.absolutePath("file:/C:/Projects"));
    assertEquals("file:/C:/Projects", fs.absolutePath("file:C:/Projects"));
  }
}