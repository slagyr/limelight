//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.io;

import limelight.Context;
import limelight.LimelightException;
import limelight.os.MockOS;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class DownloaderTest
{
  private static final String dataRoot = "/limelight/data";
  private static final String downloadRoot = Context.fs().join(dataRoot, "Downloads");

  private Downloader downloader;
  private FakeFileSystem fs;
  private MockOS os;

  @Before
  public void setUp() throws Exception
  {
    Data.reset();
    
    os = new MockOS();
    os.dataRoot = dataRoot;
    Context.instance().os = os;

    fs = FakeFileSystem.installed();
    downloader = new Downloader();
  }
  
  @Test
  public void directories() throws Exception
  {
    assertEquals(downloadRoot, downloader.getDestinationRoot());
  }

  @Test
  public void defaultRootPath() throws Exception
  {
    assertEquals(Data.downloadsDir(), new Downloader().getDestinationRoot());
  }

  @Test
  public void downloadingFileOnTheLocalFilesystem() throws Exception
  {
    final String testFilePath = fs.join(dataRoot, "alt", "testFile.txt");
    fs.createTextFile(testFilePath, "some text");

    String result = downloader.download("file://" + testFilePath);

    assertEquals("testFile.txt", fs.filename(result));
    assertEquals(downloadRoot, fs.parentPath(result));
    assertEquals(true, fs.exists(result));
    assertEquals("some text", fs.readTextFile(result));
  }
  
  @Test
  public void downloadingFileWithoutURLSyntax() throws Exception
  {
    final String testFilePath = fs.join(dataRoot, "alt", "testFile.txt");
    fs.createTextFile(testFilePath, "some text");

    String result = downloader.download(testFilePath);

    assertEquals("testFile.txt", fs.filename(result));
    assertEquals(downloadRoot, fs.parentPath(result));
    assertEquals(true, fs.exists(result));
    assertEquals("some text", fs.readTextFile(result));
  }

  @Test
  public void uniqueFilenamesAreUsedForDownloading() throws Exception
  {
    final String testFilePath = fs.join(dataRoot, "alt", "testFile.txt");
    fs.createTextFile(testFilePath, "some text");

    String result1 = downloader.download(testFilePath);
    String result2 = downloader.download(testFilePath);
    String result3 = downloader.download(testFilePath);

    assertEquals("testFile.txt", fs.filename(result1));
    assertEquals("testFile_2.txt", fs.filename(result2));
    assertEquals("testFile_3.txt", fs.filename(result3));
  }

  @Test
  public void throwsExceptionWithBadPath() throws Exception
  {
    final String resource = fs.join("blah", "blah", "blah");
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


//  The following tests should not run as part of the normal suite.  They are expensive and require the net.
//
//  @Test
//  public void throwsExceptionWithBadHttpURL() throws Exception
//  {
//    final String resource = "http://blah.blah/blah.blah";
//    try
//    {
//      final String download = downloader.download(resource);
//      fail("should throw exception but got: " + download);
//    }
//    catch(LimelightException e)
//    {
//      assertEquals("Failed to download resource: java.net.UnknownHostException: blah.blah", e.getMessage());
//    }
//  }
//
//  @Test
//  public void downloadViaHTTP() throws Exception
//  {
//    String result = downloader.download("http://limelight.8thlight.com/images/logo.png");
//
//    assertEquals("logo.png", fs.filename(result));
//    assertEquals(downloadRoot, fs.parentPath(result));
//    assertEquals(true, fs.exists(result));
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
//    String result = downloader.download("https://www.google.com/images/logos/ssl_logo_lg.gif");
//
//    assertEquals("ssl_logo_lg.gif", fs.filename(result));
//    assertEquals(downloadRoot,  fs.parentPath(result));
//    assertEquals(true, fs.exists(result));
//  }
//
//  @Test
//  public void canPullFilenameFromContentDisposition() throws Exception
//  {
//    String result = downloader.download("http://www.jtricks.com/download-text");
//
//    assertEquals("content.txt", fs.filename(result));
//  }
//
//  @Test
//  public void downloadHandlesRedirects() throws Exception
//  {
//    String result = downloader.download("http://rubyforge.org/frs/download.php/37521/limelight-0.0.1-java.gem");
//
//    assertEquals("limelight-0.0.1-java.gem", fs.filename(result));
//  }
}
