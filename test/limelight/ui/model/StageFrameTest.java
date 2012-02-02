//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.ui.*;
import limelight.Context;
import limelight.os.MockOS;
import limelight.util.TestUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.awt.*;

import static org.junit.Assume.assumeTrue;

public class StageFrameTest extends Assert
{
  private MockStage stage;
  public MockGraphicsDevice graphicsDevice;
  private MockOS os;
  private StageFrame frame;

  @Before
  public void setUp() throws Exception
  {
    StageFrame.hiddenMode = false;
    assumeTrue(TestUtil.notHeadless());
    stage = new MockStage();
    frame = new StageFrame(stage);

    graphicsDevice = new MockGraphicsDevice();
    frame.setGraphicsDevice(graphicsDevice);

    os = new MockOS();
    Context.instance().os = os;
  }

  @After
  public void tearDown() throws Exception
  {
    try
    {
      frame.setVisible(false);
      frame.dispose();
    }
    catch(Exception e)
    {
      //ok
    }
  }

  @Test
  public void getsStage() throws Exception
  {
    assertSame(stage, frame.getStage());
  }

  @Test
  public void setFullScreenWhenNotVisible() throws Exception
  {
    frame.setFullScreen(true);

    assertEquals(true, frame.isFullScreen());
    frame.setVisible(true);
    assertEquals(frame, graphicsDevice.getFullScreenWindow());
  }

  @Test
  public void setFullScreenWhenVisible() throws Exception
  {
    frame.setVisible(true);
    frame.setFullScreen(true);

    assertEquals(true, frame.isFullScreen());
    assertEquals(frame, graphicsDevice.getFullScreenWindow());
  }

  @Test
  public void turnOffFullScreenWhiledisplayed() throws Exception
  {
    frame.setFullScreen(true);
    frame.setVisible(true);
    frame.setFullScreen(false);

    assertEquals(false, frame.isFullScreen());
    assertEquals(null, graphicsDevice.getFullScreenWindow());
  }

  @Test
  public void setKioskMode() throws Exception
  {
    frame.setKiosk(true);

    assertEquals(true, frame.isKiosk());
  }

  @Test
  public void kioskWillGoFullscreenAndFramelessWhenOpened() throws Exception
  {
    frame.setKiosk(true);
    frame.setVisible(true);

    assertEquals(frame, graphicsDevice.getFullScreenWindow());
    assertEquals(true, os.isInKioskMode());
  }

  @Test
  public void kioskWillGoFullscreenAndFramelessWhenClosed() throws Exception
  {
    frame.setKiosk(true);
    frame.setVisible(true);
    frame.setVisible(false);

    assertEquals(null, graphicsDevice.getFullScreenWindow());
    assertEquals(false, os.isInKioskMode());
  }

  @Test
  public void kioskModePreservesScreenSetting() throws Exception
  {
    frame.setFullScreen(false);
    frame.setKiosk(true);
    frame.setVisible(true);
    frame.setKiosk(false);

    assertEquals(null, graphicsDevice.getFullScreenWindow());
    assertEquals(false, os.isInKioskMode());
  }

  @Test
  public void kioskModePreservesScreenSettingWithFullscreenOn() throws Exception
  {
    frame.setFullScreen(true);
    frame.setKiosk(true);
    frame.setVisible(true);
    frame.setKiosk(false);

    assertEquals(frame, graphicsDevice.getFullScreenWindow());
    assertEquals(false, os.isInKioskMode());
  }

  @Test
  public void enterKioskModeWhileOpen() throws Exception
  {
    frame.setKiosk(false);
    frame.setVisible(true);
    frame.setKiosk(true);

    assertEquals(frame, graphicsDevice.getFullScreenWindow());
    assertEquals(true, os.isInKioskMode());
  }

  @Test
  public void hidingWhileInKioskMode() throws Exception
  {
    frame.setKiosk(true);
    frame.setVisible(true);
    frame.setVisible(false);

    assertEquals(null, graphicsDevice.getFullScreenWindow());
    assertEquals(false, os.isInKioskMode());
  }

  @Test
  public void showingAfterHidingWhileInKioskMode() throws Exception
  {
    frame.setKiosk(true);
    frame.setVisible(true);
    frame.setVisible(false);
    frame.setVisible(true);

    assertEquals(frame, graphicsDevice.getFullScreenWindow());
    assertEquals(true, os.isInKioskMode());
  }

  @Test
  public void fullscreenOffWhenInKioskMode() throws Exception
  {
    frame.setKiosk(true);
    frame.setFullScreen(true);
    frame.setVisible(true);

    frame.setFullScreen(false);
    assertEquals(frame, graphicsDevice.getFullScreenWindow());
  }

  @Test
  public void hideAndShowWithFullScreen() throws Exception
  {
    frame.setFullScreen(true);
    frame.setVisible(true);
    frame.setVisible(false);
    assertEquals(null, graphicsDevice.getFullScreenWindow());

    frame.setVisible(true);
    assertEquals(frame, graphicsDevice.getFullScreenWindow());
  }

  @Test
  public void settingFullScreenWhileHidden() throws Exception
  {
    frame.setVisible(true);
    frame.setVisible(false);
    frame.setFullScreen(true);
    assertEquals(null, graphicsDevice.getFullScreenWindow());
  }

  @Test
  public void notGetVisibleWhenInHiddenMode() throws Exception
  {
    StageFrame.hiddenMode = true;

    frame.setVisible(true);
    assertEquals(false, frame.isVisible());
  }
}
