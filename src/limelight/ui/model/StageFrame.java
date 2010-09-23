//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.Context;
import limelight.background.PanelPainterLoop;
import limelight.events.*;
import limelight.styles.abstrstyling.*;
import limelight.ui.events.stage.StageClosingEvent;
import limelight.ui.images.Images;
import limelight.util.Colors;
import limelight.ui.api.Stage;

import java.awt.*;

public class StageFrame extends Frame implements PropFrame
{
  private static final StyleCompiler widthCompiler = Context.instance().styleAttributeCompilerFactory.compiler("dimension", "stage width");
  private static final StyleCompiler heightCompiler = Context.instance().styleAttributeCompilerFactory.compiler("dimension", "stage height");
  private static final StyleCompiler xCompiler = Context.instance().styleAttributeCompilerFactory.compiler("x-coordinate", "stage x-coordinate");
  private static final StyleCompiler yCompiler = Context.instance().styleAttributeCompilerFactory.compiler("y-coordinate", "stage y-coordinate");
  private static final NoneableValue<DimensionValue> NONE = new NoneableValue<DimensionValue>(null);

  private Stage stage;
  protected RootPanel root;
  private boolean fullscreen;
  private GraphicsDevice graphicsDevice; //used for testing
  private Insets screenInsets; //used for testing
  private boolean kiosk;
  private Dimension sizeBeforeFullScreen;
  private Point locationBeforeFullScreen;
  private DimensionValue widthStyle = (DimensionValue) widthCompiler.compile(500);
  private DimensionValue heightStyle = (DimensionValue) heightCompiler.compile(500);
  private XCoordinateValue xLocationStyle = (XCoordinateValue) xCompiler.compile("center");
  private YCoordinateValue yLocationStyle = (YCoordinateValue) yCompiler.compile("center");
  private boolean vital = true;
  private Dimension previousSize;
  private boolean opened;
  private boolean closing;
  private EventHandler eventHandler = new EventHandler();

  protected StageFrame()
  {
    super();
  }

  public StageFrame(Stage stage)
  {
    this();
    this.stage = stage;
    setBackground(Color.WHITE);
    Context.instance().frameManager.watch(this);

    setIconImage(Images.load("icon_48.gif"));
  }

  public void paint(Graphics g)
  {
    if(root != null)
    {
      if(isWindowResizing() && Context.instance().bufferedImagePool != null) // MDM - Check the bufferedImagePool because errors get printed during test runs.
      {
        PanelPainterLoop.doPaintJob(root, root.getAbsoluteBounds(), (Graphics2D) g);
      }
      else
      {
        root.addDirtyRegion(root.getAbsoluteBounds());
      }
    }
  }

  // MDM - When the window is resizing, the layout and painting have to take place
  // immediately.  Otherwise, the window flashes and flickers annoyingly.
  // The best way to tell if the window is being resized (as far as I can tell), is to
  // check the currentEvent in the EventQueue. I didn't see anything in particular about
  // the event that tells us what's happening.
  private boolean isWindowResizing()
  {
    return EventQueue.getCurrentEvent() != null;
  }

  public void doLayout()
  {
    if(root != null && isWindowResizing())
    {
      root.doLayout();
    }
    else
      super.doLayout();

    refresh();
  }

  public void close()
  {
    if(closing)
      return;
    closing = true;
    new StageClosingEvent().dispatch(this);
    setVisible(false);
    exitKioskOrFullscreenIfNeeded();
    dispose();
    opened = false;
  }

  public boolean isClosed()
  {
    return !opened;
  }

  public boolean isOpen()
  {
    return opened;
  }

  public void open()
  {
    if(opened)
      return;

    addNotify(); // MDM - Force the loading of the native peer to calculate insets.

    applySizeStyles();
    collapseAutoDimensions();
    applyLocationStyles();

    setVisible(true);

    opened = true;
  }

  public void setVisible(boolean visible)
  {
    if(visible == isVisible())
      return;
    super.setVisible(visible);

    if(visible)
    {
      enterKioskOrFullscreenIfNeeded();
    }
    else
      exitKioskOrFullscreenIfNeeded();
  }

  public void refresh()
  {
    if(root != null)
    {
      if(previousSize == null || !previousSize.equals(getSize()))
      {
        root.consumableAreaChanged();
      }
      else
      {
        root.markAsNeedingLayout();
      }
    }
    previousSize = getSize();
  }

  public void setSizeStyles(Object widthValue, Object heightValue)
  {
    widthStyle = (DimensionValue) widthCompiler.compile(widthValue);
    heightStyle = (DimensionValue) heightCompiler.compile(heightValue);

    applySizeStyles();
  }

  public DimensionValue getWidthStyle()
  {
    return widthStyle;
  }

  public DimensionValue getHeightStyle()
  {
    return heightStyle;
  }

  public void setLocationStyles(Object xValue, Object yValue)
  {
    xLocationStyle = (XCoordinateValue) xCompiler.compile(xValue);
    yLocationStyle = (YCoordinateValue) yCompiler.compile(yValue);

    applyLocationStyles();
  }

  public XCoordinateValue getXLocationStyle()
  {
    return xLocationStyle;
  }

  public YCoordinateValue getYLocationStyle()
  {
    return yLocationStyle;
  }

  public void load(RootPanel child)
  {
    if(root != null)
      root.setFrame(null);
    setCursor(child.getStyle().getCompiledCursor().getCursor());
    root = child;
    root.setFrame(this);
  }

  public Stage getStage()
  {
    return stage;
  }

  public RootPanel getRoot()
  {
    return root;
  }

  public void setFullScreen(boolean setting)
  {
    if(fullscreen != setting)
    {
      fullscreen = setting;
      if(fullscreen && isVisible() && this != getGraphicsDevice().getFullScreenWindow())
        turnFullScreenOn();
      else if(this == getGraphicsDevice().getFullScreenWindow() && !kiosk)
        turnFullscreenOff();
    }
  }

  public void setScreenInsets(Insets insets)  // Used for testing
  {
    screenInsets = insets;
  }

  public void setGraphicsDevice(GraphicsDevice device)  // Used for testing
  {
    this.graphicsDevice = device;
  }

  public GraphicsDevice getGraphicsDevice()
  {
    if(graphicsDevice != null)
      return graphicsDevice;
    return getGraphicsConfiguration().getDevice();
  }

  public boolean isFullScreen()
  {
    return fullscreen;
  }

  public void setBackgroundColor(Object colorString)
  {
    setBackground(Colors.resolve(colorString.toString()));
  }

  public String getBackgroundColor()
  {
    return Colors.toString(getBackground());
  }

  public void setKiosk(boolean value)
  {
    if(kiosk == value)
      return;
    kiosk = value;
    if(isVisible())
    {
      if(kiosk)
      {
        if(getGraphicsDevice().getFullScreenWindow() != this)
          turnFullScreenOn();
        Context.instance().os.enterKioskMode();
      }
      else
      {
        if(!fullscreen && getGraphicsDevice().getFullScreenWindow() == this)
          turnFullscreenOff();
        Context.instance().os.exitKioskMode();
      }
    }
  }

  public boolean isKiosk()
  {
    return kiosk;
  }

  public boolean shouldAllowClose()
  {
    if(root == null)
      return true;
    else
      return root.shouldAllowClose();
  }

  public boolean isVital()
  {
    return vital;
  }

  public void setVital(boolean value)
  {
    vital = value;
  }
  
  // Protected ////////////////////////////////////////////

  protected void setStage(Stage stage)
  {
    this.stage = stage;
  }

  // Private //////////////////////////////////////////////

  private void enterKioskOrFullscreenIfNeeded()
  {
    if(fullscreen || kiosk)
      turnFullScreenOn();
    if(kiosk)
      Context.instance().os.enterKioskMode();
  }

  private void exitKioskOrFullscreenIfNeeded()
  {
    if(fullscreen || kiosk)
      turnFullscreenOff();
    if(kiosk)
      Context.instance().os.exitKioskMode();
  }

  private void turnFullScreenOn()
  {
    sizeBeforeFullScreen = getSize();
    locationBeforeFullScreen = getLocation();
    getGraphicsDevice().setFullScreenWindow(this);
  }

  private void turnFullscreenOff()
  {
    getGraphicsDevice().setFullScreenWindow(null);
    if(sizeBeforeFullScreen != null)
      super.setSize(sizeBeforeFullScreen);
    if(locationBeforeFullScreen != null)
      super.setLocation(locationBeforeFullScreen);
  }

  private limelight.util.Box getUsableScreenBounds()
  {
    GraphicsConfiguration config = getGraphicsDevice().getDefaultConfiguration();
    limelight.util.Box bounds = new limelight.util.Box(config.getBounds());

    Insets insets = screenInsets == null ? Toolkit.getDefaultToolkit().getScreenInsets(config) : screenInsets;

    bounds.shave(insets.top, insets.right, insets.bottom, insets.left);

    return bounds;
  }

  private void applySizeStyles()
  {
    Rectangle usableBounds = getUsableScreenBounds();

    int width = widthStyle.calculateDimension(usableBounds.width, NONE, NONE, 0);
    int height = heightStyle.calculateDimension(usableBounds.height, NONE, NONE, 0);

    setSize(width, height);
  }

  private void applyLocationStyles()
  {
    Rectangle usableBounds = getUsableScreenBounds();

    int x = xLocationStyle.getX(getWidth(), usableBounds);
    int y = yLocationStyle.getY(getHeight(), usableBounds);

    setLocation(x, y);
  }

  private void collapseAutoDimensions()
  {
    if(root == null)
      return;

    super.doLayout();
    root.doLayout();

    Insets insets = getInsets();
    int widthInsets = insets.left + insets.right;
    int heightInsets = insets.top + insets.bottom;

    Dimension size = getSize();
    if(widthStyle.isAuto())
      size.width = widthStyle.collapseExcess(size.width + widthInsets, root.getWidth() + widthInsets, NONE, NONE);
    if(heightStyle.isAuto())
      size.height = heightStyle.collapseExcess(size.height + heightInsets, root.getHeight() + heightInsets, NONE, NONE);

    setSize(size);
  }

  public EventHandler getEventHandler()
  {
    return eventHandler;
  }
}

