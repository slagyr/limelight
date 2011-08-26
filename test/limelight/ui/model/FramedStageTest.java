//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.Context;
import limelight.model.api.FakePropProxy;
import limelight.os.MockOS;
import limelight.styles.compiling.RealStyleAttributeCompilerFactory;
import limelight.styles.values.*;
import limelight.ui.*;
import limelight.model.api.MockStageProxy;
import limelight.util.Colors;
import limelight.util.Util;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;

import static junit.framework.Assert.*;

public class FramedStageTest
{
  private MockStageProxy stageProxy;
  private FramedStage stage;
  private FrameManager frameManager;
  public MockGraphicsDevice graphicsDevice;
  private Insets insets;
  private StageFrame frame;

  @Before
  public void setUp() throws Exception
  {
    RealStyleAttributeCompilerFactory.install();
    frameManager = new InertFrameManager();
    Context.instance().frameManager = frameManager;
    Context.instance().keyboardFocusManager = new limelight.ui.KeyboardFocusManager();

    stageProxy = new MockStageProxy();
    stage = new FramedStage("default", stageProxy);
    frame = stage.getFrame();

    graphicsDevice = new MockGraphicsDevice();
    frame.setGraphicsDevice(graphicsDevice);
    insets = new Insets(0, 0, 0, 0);
    frame.setScreenInsets(insets);

    Context.instance().os = new MockOS();
  }

  @After
  public void tearDown() throws Exception
  {
    try
    {
      stage.close();
    }
    catch(Exception e)
    {
      //ok
    }
  }

  @Test
  public void titleShouldDefaultToName() throws Exception
  {
    assertEquals("default", stage.getTitle());
  }

  @Test
  public void shouldIcon() throws Exception
  {
    assertNotNull(frame.getIconImage());
  }

  @Test
  public void shouldStage() throws Exception
  {
    assertSame(stageProxy, stage.getProxy());
  }

  @Test
  public void shouldLoad() throws Exception
  {
    ScenePanel panel = new ScenePanel(new FakePropProxy());
    stage.setScene(panel);

    Scene root = stage.getScene();

    assertSame(panel, root);
  }

  @Test
  public void shouldLoadSetsDefaultCursor() throws Exception
  {
    stage.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    ScenePanel panel = new ScenePanel(new FakePropProxy());
    stage.setScene(panel);

    assertEquals(Cursor.DEFAULT_CURSOR, stage.getCursor().getType());
  }

  @Test
  public void shouldLoadWillDestroyPreviousRoots() throws Exception
  {
    ScenePanel panel = new ScenePanel(new FakePropProxy());
    stage.setScene(panel);

    Scene firstRoot = stage.getScene();
    assertEquals(true, firstRoot.isIlluminated());

    ScenePanel panel2 = new ScenePanel(new FakePropProxy());
    stage.setScene(panel2);

    assertEquals(false, firstRoot.isIlluminated());
  }

  @Test
  public void shouldAddsSelfToFrameManager() throws Exception
  {
    assertEquals(1, frameManager.getFrameCount());
    assertEquals(true, frameManager.isWatching(frame));
  }

  @Test
  public void shouldHideAndShow() throws Exception
  {
    stage.open();
    assertEquals(true, stage.isVisible());

    stage.setVisible(false);
    assertEquals(false, stage.isVisible());

    stage.setVisible(true);
    assertEquals(true, stage.isVisible());
  }

  @Test
  public void shouldSetBackgroundColor() throws Exception
  {
    stage.setBackgroundColor("blue");
    assertEquals(Colors.resolve("blue"), frame.getBackground());
    assertEquals("#0000ffff", stage.getBackgroundColor());

    stage.setBackgroundColor("#abc");
    assertEquals(Colors.resolve("#abc"), frame.getBackground());
    assertEquals("#aabbccff", stage.getBackgroundColor());
  }

  @Test
  public void shouldShouldRetainSizeAndLocationWhenComingOutOfFullscreen() throws Exception
  {
    stage.setSizeStyles(128, 456);
    stage.setLocationStyles(12, 34);
    stage.open();

    stage.setFullScreen(true);
    stage.setFullScreen(false);

    assertEquals(new Dimension(128, 456), stage.getSize());
    assertEquals(new Point(12, 34), stage.getLocation());
  }

  @Test
  public void shouldSettingDimensionStyles() throws Exception
  {
    graphicsDevice.defaultConfiguration.bounds = new Rectangle(0, 0, 1000, 1000);
    insets.set(10, 20, 30, 40);

    stage.setSizeStyles(50, 100);
    assertEquals(new StaticPixelsValue(50), stage.getWidthStyle());
    assertEquals(new StaticPixelsValue(100), stage.getHeightStyle());
    assertEquals(new Dimension(50, 100), stage.getSize());

    stage.setSizeStyles("50%", "100%");
    assertEquals(new PercentagePixelsValue(50.0), stage.getWidthStyle());
    assertEquals(new PercentagePixelsValue(100.0), stage.getHeightStyle());
    assertEquals(new Dimension(470, 960), stage.getSize());
  }

  @Test
  public void shouldSettingLocationStyles() throws Exception
  {
    graphicsDevice.defaultConfiguration.bounds = new Rectangle(0, 0, 1000, 1000);
    insets.set(10, 20, 30, 40);

    frame.setSize(100, 100);

    stage.setLocationStyles(50, 100);
    assertEquals(new StaticXCoordinateValue(50), stage.getXLocationStyle());
    assertEquals(new StaticYCoordinateValue(100), stage.getYLocationStyle());
    assertEquals(new Point(insets.left + 50, insets.top + 100), stage.getLocation());

    stage.setLocationStyles("50%", "75%");
    assertEquals(new PercentageXCoordinateValue(50.0), stage.getXLocationStyle());
    assertEquals(new PercentageYCoordinateValue(75.0), stage.getYLocationStyle());
    assertEquals(new Point(490, 730), stage.getLocation());
  }

  @Test
  public void shouldApplyingLocationBeforeSizeWillAdjustBeforeOpening() throws Exception
  {
    graphicsDevice.defaultConfiguration.bounds = new Rectangle(0, 0, 1000, 1000);
    insets.set(10, 20, 30, 40);

    stage.setLocationStyles("center", "center");
    stage.setSizeStyles(200, 100);
    stage.open();

    assertEquals(new Dimension(200, 100), stage.getSize());
    assertEquals(new Point(390, 440), stage.getLocation());
  }

  @Test
  public void shouldSizeChangesPropogateDown() throws Exception
  {
    FakeScene panel = new FakeScene();
    stage.setScene(panel);

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
    stage.setSizeStyles("auto", "auto");
    FakeScene child = new FakeScene();
    child.prepForSnap(300, 200);
    stage.setScene(child);

    stage.open();
    Insets insets = stage.getInsets();

    int width = 300 + insets.left + insets.right;
    int height = 200 + insets.top + insets.bottom;
    assertEquals(new Dimension(width, height), stage.getSize());
  }

  @Test
  public void isClosed() throws Exception
  {
    stage.open();
    assertEquals(false, stage.isClosed());
    assertEquals(true, stage.isOpen());

    stage.close();

    assertEquals(true, stage.isClosed());
    assertEquals(false, stage.isOpen());
  }

  @Test
  public void shouldAddMouseListenersUponSettingTheFrame() throws Exception
  {
    final StageMouseListener listener = stage.getMouseListener();
    assertNotNull(listener);

    assertEquals(true, Arrays.asList(frame.getMouseListeners()).contains(listener));
    assertEquals(true, Arrays.asList(frame.getMouseMotionListeners()).contains(listener));
    assertEquals(true, Arrays.asList(frame.getMouseWheelListeners()).contains(listener));
  }

  @Test
  public void addsKeyListener() throws Exception
  {
    StageKeyListener listener = stage.getKeyListener();
    assertNotNull(listener);

    assertEquals(true, Arrays.asList(frame.getKeyListeners()).contains(listener));
  }

  @Test
  public void shouldDestroyRemovesListeners() throws Exception
  {
    StageMouseListener mouseListener = stage.getMouseListener();
    StageKeyListener keyListener = stage.getKeyListener();
    stage.close();

    assertEquals(false, Arrays.asList(frame.getMouseListeners()).contains(mouseListener));
    assertEquals(false, Arrays.asList(frame.getMouseMotionListeners()).contains(mouseListener));
    assertEquals(false, Arrays.asList(frame.getMouseWheelListeners()).contains(mouseListener));
    assertEquals(false, Arrays.asList(frame.getKeyListeners()).contains(keyListener));

    assertNull(stage.getMouseListener());
    assertNull(stage.getKeyListener());
  }
  
  @Test
  public void applyOptionsArePassedToProxy() throws Exception
  {
    stage = new FramedStage("default", stageProxy);
    stage.applyOptions(Util.toMap("foo", "bar"));

    assertEquals("bar", stageProxy.appliedOptions.get("foo"));
  }

  @Test
  public void applyOptionsAreAppliedToFramedStage() throws Exception
  {
    stage.applyOptions(Util.toMap("title", "My Title", "kiosk", true, "framed", false));

    assertEquals("My Title", stage.getTitle());
    assertEquals(true, stage.isKiosk());
    assertEquals(false, stage.isFramed());
  }

  @Test
  public void framedSettings() throws Exception
  {
    stage.setFramed(true);
    assertEquals(true, stage.isFramed());
    assertEquals(false, frame.isUndecorated());

    stage.setFramed(false);
    assertEquals(false, stage.isFramed());
    assertEquals(true, frame.isUndecorated());
  }

  @Test
  public void alwaysOnTop() throws Exception
  {
    stage.setAlwaysOnTop(true);
    assertEquals(true, stage.isAlwaysOnTop());
    assertEquals(true, frame.isAlwaysOnTop());

    stage.setAlwaysOnTop(false);
    assertEquals(false, stage.isAlwaysOnTop());
    assertEquals(false, frame.isAlwaysOnTop());
  }
  
  @Test
  public void setSizeWithCollection() throws Exception
  {
    LinkedList<Object> sizes = new LinkedList<Object>();
    sizes.add(123);
    sizes.add(456);

    stage.setSize(sizes);

    assertEquals("123", stage.getWidthStyle().toString());
    assertEquals("456", stage.getHeightStyle().toString());
  }

  @Test
  public void setLocationWithCollection() throws Exception
  {
    LinkedList<Object> locations = new LinkedList<Object>();
    locations.add(123);
    locations.add(456);

    stage.setLocation(locations);

    assertEquals("123", stage.getXLocationStyle().toString());
    assertEquals("456", stage.getYLocationStyle().toString());
  }

  // TODO MDM - make sure this works
//  @Test
//  public void keyboardFocusDoesNotRemainOnChildWhenDestroyed() throws Exception
//  {
//    TextBoxPanel inputPanel = new TextBoxPanel();
//    root.setFrame(frame);
//    child.add(inputPanel);
//    root.add(child);
//
//    Context.instance().keyboardFocusManager.focusPanel(inputPanel);
//    root.setFrame(null);
//
//    assertNotSame(inputPanel, Context.instance().keyboardFocusManager.getFocusedPanel());
//  }
}
