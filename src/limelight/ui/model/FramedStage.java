//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.Context;
import limelight.styles.abstrstyling.*;
import limelight.ui.api.StageProxy;
import limelight.ui.events.stage.StageClosingEvent;
import limelight.ui.images.Images;
import limelight.util.Colors;

import java.awt.*;

public class FramedStage extends Stage
{
  private static final StyleCompiler widthCompiler = Context.instance().styleAttributeCompilerFactory.compiler("dimension", "stage width");
  private static final StyleCompiler heightCompiler = Context.instance().styleAttributeCompilerFactory.compiler("dimension", "stage height");
  private static final StyleCompiler xCompiler = Context.instance().styleAttributeCompilerFactory.compiler("x-coordinate", "stage x-coordinate");
  private static final StyleCompiler yCompiler = Context.instance().styleAttributeCompilerFactory.compiler("y-coordinate", "stage y-coordinate");
  private static final NoneableValue<DimensionValue> NONE = new NoneableValue<DimensionValue>(null);

  private StageProxy stage;
  private DimensionValue widthStyle = (DimensionValue) widthCompiler.compile(500);
  private DimensionValue heightStyle = (DimensionValue) heightCompiler.compile(500);
  private XCoordinateValue xLocationStyle = (XCoordinateValue) xCompiler.compile("center");
  private YCoordinateValue yLocationStyle = (YCoordinateValue) yCompiler.compile("center");
  private boolean opened;
  private boolean closing;
  private StageFrame frame;

  protected FramedStage()
  {
    frame = new StageFrame(this);
    addListeners();
  }

  public FramedStage(StageProxy stage)
  {
    this();
    this.stage = stage;

    Context.instance().frameManager.watch(frame);
    frame.setBackground(Color.WHITE);
    frame.setIconImage(Images.load("icon_48.gif"));
  }

  public StageFrame getFrame()
  {
    return frame;
  }

  public void close()
  {
    if(closing)
      return;
    closing = true;
    new StageClosingEvent().dispatch(this);
    frame.setVisible(false);
    frame.exitKioskOrFullscreenIfNeeded();
    clearListeners();
    frame.dispose();
    opened = false;
  }

  public boolean isVisible()
  {
    return frame.isVisible();
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

    frame.addNotify(); // MDM - Force the loading of the native peer to calculate insets.

    applySizeStyles();
    collapseAutoDimensions();
    applyLocationStyles();

    frame.setVisible(true);

    opened = true;
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

  @Override
  public void setRoot(RootPanel child)
  {
    if(root != null)
      root.setStage(null);

    mouseListener.reset();
    keyListener.reset(child);

    setCursor(child.getStyle().getCompiledCursor().getCursor());
    root = child;
    root.setStage(this);
  }

  public StageProxy getStage()
  {
    return stage;
  }

  public void setFullScreen(boolean setting)
  {
    frame.setFullScreen(setting);
  }

  public boolean isFullScreen()
  {
    return frame.isFullScreen();
  }

  public void setBackgroundColor(Object colorString)
  {
    frame.setBackground(Colors.resolve(colorString.toString()));
  }

  public String getBackgroundColor()
  {
    return Colors.toString(frame.getBackground());
  }

  public void setKiosk(boolean value)
  {
    frame.setKiosk(value);
  }

  public boolean isKiosk()
  {
    return frame.isKiosk();
  }

  public int getWidth()
  {
    return frame.getWidth();
  }

  public int getHeight()
  {
    return frame.getHeight();
  }

  public Graphics getGraphics()
  {
    return frame.getGraphics();
  }

  public Cursor getCursor()
  {
    return frame.getCursor();
  }

  public void setCursor(Cursor cursor)
  {
    frame.setCursor(cursor);
  }

  public Insets getInsets()
  {
    return frame.getInsets();
  }

  public void setVisible(boolean visible)
  {
    frame.setVisible(visible);
  }

  public Dimension getSize()
  {
    return frame.getSize();
  }
  
  public Point getLocation()
  {
    return frame.getLocation();
  }

  public void setTitle(String title)
  {
    frame.setTitle(title);
  }

  public String getTitle()
  {
    return frame.getTitle();
  }

  // Protected ////////////////////////////////////////////

  protected void setStage(StageProxy stage)
  {
    this.stage = stage;
  }

  // Private //////////////////////////////////////////////


  private void applySizeStyles()
  {
    Rectangle usableBounds = frame.getUsableScreenBounds();

    int width = widthStyle.calculateDimension(usableBounds.width, NONE, NONE, 0);
    int height = heightStyle.calculateDimension(usableBounds.height, NONE, NONE, 0);

    frame.setSize(width, height);
  }

  private void applyLocationStyles()
  {
    Rectangle usableBounds = frame.getUsableScreenBounds();

    int x = xLocationStyle.getX(getWidth(), usableBounds);
    int y = yLocationStyle.getY(getHeight(), usableBounds);

    frame.setLocation(x, y);
  }

  private void collapseAutoDimensions()
  {
    if(root == null)
      return;

    frame.superDoLayout();
    root.doLayout();

    Insets insets = frame.getInsets();
    int widthInsets = insets.left + insets.right;
    int heightInsets = insets.top + insets.bottom;

    Dimension size = frame.getSize();
    if(widthStyle.isAuto())
      size.width = widthStyle.collapseExcess(size.width + widthInsets, root.getWidth() + widthInsets, NONE, NONE);
    if(heightStyle.isAuto())
      size.height = heightStyle.collapseExcess(size.height + heightInsets, root.getHeight() + heightInsets, NONE, NONE);

    frame.setSize(size);
  }
  
  private void addListeners()
  {
    frame.addMouseListener(mouseListener);
    frame.addMouseMotionListener(mouseListener);
    frame.addMouseWheelListener(mouseListener);

    frame.addKeyListener(keyListener);
  }

  private void clearListeners()
  {
    frame.removeMouseListener(mouseListener);
    frame.removeMouseMotionListener(mouseListener);
    frame.removeMouseWheelListener(mouseListener);
    frame.removeKeyListener(keyListener);
    mouseListener = null;
    keyListener = null;
  }
}

