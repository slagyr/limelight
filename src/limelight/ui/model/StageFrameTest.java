//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import junit.framework.TestCase;
import limelight.ui.api.MockStage;
import limelight.ui.*;
import limelight.Context;
import limelight.KeyboardFocusManager;
import limelight.os.MockOS;
import limelight.util.Colors;

import javax.swing.*;
import java.awt.*;

public class StageFrameTest extends TestCase
{
  private MockStage stage;
  private StageFrame frame;
  private FrameManager frameManager;
  public GraphicsDevice graphicsDevice;
  private MockOS os;

  public void setUp() throws Exception
  {
    frameManager = new InertFrameManager();
    Context.instance().frameManager = frameManager;
    Context.instance().keyboardFocusManager = new KeyboardFocusManager();

    stage = new MockStage();
    frame = new StageFrame(stage);

    graphicsDevice = new MockGraphicsDevice();
    frame.setGraphicsDevice(graphicsDevice);

    os = new MockOS();
    Context.instance().os = os;
  }

  public void testIcon() throws Exception
  {
    assertNotNull(frame.getIconImage());
  }

  public void testStage() throws Exception
  {
    assertSame(stage, frame.getStage());
  }

  public void testLoad() throws Exception
  {
    MockPanel panel = new MockPanel();
    frame.load(panel);

    RootPanel root = frame.getRoot();

    assertSame(panel, root.getPanel());
  }

  public void testLoadSetsDefaultCursor() throws Exception
  {
    frame.getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    MockPanel panel = new MockPanel();
    frame.load(panel);

    assertEquals(Cursor.DEFAULT_CURSOR, frame.getContentPane().getCursor().getType());
  }

  public void testLoadWillDestroyPreviousRoots() throws Exception
  {
    MockPanel panel = new MockPanel();
    frame.load(panel);

    RootPanel firstRoot = frame.getRoot();
    assertEquals(true, firstRoot.isAlive());

    MockPanel panel2 = new MockPanel();
    frame.load(panel2);

    assertEquals(false, firstRoot.isAlive());
  }

  public void testAddsSelfToFrameManager() throws Exception
  {
    assertEquals(1, frameManager.getFrameCount());
    assertEquals(true, frameManager.isWatching(frame));
  }

  public void testDefaultCloseOperations() throws Exception
  {
    assertEquals(WindowConstants.DISPOSE_ON_CLOSE, frame.getDefaultCloseOperation());
  }

  public void testSetFullScreenWhenNotVisible() throws Exception
  {
    frame.setFullScreen(true);

    assertEquals(true, frame.isFullScreen());
    frame.open();
    assertEquals(frame, graphicsDevice.getFullScreenWindow());
  }

  public void testSetFullScreenWhenVisible() throws Exception
  {
    frame.open();
    frame.setFullScreen(true);

    assertEquals(true, frame.isFullScreen());
    assertEquals(frame, graphicsDevice.getFullScreenWindow());
  }

  public void testTurnOffFullScreenWhiledisplayed() throws Exception
  {
    frame.setFullScreen(true);
    frame.open();
    frame.setFullScreen(false);

    assertEquals(false, frame.isFullScreen());
    assertEquals(null, graphicsDevice.getFullScreenWindow());
  }

  public void testSetKioskMode() throws Exception
  {
    frame.setKiosk(true);

    assertEquals(true, frame.isKiosk());
  }

  public void testKioskWillGoFullscreenAndFramelessWhenOpened() throws Exception
  {
    frame.setKiosk(true);
    frame.open();

    assertEquals(frame, graphicsDevice.getFullScreenWindow());
    assertEquals(true, os.isInKioskMode());
  }

  public void testKioskWillGoFullscreenAndFramelessWhenClosed() throws Exception
  {
    frame.setKiosk(true);
    frame.open();
    frame.close();

    assertEquals(null, graphicsDevice.getFullScreenWindow());
    assertEquals(false, os.isInKioskMode());
  }

  public void testKioskModePreservesScreenSetting() throws Exception
  {
    frame.setFullScreen(false);
    frame.setKiosk(true);
    frame.open();
    frame.setKiosk(false);

    assertEquals(null, graphicsDevice.getFullScreenWindow());
    assertEquals(false, os.isInKioskMode());
  }

  public void testKioskModePreservesScreenSettingWithFullscreenOn() throws Exception
  {
    frame.setFullScreen(true);
    frame.setKiosk(true);
    frame.open();
    frame.setKiosk(false);

    assertEquals(frame, graphicsDevice.getFullScreenWindow());
    assertEquals(false, os.isInKioskMode());
  }

  public void testEnterKioskModeWhileOpen() throws Exception
  {
    frame.setKiosk(false);
    frame.open();
    frame.setKiosk(true);

    assertEquals(frame, graphicsDevice.getFullScreenWindow());
    assertEquals(true, os.isInKioskMode());
  }

  public void testHidingWhileInKioskMode() throws Exception
  {
    frame.setKiosk(true);
    frame.open();
    frame.setVisible(false);

    assertEquals(null, graphicsDevice.getFullScreenWindow());
    assertEquals(false, os.isInKioskMode());
  }

  public void testShowingAfterHidingWhileInKioskMode() throws Exception
  {
    frame.setKiosk(true);
    frame.open();
    frame.setVisible(false);
    frame.setVisible(true);

    assertEquals(frame, graphicsDevice.getFullScreenWindow());
    assertEquals(true, os.isInKioskMode());
  }

  public void testFullscreenOffWhenInKioskMode() throws Exception
  {
    frame.setKiosk(true);
    frame.setFullScreen(true);
    frame.open();

    frame.setFullScreen(false);
    assertEquals(frame, graphicsDevice.getFullScreenWindow());
  }

  public void testHideAndShow() throws Exception
  {
    frame.open();
    assertEquals(true, frame.isVisible());

    frame.setVisible(false);
    assertEquals(false, frame.isVisible());

    frame.setVisible(true);
    assertEquals(true, frame.isVisible());
  }

  public void testHideAndShowWithFullScreen() throws Exception
  {
    frame.setFullScreen(true);
    frame.open();
    frame.setVisible(false);
    assertEquals(null, graphicsDevice.getFullScreenWindow());

    frame.setVisible(true);
    assertEquals(frame, graphicsDevice.getFullScreenWindow());
  }

  public void testSettingFullScreenWhileHidden() throws Exception
  {
    frame.open();
    frame.setVisible(false);
    frame.setFullScreen(true);
    assertEquals(null, graphicsDevice.getFullScreenWindow());
  }

  public void testSetBackgroundColor() throws Exception
  {
    frame.setBackgroundColor("blue");
    assertEquals(Colors.resolve("blue"), frame.getBackground());
    assertEquals("#0000FF", frame.getBackgroundColor());

    frame.setBackgroundColor("#abc");
    assertEquals(Colors.resolve("#abc"), frame.getBackground());
    assertEquals("#AABBCC", frame.getBackgroundColor());
  }
  
  public void testShouldRetainSizeAndLocationWhenComingOutOfFullscreen() throws Exception
  {
    frame.setSize(128, 456);
    frame.setLocation(12, 34);
    frame.open();

    frame.setFullScreen(true);
    frame.setFullScreen(false);

    assertEquals(new Dimension(128, 456), frame.getSize());
    assertEquals(new Point(12, 34), frame.getLocation());
  }
}
