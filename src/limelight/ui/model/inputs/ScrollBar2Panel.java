//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.styles.ScreenableStyle;
import limelight.ui.model.BasePanel;
import limelight.ui.model.inputs.painting.ScrollBarPainter;
import limelight.util.Box;

import java.awt.*;
import java.awt.event.*;

public class ScrollBar2Panel extends BasePanel
{
  public static final int VERTICAL = 0;
  public static final int HORIZONTAL = 1;

  private ScrollBarPainter painter = ScrollBarPainter.instance;
  private ScrollMouseProcessor mouseProcessor = new ScrollMouseProcessor(this);

  private final int preferredGirth;
  private int orientation;
  private int availableAmount;
  private int visibleAmount;
  private int unitIncrement = 5;
  private int blockIncrement;
  private int value;
  private int maxValue;
  private int sliderSize;
  private int sliderPosition;
  private int minSliderPosition;
  private int maxSliderPosition;
  private int trackSize;
  private Box increasingButtonBounds;
  private Box decreasingButtonBounds;
  private boolean increasingButtonActive;
  private boolean decreasingButtonActive;
  private Box trackBounds;
  private Box sliderBounds;

  public ScrollBar2Panel(int orientation)
  {
    this.orientation = orientation;
    if(getOrientation() == VERTICAL)
    {
      preferredGirth = width = 15;
      sliderBounds = new Box(0, 0, preferredGirth, 0);
    }
    else
    {
      preferredGirth = height = 15;
      sliderBounds = new Box(0, 0, 0, preferredGirth);
    }
  }

  public int getOrientation()
  {
    return orientation;
  }

  public boolean isVertical()
  {
    return orientation == VERTICAL;
  }
  
  public boolean isHorizontal()
  {
    return orientation == HORIZONTAL;
  }

  public limelight.util.Box getChildConsumableArea()
  {
    return getBoundingBox();
  }

  public limelight.util.Box getBoxInsidePadding()
  {
    return getBoundingBox();
  }

  public ScreenableStyle getStyle()
  {
    return getParent().getStyle();
  }

  @Override
  public void setSize(int width, int height)
  {
    if(getOrientation() == VERTICAL)
      width = preferredGirth;
    else
      height = preferredGirth;
    super.setSize(width, height);

    increasingButtonBounds = painter.getIncreasingBox(this);
    decreasingButtonBounds = painter.getDecreasingBox(this);
    trackBounds = painter.getTrackBox(this);

    markAsDirty();
  }

  public void setHeight(int height)
  {
    setSize(preferredGirth, height);
  }

  public void setWidth(int width)
  {
    setSize(width, preferredGirth);
  }

  @Override
  public void mousePressed(MouseEvent e)
  {
    mouseProcessor.mousePressed(translatedMouseEvent(e));
  }

  private MouseEvent translatedMouseEvent(MouseEvent e)
  {
    Point p = e.getPoint();
    e = new MouseEvent((Component) e.getSource(), e.getID(), e.getWhen(), e.getModifiers(), p.x - getAbsoluteLocation().x, p.y - getAbsoluteLocation().y, e.getClickCount(), false);
    return e;
  }

  @Override
  public void mouseReleased(MouseEvent e)
  {
    mouseProcessor.mouseReleased(translatedMouseEvent(e));
  }

  @Override
  public void mouseClicked(MouseEvent e)
  {
    mouseProcessor.mouseClicked(translatedMouseEvent(e));
  }

  @Override
  public void mouseDragged(MouseEvent e)
  {
    mouseProcessor.mouseDragged(translatedMouseEvent(e));
  }

  @Override
  public void mouseEntered(MouseEvent e)
  {
    mouseProcessor.mouseEntered(translatedMouseEvent(e));
  }

  @Override
  public void mouseExited(MouseEvent e)
  {
    mouseProcessor.mouseExited(translatedMouseEvent(e));
  }

  @Override
  public void mouseMoved(MouseEvent e)
  {
    mouseProcessor.mouseMoved(translatedMouseEvent(e));
  }

  public void paintOn(Graphics2D graphics)
  {
    painter.paintOn(graphics, this);
  }

  public boolean canBeBuffered()
  {
    return false;
  }

  public void setValue(int value)
  {
    if(value < 0)
      value = 0;
    else if(value > maxValue)
      value = maxValue;

    if(value != getValue())
    {
      this.value = value;
      calculateSliderPosition();
      configurationChanged();
    }
  }

  private synchronized void configurationChanged()
  {
    //TODO - could improve performance here... no need to create new instance of layout every time
    BasePanel parent = (BasePanel) getParent();
    if(parent != null)
      parent.markAsNeedingLayout(new ScrollLayout(this));
  }

  public int getValue()
  {
    return value;
  }

  public int getMaxValue()
  {
    return maxValue;
  }

  public int getAvailableAmount()
  {
    return availableAmount;
  }

  public int getVisibleAmount()
  {
    return visibleAmount;
  }

  public int getUnitIncrement()
  {
    return unitIncrement;
  }

  public void setUnitIncrement(int unitIncrement)
  {
    this.unitIncrement = unitIncrement;
  }

  public int getBlockIncrement()
  {
    return blockIncrement;
  }

  public int getSliderSize()
  {
    return sliderSize;
  }

  public void configure(int visible, int available)
  {
    synchronized(this)
    {
      availableAmount = available;
      visibleAmount = visible;
      maxValue = available - visible;
      blockIncrement = (int) (visible * 0.9);
      calculateSliderSize();
      calculateSliderPosition();
    }
    configurationChanged();
  }

  private void calculateSliderSize()
  {
    double proportion = (double) visibleAmount / availableAmount;
    int fullSize = isHorizontal() ? getWidth() : getHeight();
    int avaiableSize = fullSize - painter.getOutsideCusion() - painter.getInsideCushion();
    int calculatedSliderSize = (int) (avaiableSize * proportion + 0.5);
    sliderSize = Math.max(calculatedSliderSize, painter.getMinSliderSize());
    minSliderPosition = painter.getOutsideCusion();
    maxSliderPosition = fullSize - painter.getInsideCushion() - sliderSize;
    trackSize = maxSliderPosition - minSliderPosition;

    if(isHorizontal())
      sliderBounds.width = sliderSize;
    else
      sliderBounds.height = sliderSize;
  }

  private void calculateSliderPosition()
  {
    double valueRatio = (double) value / maxValue;
    sliderPosition = minSliderPosition + (int) (trackSize * valueRatio + 0.5);

    if(isHorizontal())
      sliderBounds.x = sliderPosition;
    else
      sliderBounds.y = sliderPosition;
  }

  public void setSliderPosition(int position)
  {
    sliderPosition = Math.min(Math.max(position, minSliderPosition), maxSliderPosition);
    
    double positionRatio = (double)(position - minSliderPosition) / (maxSliderPosition - minSliderPosition);
    int value = (int)(positionRatio * maxValue + 0.5);
    setValue(value);
  }

  public int getSliderPosition()
  {
    return sliderPosition;
  }

  public int getMinSliderPosition()
  {
    return minSliderPosition;
  }

  public int getMaxSliderPosition()
  {
    return maxSliderPosition;
  }

  public Box getIncreasingButtonBounds()
  {
    return increasingButtonBounds;
  }

  public Box getDecreasingButtonBounds()
  {
    return decreasingButtonBounds;
  }

  public Box getTrackBounds()
  {
    return trackBounds;
  }

  public Box getSliderBounds()
  {
    return sliderBounds;
  }

  public ScrollMouseProcessor getMouseProcessor()
  {
    return mouseProcessor;
  }

  public boolean isIncreasingButtonActive()
  {
    return increasingButtonActive;
  }

  public boolean isDecreasingButtonActive()
  {
    return decreasingButtonActive;
  }

  public void setIncreasingButtonActive(boolean value)
  {
    increasingButtonActive = value;
  }

  public void setDecreasingButtonActive(boolean value)
  {
    decreasingButtonActive = value;
  }
}
