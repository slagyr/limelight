package limelight;

import limelight.io.FakeFileSystem;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.Handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LogTest
{
  @Before
  public void setUp()
  {
      Log.silence();
  }

  @Test
  public void stderrHandler() throws Exception
  {
    assertEquals(false, hasHandler(Log.stderrHandler));
    Log.stderrOn();
    assertEquals(true, hasHandler(Log.stderrHandler));
  }

  @Test
  public void silence() throws Exception
  {
    Log.stderrOn();
    assertEquals(1, Log.logger.getHandlers().length);
    Log.silence();
    assertEquals(0, Log.logger.getHandlers().length);
  }

  private boolean hasHandler(Handler desired)
  {
    boolean found = false;
    for(Handler handler : Log.logger.getHandlers())
    {
      if(handler == desired)
      {
        found = true;
        break;
      }
    }
    return found;
  }

  @Test
  public void settingLevelWithString() throws Exception
  {
    Log.setLevel("debug");
    assertEquals("DEBUG", Log.getLevelName());

    Log.setLevel("INFO");
    assertEquals("INFO", Log.getLevelName());

    Log.setLevel("info");
    assertEquals("INFO", Log.getLevelName());

    String level = null;
    Log.setLevel(level);
    assertEquals("OFF", Log.getLevelName());
  }

//  @Test
//  public void settingLogFile() throws Exception
//  {
//    Log.setLogFile("/tmp/foo");
//    assertNotNull(Log.fileHandler);
//    assertEquals(true, hasHandler(Log.fileHandler));
//    assertEquals(false, hasHandler(Log.stderrHandler));
//    assertEquals(Log.stderrHandler.getFormatter().getClass(), Log.fileHandler.getFormatter().getClass());
//  }
}
