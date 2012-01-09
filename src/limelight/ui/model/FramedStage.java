//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.Context;
import limelight.model.Stage;
import limelight.styles.abstrstyling.*;
import limelight.model.api.StageProxy;
import limelight.styles.values.AutoDimensionValue;
import limelight.ui.images.Images;
import limelight.util.Colors;
import limelight.util.Options;

import java.awt.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class FramedStage extends Stage
{
  private static final StyleCompiler widthCompiler = Context.instance().styleAttributeCompilerFactory.compiler("dimension", "stage width");
  private static final StyleCompiler heightCompiler = Context.instance().styleAttributeCompilerFactory.compiler("dimension", "stage height");
  private static final StyleCompiler xCompiler = Context.instance().styleAttributeCompilerFactory.compiler("x-coordinate", "stage x-coordinate");
  private static final StyleCompiler yCompiler = Context.instance().styleAttributeCompilerFactory.compiler("y-coordinate", "stage y-coordinate");
  private static final NoneableValue<DimensionValue> NONE = new NoneableValue<DimensionValue>(null);

  private DimensionValue widthStyle = (DimensionValue) widthCompiler.compile(500);
  private DimensionValue heightStyle = (DimensionValue) heightCompiler.compile(500);
  private XCoordinateValue xLocationStyle = (XCoordinateValue) xCompiler.compile("center");
  private YCoordinateValue yLocationStyle = (YCoordinateValue) yCompiler.compile("center");
  private boolean opened;
  private StageFrame frame;

  public FramedStage(String name)
  {
    super(name);
    frame = new StageFrame(this);
    frame.setTitle(name);
    addListeners();

    if(Context.instance().frameManager != null)
      Context.instance().frameManager.watch(frame);
    frame.setBackground(Color.WHITE);
    frame.setIconImage(Images.load("icon_48.gif"));
  }

  public FramedStage(String name, StageProxy proxy)
  {
    this(name);
    setProxy(proxy);
  }

  public void applyOptions(Map<String, Object> options)
  {
    Options.apply(this, options);
    getProxy().applyOptions(options);
  }

  public StageFrame getFrame()
  {
    return frame;
  }

  @Override
  protected void doOpen()
  {
    frame.addNotify(); // MDM - Force the loading of the native peer to calculate insets.
    applySizeStyles();
    collapseAutoDimensions();
    applyLocationStyles();
    opened = true;
  }

  @Override
  protected void doClose()
  {
    frame.setVisible(false);
    frame.exitKioskOrFullscreenIfNeeded();
    clearListeners();
    frame.dispose();
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

  public void setSizeStyles(Object widthValue, Object heightValue)
  {
    widthStyle = (DimensionValue) widthCompiler.compile(widthValue);
    heightStyle = (DimensionValue) heightCompiler.compile(heightValue);

    applySizeStyles();
  }

  public void setSize(Collection<Object> sizes)
  {
    final Iterator<Object> iterator = sizes.iterator();
    Object width = iterator.next();
    Object height = iterator.next();
    setSizeStyles(width, height);
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

  public void setLocation(Collection<Object> locations)
  {
    final Iterator<Object> iterator = locations.iterator();
    Object x = iterator.next();
    Object y = iterator.next();
    setLocationStyles(x, y);
  }

  public XCoordinateValue getXLocationStyle()
  {
    return xLocationStyle;
  }

  public YCoordinateValue getYLocationStyle()
  {
    return yLocationStyle;
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

  @Override
  public void setFramed(boolean framed)
  {
    frame.setUndecorated(!framed);
  }

  @Override
  public boolean isFramed()
  {
    return !frame.isUndecorated();
  }

  @Override
  public void setAlwaysOnTop(boolean value)
  {
    frame.setAlwaysOnTop(value);
  }

  @Override
  public boolean isAlwaysOnTop()
  {
    return frame.isAlwaysOnTop();
  }

  protected void doSetVisible(boolean visible)
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
    if(scene == null)
      return;

    frame.superDoLayout();
    scene.doLayout();

    Insets insets = frame.getInsets();
    int widthInsets = insets.left + insets.right;
    int heightInsets = insets.top + insets.bottom;

    Dimension size = frame.getSize();
    if(widthStyle instanceof AutoDimensionValue)
      size.width = widthStyle.collapseExcess(size.width + widthInsets, scene.getWidth() + widthInsets, NONE, NONE);
    if(heightStyle instanceof AutoDimensionValue)
      size.height = heightStyle.collapseExcess(size.height + heightInsets, scene.getHeight() + heightInsets, NONE, NONE);

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

