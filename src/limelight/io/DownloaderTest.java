//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.io;

import junit.framework.TestCase;
import limelight.LimelightException;
import limelight.util.TestUtil;
import java.io.File;

public class DownloaderTest extends TestCase
{
  private String testFilePath;
  private Downloader downloader;
  private TempDirectory tempDirectory;

  public void setUp() throws Exception
  {
    tempDirectory = new TempDirectory();
    downloader = new Downloader(tempDirectory);
    testFilePath = FileUtil.pathTo(TestUtil.TMP_DIR, "test_file.txt");
  }

  public void tearDown() throws Exception
  {
    FileUtil.deleteFile(testFilePath);
    tempDirectory.cleanup();
  }

  public void testDownloaingLocalFile() throws Exception
  {
    FileUtil.createFile(testFilePath, "a test file");

    File file = downloader.download(testFilePath);

    assertEquals(testFilePath, file.getPath());
  }

  public void testDownloadingLocalFileWithFileProtocol() throws Exception
  {
    FileUtil.createFile(testFilePath, "a test file");

    String absolutePath = new File(testFilePath).getAbsolutePath();
    File file = downloader.download("file://" + absolutePath);

    assertEquals(absolutePath, file.getPath());
  }

  public void testMissingLocalFile() throws Exception
  {
    try
    {
      downloader.download(testFilePath);
      fail("should rais exception");
    }
    catch(LimelightException e)
    {
      assertEquals("Not found: " + testFilePath, e.getMessage());
    }
  }

  public void testDownloadFailure() throws Exception
  {
    try
    {
      downloader.download("http://blahblahblah.blah/blah.blah");
      fail("should throw exception");
    }
    catch(LimelightException e)
    {
    }
  }

// - These tests hit web sites over the internet and should not be start as part of the normal test suite.
//
//  public void testDownloadingViaHTTP() throws Exception
//  {
//    File file = downloader.download("http://limelight.8thlight.com/images/logo.png");
//
//    assertEquals("logo.png", file.getName());
//    assertEquals("downloads", file.getParentFile().getName());
//    assertEquals(tempDirectory.getRoot(), file.getParentFile().getParentFile());
//  }
//
//  public void testDownloadingViaHTTPNotFound() throws Exception
//  {
//    try
//    {
//      downloader.download("http://limelight.8thlight.com/blah.blah");
//      fail("should throw exception");
//    }
//    catch(LimelightException e)
//    {
//      assertEquals("Not found: http://limelight.8thlight.com/blah.blah", e.getMessage());
//    }
//  }
//
//  // Doesn't work because the 8th Light certificate is not signed.
//  public void testDownloadingViaHTTPS() throws Exception
//  {
//    File file = downloader.download("https://www.8thlight.com/images/header.jpg");
//
//    assertEquals("header.jpg", file.getName());
//    assertEquals("downloads", file.getParentFile().getName());
//    assertEquals(tempDirectory.getRoot(), file.getParentFile().getParentFile());
//  }
}
