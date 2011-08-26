//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.ui.*;
import limelight.Context;
import limelight.os.MockOS;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.awt.*;

public class StageFrameTest extends Assert
{
  private MockStage stage;
  public MockGraphicsDevice graphicsDevice;
  private MockOS os;
  private Insets insets;
  private StageFrame frame;

  @Before
  public void setUp() throws Exception
  {
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
  public void shouldSetFullScreenWhenNotVisible() throws Exception
  {
    frame.setFullScreen(true);

    assertEquals(true, frame.isFullScreen());
    frame.setVisible(true);
    assertEquals(frame, graphicsDevice.getFullScreenWindow());
  }

  @Test
  public void shouldSetFullScreenWhenVisible() throws Exception
  {
    frame.setVisible(true);
    frame.setFullScreen(true);

    assertEquals(true, frame.isFullScreen());
    assertEquals(frame, graphicsDevice.getFullScreenWindow());
  }

  @Test
  public void shouldTurnOffFullScreenWhiledisplayed() throws Exception
  {
    frame.setFullScreen(true);
    frame.setVisible(true);
    frame.setFullScreen(false);

    assertEquals(false, frame.isFullScreen());
    assertEquals(null, graphicsDevice.getFullScreenWindow());
  }

  @Test
  public void shouldSetKioskMode() throws Exception
  {
    frame.setKiosk(true);

    assertEquals(true, frame.isKiosk());
  }

  @Test
  public void shouldKioskWillGoFullscreenAndFramelessWhenOpened() throws Exception
  {
    frame.setKiosk(true);
    frame.setVisible(true);

    assertEquals(frame, graphicsDevice.getFullScreenWindow());
    assertEquals(true, os.isInKioskMode());
  }

  @Test
  public void shouldKioskWillGoFullscreenAndFramelessWhenClosed() throws Exception
  {
    frame.setKiosk(true);
    frame.setVisible(true);
    frame.setVisible(false);

    assertEquals(null, graphicsDevice.getFullScreenWindow());
    assertEquals(false, os.isInKioskMode());
  }

  @Test
  public void shouldKioskModePreservesScreenSetting() throws Exception
  {
    frame.setFullScreen(false);
    frame.setKiosk(true);
    frame.setVisible(true);
    frame.setKiosk(false);

    assertEquals(null, graphicsDevice.getFullScreenWindow());
    assertEquals(false, os.isInKioskMode());
  }

  @Test
  public void shouldKioskModePreservesScreenSettingWithFullscreenOn() throws Exception
  {
    frame.setFullScreen(true);
    frame.setKiosk(true);
    frame.setVisible(true);
    frame.setKiosk(false);

    assertEquals(frame, graphicsDevice.getFullScreenWindow());
    assertEquals(false, os.isInKioskMode());
  }

  @Test
  public void shouldEnterKioskModeWhileOpen() throws Exception
  {
    frame.setKiosk(false);
    frame.setVisible(true);
    frame.setKiosk(true);

    assertEquals(frame, graphicsDevice.getFullScreenWindow());
    assertEquals(true, os.isInKioskMode());
  }

  @Test
  public void shouldHidingWhileInKioskMode() throws Exception
  {
    frame.setKiosk(true);
    frame.setVisible(true);
    frame.setVisible(false);

    assertEquals(null, graphicsDevice.getFullScreenWindow());
    assertEquals(false, os.isInKioskMode());
  }

  @Test
  public void shouldShowingAfterHidingWhileInKioskMode() throws Exception
  {
    frame.setKiosk(true);
    frame.setVisible(true);
    frame.setVisible(false);
    frame.setVisible(true);

    assertEquals(frame, graphicsDevice.getFullScreenWindow());
    assertEquals(true, os.isInKioskMode());
  }

  @Test
  public void shouldFullscreenOffWhenInKioskMode() throws Exception
  {
    frame.setKiosk(true);
    frame.setFullScreen(true);
    frame.setVisible(true);

    frame.setFullScreen(false);
    assertEquals(frame, graphicsDevice.getFullScreenWindow());
  }

  @Test
  public void shouldHideAndShowWithFullScreen() throws Exception
  {
    frame.setFullScreen(true);
    frame.setVisible(true);
    frame.setVisible(false);
    assertEquals(null, graphicsDevice.getFullScreenWindow());

    frame.setVisible(true);
    assertEquals(frame, graphicsDevice.getFullScreenWindow());
  }

  @Test
  public void shouldSettingFullScreenWhileHidden() throws Exception
  {
    frame.setVisible(true);
    frame.setVisible(false);
    frame.setFullScreen(true);
    assertEquals(null, graphicsDevice.getFullScreenWindow());
  }

  @Test
  public void doesNotgetVisibleWhenInHiddenMode() throws Exception
  {
    StageFrame.hiddenMode = true;

    frame.setVisible(true);
    assertEquals(false, frame.isVisible());
  }
}
