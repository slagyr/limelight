//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.styles.ScreenableStyle;
import limelight.ui.model.BasePanel;
import limelight.ui.model.inputs.painting.ScrollBarPainter;

import java.awt.*;
import java.awt.event.*;

public class ScrollBar2Panel extends BasePanel
{
  public static final int VERTICAL = 0;
  public static final int HORIZONTAL = 1;

  private ScrollBarPainter painter;
  private final int preferredGirth;
  private int orientation;
  private int availableAmount;
  private int visibleAmount;
  private int unitIncrement;
  private int blockIncrement;
  private int value;
  private int maxValue;
  private int gemSize;
  private int gemLocation;
  private int minGemLocation;
  private int maxGemLocation;
  private int gemPlay;

  public ScrollBar2Panel(int orientation)
  {
    painter = ScrollBarPainter.instance;
    this.orientation = orientation;
    if(getOrientation() == VERTICAL)
      preferredGirth = width = 15;
    else
      preferredGirth = height = 15;
  }

  public int getOrientation()
  {
    return orientation;
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

  public void setSize(int width, int height)
  {
    if(getOrientation() == VERTICAL)
      width = preferredGirth;
    else
      height = preferredGirth;
    super.setSize(width, height);
  }

  public void setHeight(int height)
  {
    setSize(preferredGirth, height);
  }

  public void setWidth(int width)
  {
    setSize(width, preferredGirth);
  }

  public void mousePressed(MouseEvent e)
  {
  }

  public void mouseReleased(MouseEvent e)
  {
  }

  public void mouseClicked(MouseEvent e)
  {
  }

  public void mouseDragged(MouseEvent e)
  {
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
    if(this.value != value)
    {
      this.value = value;
      calculateGemLocation();
      configurationChanged();
    }
  }

  private synchronized void configurationChanged()
  {
    //TODO - could improve performance here... no need to create new instance of layout every time
    BasePanel parent = (BasePanel) getParent();
    if(parent != null)
      parent.markAsNeedingLayout(new ScrollLayout(getOrientation(), this));
  }

  public int getValue()
  {
    return value;
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

  public int getBlockIncrement()
  {
    return blockIncrement;
  }

  public int getGemSize()
  {
    return gemSize;
  }

  public void configure(int visible, int available)
  {
    synchronized(this)
    {
      availableAmount = available;
      visibleAmount = visible;
      maxValue = available - visible;
      unitIncrement = (int) (visible * 0.1);
      blockIncrement = (int) (visible * 0.9);
      calculateGemSize();
      calculateGemLocation();
    }
    configurationChanged();
  }

  private void calculateGemSize()
  {
    double proportion = (double) visibleAmount / availableAmount;
    int fullSize = isHorizontal() ? getWidth() : getHeight();
    int avaiableSize = fullSize - painter.getOutsideCusion() - painter.getInsideCushion();
    int calculatedGemSize = (int) (avaiableSize * proportion + 0.5);
    gemSize = Math.max(calculatedGemSize, painter.getMinGemSize());
    minGemLocation = painter.getOutsideCusion();
    maxGemLocation = fullSize - painter.getInsideCushion() - gemSize;
    gemPlay = maxGemLocation - minGemLocation;
  }

  private void calculateGemLocation()
  {
    double valueRatio = (double)value / maxValue;
    gemLocation = minGemLocation + (int)(gemPlay * valueRatio + 0.5);
    
  }

  public boolean isHorizontal()
  {
    return orientation == HORIZONTAL;
  }

  public int getGemLocation()
  {
    return gemLocation;
  }
}
