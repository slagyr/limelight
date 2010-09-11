//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.ui.api.MockProp;
import limelight.ui.api.MockStage;
import limelight.ui.*;
import limelight.Context;
import limelight.KeyboardFocusManager;
import limelight.MockContext;
import limelight.styles.values.*;
import limelight.styles.compiling.RealStyleAttributeCompilerFactory;
import limelight.os.MockOS;
import limelight.util.Colors;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.WindowEvent;

public class StageFrameTest extends Assert
{
  private MockStage stage;
  private StageFrame frame;
  private FrameManager frameManager;
  public MockGraphicsDevice graphicsDevice;
  private MockOS os;
  private Insets insets;

  @Before
  public void setUp() throws Exception
  {
    RealStyleAttributeCompilerFactory.install();
    frameManager = new InertFrameManager();
    Context.instance().frameManager = frameManager;
    Context.instance().keyboardFocusManager = new KeyboardFocusManager();

    stage = new MockStage();
    frame = new StageFrame(stage);

    graphicsDevice = new MockGraphicsDevice();
    frame.setGraphicsDevice(graphicsDevice);
    insets = new Insets(0, 0, 0, 0);
    frame.setScreenInsets(insets);

    os = new MockOS();
    Context.instance().os = os;
  }

  @After
  public void tearDown() throws Exception
  {
    try
    {
      frame.close(null);
    }
    catch(Exception e)
    {
      //ok
    }
  }
  
  @Test
  public void shouldIcon() throws Exception
  {
    assertNotNull(frame.getIconImage());
  }

  @Test
  public void shouldStage() throws Exception
  {
    assertSame(stage, frame.getStage());
  }

  @Test
  public void shouldLoad() throws Exception
  {
    ScenePanel panel = new ScenePanel(new MockProp());
    frame.load(panel);

    RootPanel root = frame.getRoot();

    assertSame(panel, root);
  }

  @Test
  public void shouldLoadSetsDefaultCursor() throws Exception
  {
    frame.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    ScenePanel panel = new ScenePanel(new MockProp());
    frame.load(panel);

    assertEquals(Cursor.DEFAULT_CURSOR, frame.getCursor().getType());
  }

  @Test
  public void shouldLoadWillDestroyPreviousRoots() throws Exception
  {
    ScenePanel panel = new ScenePanel(new MockProp());
    frame.load(panel);

    RootPanel firstRoot = frame.getRoot();
    assertEquals(true, firstRoot.isIlluminated());

    ScenePanel panel2 = new ScenePanel(new MockProp());
    frame.load(panel2);

    assertEquals(false, firstRoot.isIlluminated());
  }

  @Test
  public void shouldAddsSelfToFrameManager() throws Exception
  {
    assertEquals(1, frameManager.getFrameCount());
    assertEquals(true, frameManager.isWatching(frame));
  }

//  @Test
//  public void shouldDefaultCloseOperations() throws Exception
//  {
//    assertEquals(WindowConstants.DO_NOTHING_ON_CLOSE, frame.getDefaultCloseOperation());
//  }

  @Test
  public void shouldSetFullScreenWhenNotVisible() throws Exception
  {
    frame.setFullScreen(true);

    assertEquals(true, frame.isFullScreen());
    frame.open();
    assertEquals(frame, graphicsDevice.getFullScreenWindow());
  }

  @Test
  public void shouldSetFullScreenWhenVisible() throws Exception
  {
    frame.open();
    frame.setFullScreen(true);

    assertEquals(true, frame.isFullScreen());
    assertEquals(frame, graphicsDevice.getFullScreenWindow());
  }

  @Test
  public void shouldTurnOffFullScreenWhiledisplayed() throws Exception
  {
    frame.setFullScreen(true);
    frame.open();
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
    frame.open();

    assertEquals(frame, graphicsDevice.getFullScreenWindow());
    assertEquals(true, os.isInKioskMode());
  }

  @Test
  public void shouldKioskWillGoFullscreenAndFramelessWhenClosed() throws Exception
  {
    frame.setKiosk(true);
    frame.open();
    frame.close(null);

    assertEquals(null, graphicsDevice.getFullScreenWindow());
    assertEquals(false, os.isInKioskMode());
  }

  @Test
  public void shouldKioskModePreservesScreenSetting() throws Exception
  {
    frame.setFullScreen(false);
    frame.setKiosk(true);
    frame.open();
    frame.setKiosk(false);

    assertEquals(null, graphicsDevice.getFullScreenWindow());
    assertEquals(false, os.isInKioskMode());
  }

  @Test
  public void shouldKioskModePreservesScreenSettingWithFullscreenOn() throws Exception
  {
    frame.setFullScreen(true);
    frame.setKiosk(true);
    frame.open();
    frame.setKiosk(false);

    assertEquals(frame, graphicsDevice.getFullScreenWindow());
    assertEquals(false, os.isInKioskMode());
  }

  @Test
  public void shouldEnterKioskModeWhileOpen() throws Exception
  {
    frame.setKiosk(false);
    frame.open();
    frame.setKiosk(true);

    assertEquals(frame, graphicsDevice.getFullScreenWindow());
    assertEquals(true, os.isInKioskMode());
  }

  @Test
  public void shouldHidingWhileInKioskMode() throws Exception
  {
    frame.setKiosk(true);
    frame.open();
    frame.setVisible(false);

    assertEquals(null, graphicsDevice.getFullScreenWindow());
    assertEquals(false, os.isInKioskMode());
  }

  @Test
  public void shouldShowingAfterHidingWhileInKioskMode() throws Exception
  {
    frame.setKiosk(true);
    frame.open();
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
    frame.open();

    frame.setFullScreen(false);
    assertEquals(frame, graphicsDevice.getFullScreenWindow());
  }

  @Test
  public void shouldHideAndShow() throws Exception
  {
    frame.open();
    assertEquals(true, frame.isVisible());

    frame.setVisible(false);
    assertEquals(false, frame.isVisible());

    frame.setVisible(true);
    assertEquals(true, frame.isVisible());
  }

  @Test
  public void shouldHideAndShowWithFullScreen() throws Exception
  {
    frame.setFullScreen(true);
    frame.open();
    frame.setVisible(false);
    assertEquals(null, graphicsDevice.getFullScreenWindow());

    frame.setVisible(true);
    assertEquals(frame, graphicsDevice.getFullScreenWindow());
  }

  @Test
  public void shouldSettingFullScreenWhileHidden() throws Exception
  {
    frame.open();
    frame.setVisible(false);
    frame.setFullScreen(true);
    assertEquals(null, graphicsDevice.getFullScreenWindow());
  }

  @Test
  public void shouldSetBackgroundColor() throws Exception
  {
    frame.setBackgroundColor("blue");
    assertEquals(Colors.resolve("blue"), frame.getBackground());
    assertEquals("#0000ffff", frame.getBackgroundColor());

    frame.setBackgroundColor("#abc");
    assertEquals(Colors.resolve("#abc"), frame.getBackground());
    assertEquals("#aabbccff", frame.getBackgroundColor());
  }

  @Test
  public void shouldShouldRetainSizeAndLocationWhenComingOutOfFullscreen() throws Exception
  {
    frame.setSizeStyles(128, 456);
    frame.setLocationStyles(12, 34);
    frame.open();

    frame.setFullScreen(true);
    frame.setFullScreen(false);

    assertEquals(new Dimension(128, 456), frame.getSize());
    assertEquals(new Point(12, 34), frame.getLocation());
  }

  @Test
  public void shouldShouldAllowClose() throws Exception
  {
    stage.shouldAllowClose = false;
    assertEquals(false, frame.shouldAllowClose());

    stage.shouldAllowClose = true;
    assertEquals(true, frame.shouldAllowClose());
  }

  @Test
  public void shouldSettingDimensionStyles() throws Exception
  {
    graphicsDevice.defaultConfiguration.bounds = new Rectangle(0, 0, 1000, 1000);
    insets.set(10, 20, 30, 40);

    frame.setSizeStyles(50, 100);
    assertEquals(new StaticPixelsValue(50), frame.getWidthStyle());
    assertEquals(new StaticPixelsValue(100), frame.getHeightStyle());
    assertEquals(new Dimension(50, 100), frame.getSize());

    frame.setSizeStyles("50%", "100%");
    assertEquals(new PercentagePixelsValue(50.0), frame.getWidthStyle());
    assertEquals(new PercentagePixelsValue(100.0), frame.getHeightStyle());
    assertEquals(new Dimension(470, 960), frame.getSize());
  }

  @Test
  public void shouldSettingLocationStyles() throws Exception
  {
    graphicsDevice.defaultConfiguration.bounds = new Rectangle(0, 0, 1000, 1000);
    insets.set(10, 20, 30, 40);

    frame.setSize(100, 100);

    frame.setLocationStyles(50, 100);
    assertEquals(new StaticXCoordinateValue(50), frame.getXLocationStyle());
    assertEquals(new StaticYCoordinateValue(100), frame.getYLocationStyle());
    assertEquals(new Point(insets.left + 50, insets.top + 100), frame.getLocation());

    frame.setLocationStyles("50%", "75%");
    assertEquals(new PercentageXCoordinateValue(50.0), frame.getXLocationStyle());
    assertEquals(new PercentageYCoordinateValue(75.0), frame.getYLocationStyle());
    assertEquals(new Point(490, 730), frame.getLocation());
  }

  @Test
  public void shouldApplyingLocationBeforeSizeWillAdjustBeforeOpening() throws Exception
  {
    graphicsDevice.defaultConfiguration.bounds = new Rectangle(0, 0, 1000, 1000);
    insets.set(10, 20, 30, 40);
    
    frame.setLocationStyles("center", "center");
    frame.setSizeStyles(200, 100);
    frame.open();

    assertEquals(new Dimension(200, 100), frame.getSize());
    assertEquals(new Point(390, 440), frame.getLocation());
  }

  @Test
  public void shouldVitality() throws Exception
  {
    assertEquals(true, frame.isVital());

    frame.setVital(false);

    assertEquals(false, frame.isVital());
  }

  @Test
  public void shouldSizeChangesPropogateDown() throws Exception
  {
    MockRootPanel panel = new MockRootPanel();
    frame.load(panel);

    frame.doLayout(); // Called when the stage is resized
    assertEquals(true, panel.consumableAreaChangedCalled);

    panel.consumableAreaChangedCalled = false;
    frame.setSize(123, 456);
    frame.doLayout();
    assertEquals(true, panel.consumableAreaChangedCalled);
  }

  @Test
  public void shouldShouldCollapseAutoDimensions() throws Exception
  {
    frame.setSizeStyles("auto", "auto");
    MockRootPanel child = new MockRootPanel();
    child.prepForSnap(300, 200);
    frame.load(child);

    frame.open();
    Insets insets = frame.getInsets();

    int width = 300 + insets.left + insets.right;
    int height = 200 + insets.top + insets.bottom;
    assertEquals(new Dimension(width, height), frame.getSize());
  }

  @Test
  public void shouldClosedIsCalledwhenClosed() throws Exception
  {
    MockContext.stub();
    AlertFrameManager manager = new AlertFrameManager();
    manager.watch(frame);
    frame.close(null);
    Thread.sleep(10);

    assertEquals(true, stage.wasClosed);
  }
  
  @Test
  public void shouldIconificationDelegatedToStage() throws Exception
  {
    frame.iconified(new WindowEvent(frame, 1));
    assertEquals(true, stage.iconified);
    frame.deiconified(new WindowEvent(frame, 1));
    assertEquals(false, stage.iconified);
  }

  @Test
  public void shouldActivationDelegatedToStage() throws Exception
  {
    frame.setVisible(true);
    frame.activated(new WindowEvent(frame, 1));
    assertEquals(true, stage.activated);
    frame.deactivated(new WindowEvent(frame, 1));
    assertEquals(false, stage.activated);
  }
  
  @Test
  public void shouldStageShouldBeNotifiedWhenClosing() throws Exception
  {
    frame.close(null);

    assertEquals(true, stage.notifiedOfClosing);
  }

  @Test
  public void shouldClosingAndClosedNotCalledMoreThanOnce() throws Exception
  {
    frame.close(null);
    frame.closed(null);
    stage.notifiedOfClosing = false;
    stage.wasClosed = false;

    frame.close(null);
    frame.closed(null);

    assertEquals(false, stage.notifiedOfClosing);
    assertEquals(false, stage.wasClosed);
  }

  @Test
  public void shouldIsClosed() throws Exception
  {
    assertEquals(false, frame.isClosed());

    frame.closed(null);

    assertEquals(true, frame.isClosed());
  }

  @Test
  public void shouldDeactivatedWhenNotPreviouslyActivatedDoesNotPropogate() throws Exception
  {
    stage.activated = true;
    frame.deactivated(null);
    assertEquals(true, stage.activated);

    frame.setVisible(true);
    frame.activated(null);
    frame.deactivated(null);
    assertEquals(false, stage.activated);
  }
}
