package limelight.io;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FileSystemTest
{
  private FileSystem fs;
  private String tmpDir;

  @Before
  public void setUp() throws Exception
  {
    fs = new FileSystem();
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
  public void buildPathEmpty() throws Exception
	{
		assertEquals("", fs.join());
	}

  @Test
	public void buildPathOneElement() throws Exception
	{
		assertEquals("a", fs.join("a"));
	}

  @Test
	public void buildPathThreeElements() throws Exception
	{
		String separator = System.getProperty("file.separator");
		assertEquals("a" + separator + "b" + separator + "c", fs.join("a", "b", "c"));
	}

  @Test
  public void baseName() throws Exception
  {
    assertEquals("foo", fs.baseName("foo"));
    assertEquals("foo", fs.baseName("bar/foo"));
    assertEquals("foo", fs.baseName("bar/foo.txt"));
  }

  @Test
  public void filename() throws Exception
  {
    assertEquals("/", fs.filename("/"));
    assertEquals("C:\\", fs.filename("C:\\"));
    assertEquals("one", fs.filename("/one"));
    assertEquals("two", fs.filename("/one/two"));
    assertEquals("two", fs.filename("one/two"));
    assertEquals("two.txt", fs.filename("one/two.txt"));
    assertEquals("two", fs.filename("one/two/"));
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
    assertEquals(false, fs.exists("file:" + tmpDir + "/file.txt"));

    fs.createTextFile(tmpDir + "/file.txt", "some text");

    assertEquals(true, fs.exists("file:" + tmpDir + "/file.txt"));
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
    final String dir2 = fs.join("file:" + tmpDir, "newDir2");
    fs.createDirectory(dir2);
    assertEquals(true, fs.exists(dir2));
    assertEquals(true, fs.isDirectory(dir2));
  }

  @Test
  public void canReadAndWriteUsingFileProtocol() throws Exception
  {
    withTmpDir();
    final String path = fs.join("file:" + tmpDir, "file.txt");

    fs.createTextFile(path, "I'm looking for a safe house.");

    assertEquals("I'm looking for a safe house.", fs.readTextFile(path));
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
    assertArrayEquals(new String[0], fs.fileListing("file:" + tmpDir));
    fs.createTextFile(fs.join("file:" + tmpDir, "file.txt"), "blah");
    assertArrayEquals(new String[]{"file.txt"}, fs.fileListing("file:" + tmpDir));
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
    final String path = fs.join("file:" + tmpDir, "file.txt");
    fs.createTextFile(path, "blah");

    final long millisSinceModified = System.currentTimeMillis() - fs.modificationTime(path);
    assertEquals(millisSinceModified + " millis ago", true, millisSinceModified < 1000);
  }
}