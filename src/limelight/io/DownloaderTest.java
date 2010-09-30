package limelight.io;

import limelight.Context;
import limelight.LimelightException;
import limelight.os.MockOS;
import limelight.util.TestUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class DownloaderTest
{
  private static String root = FileUtil.absolutePath(TestUtil.tmpDirPath("downloader"));

  private Downloader downloader;

  @Before
  public void setUp() throws Exception
  {
    FileUtil.deleteFileSystemDirectory(root);
    downloader = new Downloader(root);
  }

  @After
  public void tearDown() throws Exception
  {
    FileUtil.deleteFileSystemDirectory(root);
  }
  
  @Test
  public void directories() throws Exception
  {
    assertEquals(root, downloader.getDestinationRoot().getPath());
  }

  @Test
  public void defaultRootPath() throws Exception
  {
    MockOS os = new MockOS();
    os.dataRoot = root;
    Context.instance().os = os;
    assertEquals(Data.downloadsDir().getAbsolutePath(), new Downloader().getDestinationRoot().getAbsolutePath());
  }

  @Test
  public void downloadingFileOnTheLocalFilesystem() throws Exception
  {
    final String testFilePath = FileUtil.pathTo(root, "alt", "testFile.txt");
    FileUtil.establishFile(testFilePath, "some text");

    File result = downloader.download("file://" + testFilePath);

    assertEquals("testFile.txt", result.getName());
    assertEquals(root, result.getParentFile().getAbsolutePath());
    assertEquals(true, result.exists());
    assertEquals("some text", FileUtil.getFileContent(result));
  }
  
  @Test
  public void downloadingFileWithoutURLSyntax() throws Exception
  {
    final String testFilePath = FileUtil.pathTo(root, "alt", "testFile.txt");
    FileUtil.establishFile(testFilePath, "some text");

    File result = downloader.download(testFilePath);

    assertEquals("testFile.txt", result.getName());
    assertEquals(root, result.getParentFile().getAbsolutePath());
    assertEquals(true, result.exists());
    assertEquals("some text", FileUtil.getFileContent(result));
  }

  @Test
  public void uniqueFilenamesAreUsedForDownloading() throws Exception
  {
    final String testFilePath = FileUtil.pathTo(root, "alt", "testFile.txt");
    FileUtil.establishFile(testFilePath, "some text");

    File result1 = downloader.download(testFilePath);
    File result2 = downloader.download(testFilePath);
    File result3 = downloader.download(testFilePath);

    assertEquals("testFile.txt", result1.getName());
    assertEquals("testFile_2.txt", result2.getName());
    assertEquals("testFile_3.txt", result3.getName());
  }

  @Test
  public void throwsExceptionWithBadPath() throws Exception
  {
    final String resource = FileUtil.pathTo("blah", "blah", "blah");
    try
    {
      downloader.download(resource);
      fail("should throw exception");
    }
    catch(LimelightException e)
    {
      assertEquals("Failed to download resource: java.net.MalformedURLException: no protocol: " + resource, e.getMessage()); 
    }
  }
  
  @Test
  public void throwsExceptionWithBadHttpURL() throws Exception
  {
    final String resource = "http://blah.blah/blah.blah";
    try
    {
      downloader.download(resource);
      fail("should throw exception");
    }
    catch(LimelightException e)
    {
      assertEquals("Failed to download resource: java.net.UnknownHostException: blah.blah", e.getMessage());
    }
  }

//  The following tests should not run as part of the normal suite.  They are expensive and require the net.

//  @Test
//  public void downloadViaHTTP() throws Exception
//  {
//    File result = downloader.download("http://limelight.8thlight.com/images/logo.png");
//
//    assertEquals("logo.png", result.getName());
//    assertEquals(root, result.getParentFile().getAbsolutePath());
//    assertEquals(true, result.exists());
//  }
//
//  @Test
//  public void handle404s() throws Exception
//  {
//    try
//    {
//      downloader.download("http://limelight.8thlight.com/blah.blah");
//      fail("should throw exception");
//    }
//    catch(LimelightException e)
//    {
//      assertEquals("Failed to download resource: java.io.FileNotFoundException: http://limelight.8thlight.com/blah.blah", e.getMessage());
//    }
//  }
//
//  @Test
//  public void canHandleHTTPS() throws Exception
//  {
//    File result = downloader.download("https://www.google.com/images/logos/ssl_logo_lg.gif");
//
//    assertEquals("ssl_logo_lg.gif", result.getName());
//    assertEquals(root, result.getParentFile().getAbsolutePath());
//    assertEquals(true, result.exists());
//  }
//
//  @Test
//  public void canPullFilenameFromContentDisposition() throws Exception
//  {
//    File result = downloader.download("http://www.jtricks.com/download-text");
//
//    assertEquals("content.txt", result.getName());
//  }
//
//  @Test
//  public void downloadHandlesRedirects() throws Exception
//  {
//    File result = downloader.download("http://rubyforge.org/frs/download.php/37521/limelight-0.0.1-java.gem");
//
//    assertEquals("limelight-0.0.1-java.gem", result.getName());
//  }
}
