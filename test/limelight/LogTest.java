package limelight;

import org.junit.Before;
import org.junit.Test;

import java.util.logging.Handler;

import static org.junit.Assert.assertEquals;

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
    assertEquals(false, hashHandler(Log.stderrHandler));
    Log.stderrOn();
    assertEquals(true, hashHandler(Log.stderrHandler));
  }

  @Test
  public void silence() throws Exception
  {
    Log.stderrOn();
    assertEquals(1, Log.logger.getHandlers().length);
    Log.silence();
    assertEquals(0, Log.logger.getHandlers().length);
  }

  private boolean hashHandler(Handler desired)
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
}
