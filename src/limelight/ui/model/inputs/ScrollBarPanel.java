//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.styles.ScreenableStyle;
import limelight.ui.events.MouseDraggedEvent;
import limelight.ui.events.MouseExitedEvent;
import limelight.ui.events.MousePressedEvent;
import limelight.ui.events.MouseReleasedEvent;
import limelight.ui.model.PanelBase;
import limelight.ui.model.inputs.painting.ScrollBarPainter;
import limelight.util.Box;

public class ScrollBarPanel extends PanelBase
{
  public static final int VERTICAL = 0;
  public static final int HORIZONTAL = 1;

  public static final int GIRTH = 15;

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
  private int sliderPosition;
  private Box increasingButtonBounds;
  private Box decreasingButtonBounds;
  private boolean increasingButtonActive;
  private boolean decreasingButtonActive;
  private Box trackBounds;
  private Box sliderBounds;

  public ScrollBarPanel(int orientation)
  {
    this.orientation = orientation;
    if(getOrientation() == VERTICAL)
    {
      preferredGirth = width = GIRTH;
      setSize(preferredGirth, 50);
      sliderBounds = new Box(0, 0, preferredGirth, 0);
    }
    else
    {
      preferredGirth = height = GIRTH;
      setSize(50, preferredGirth);
      sliderBounds = new Box(0, 0, 0, preferredGirth);
    }
    
    getEventHandler().add(MousePressedEvent.class, mouseProcessor);
    getEventHandler().add(MouseReleasedEvent.class, mouseProcessor);
    getEventHandler().add(MouseDraggedEvent.class, mouseProcessor);
    getEventHandler().add(MouseExitedEvent.class, mouseProcessor);
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

  public void paintOn(java.awt.Graphics2D graphics)
  {
    painter.paintOn(graphics, this);
  }

  public boolean canBeBuffered()
  {
    return false;
  }

  private synchronized void configurationChanged()
  {
    //TODO - could improve performance here... no need to create new instance of layout every time
    PanelBase parent = (PanelBase) getParent();
    if(parent != null)
      parent.markAsNeedingLayout(new ScrollLayout(this));
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
    int calculatedSliderSize = (int) (getTrackSize() * proportion + 0.5);
    int sliderSize = Math.max(calculatedSliderSize, painter.getMinSliderSize());

    if(isHorizontal())
      sliderBounds.width = sliderSize;
    else
      sliderBounds.height = sliderSize;
  }

  private void calculateSliderPosition()
  {
    double valueRatio = (double) value / maxValue;
    sliderPosition = getMinSliderPosition() + (int) (getSliderPlay() * valueRatio + 0.5);

    if(isHorizontal())
      sliderBounds.x = sliderPosition;
    else
      sliderBounds.y = sliderPosition;
  }

  public void setSliderPosition(int position)
  {
    sliderPosition = Math.min(Math.max(position, getMinSliderPosition()), getMaxSliderPosition());
    
    double positionRatio = (double)(position - getMinSliderPosition()) / getSliderPlay();
    int value = (int)(positionRatio * maxValue + 0.5);
    setValue(value);
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
      markAsDirty();
    }
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
    return isHorizontal() ? sliderBounds.width : sliderBounds.height;
  }

  public int getTrackSize()
  {
    return isHorizontal() ? trackBounds.width : trackBounds.height;
  }

  private int getSliderPlay()
  {
    return getMaxSliderPosition() - getMinSliderPosition();
  }

  public int getSliderPosition()
  {
    return sliderPosition;
  }

  public int getMinSliderPosition()
  {
    return isHorizontal() ? trackBounds.x : trackBounds.y;
  }

  public int getMaxSliderPosition()
  {
    return (isHorizontal() ? trackBounds.width + trackBounds.x : trackBounds.height + trackBounds.y) - getSliderSize();
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
    markAsDirty();
  }

  public void setDecreasingButtonActive(boolean value)
  {
    decreasingButtonActive = value;
    markAsDirty();
  }

}
