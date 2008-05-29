//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight;

import junit.framework.TestCase;
import limelight.io.Downloader;

public class MainTest extends TestCase
{
  private Main main;

  public void setUp() throws Exception
  {
    main = new Main();
    Context.removeInstance();
  }
  
  public void testTempFileIsAddedToContext() throws Exception
  {
    main.configureContext();

    assertNotNull(Context.instance().tempDirectory);
  }

  public void testDownloaderIsAddedToContext() throws Exception
  {
    main.configureContext();

    Downloader downloader = Context.instance().downloader;
    assertSame(Context.instance().tempDirectory, downloader.getTempDirectory());
  }
}
