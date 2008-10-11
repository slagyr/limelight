//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.styles.Style;
import limelight.ui.Panel;
import limelight.ui.model.inputs.ScrollBarPanel;
import limelight.util.Aligner;
import limelight.util.Box;

import java.util.LinkedList;
import java.util.List;

public class PropPanelLayout
{
  private LinkedList<Row> rows;
  private Row currentRow;
  protected PropPanel panel;
  protected int consumedHeight;
  protected int consumedWidth;
  private LinkedList<Panel> floaters;

  public PropPanelLayout(PropPanel panel)
  {
    this.panel = panel;
    rows = new LinkedList<Row>();
  }

  synchronized public void doLayout()
  {
    resetConsumedDimensions();

    Style style = panel.getStyle();

    if(panel.sizeChanged() || style.hasPercentageDimension() || style.hasAutoDimension())
      panel.snapToSize();

    establishScrollBars();

    if(!hasNonScrollBarChildren())
      collapseAutoDimensions();
    else
    {
      doLayoutOnChildren();
      buildRows();
      collapseAutoDimensions();
      layoutRows();
      layoutFloaters();
    }
    layoutScrollBars();
  }

  private boolean hasNonScrollBarChildren()
  {
    List<Panel> children = panel.getChildren();
    if(children.size() == 0)
      return false;
    else if(children.size() == 1)
      return !(children.contains(panel.getVerticalScrollBar()) || children.contains(panel.getHorizontalScrollBar()));
    else if(children.size() == 2)
      return !(children.contains(panel.getVerticalScrollBar()) && children.contains(panel.getHorizontalScrollBar()));
    else
      return true;
  }

  private void layoutScrollBars()
  {
    ScrollBarPanel vertical = panel.getVerticalScrollBar();
    ScrollBarPanel horizontal = panel.getHorizontalScrollBar();
    Box area = panel.getChildConsumableArea();
    if(vertical != null)
    {
      vertical.setHeight(area.height);
      vertical.setLocation(area.right() + 1, area.y);
      vertical.configure(area.height, consumedHeight);
    }
    if(horizontal != null)
    {
      horizontal.setWidth(area.width);
      horizontal.setLocation(area.x, area.bottom() + 1);
      horizontal.configure(area.width, consumedWidth);
    }
  }

  private void collapseAutoDimensions()
  {
    Style style = panel.getStyle();

    int width = style.getCompiledWidth().collapseExcess(panel.getWidth(), consumedWidth + horizontalInsets(), style.getCompiledMinWidth(), style.getCompiledMaxWidth());
    int height = style.getCompiledHeight().collapseExcess(panel.getHeight(), consumedHeight + verticalInsets(), style.getCompiledMinHeight(), style.getCompiledMaxHeight());

    panel.setSize(width, height);
  }

  public int horizontalInsets()
  {
    return panel.getBoundingBox().width - panel.getBoxInsidePadding().width;
  }

  public int verticalInsets()
  {
    return panel.getBoundingBox().height - panel.getBoxInsidePadding().height;
  }

  protected void doLayoutOnChildren()
  {
    for(Panel child : panel.getChildren())
    {

      if(child.needsLayout())
      {
        child.doLayout();
      }
    }
  }

  public void layoutRows()
  {
    Aligner aligner = buildAligner(panel.getChildConsumableArea());
    aligner.addConsumedHeight(consumedHeight);
    int y = aligner.startingY();
    if(panel.getVerticalScrollBar() != null)
      y -= panel.getVerticalScrollBar().getValue();
    for(Row row : rows)
    {
      int x = aligner.startingX(row.width);
      if(panel.getHorizontalScrollBar() != null)
        x -= panel.getHorizontalScrollBar().getValue();
      row.layoutComponents(x, y);
      y += row.height;
    }
  }

  private void layoutFloaters()
  {
    if(floaters == null)
      return;
    for(Panel floater : floaters)
      layoutFloater(floater);
  }

  //TODO Floater need to change position when scrolled too.
  private void layoutFloater(Panel floater)
  {
    Style style = floater.getStyle();
    int x = style.getCompiledX().getValue();
    int y = style.getCompiledY().getValue();
    Box area = panel.getChildConsumableArea();
    floater.setLocation(area.x + x, area.y + y);
  }

  private void addFloater(Panel panel)
  {
    if(floaters == null)
      floaters = new LinkedList<Panel>();
    floaters.add(panel);
  }

  protected void buildRows()
  {
    resetRows();
    for(Panel child : panel.getChildren())
    {
      if(child instanceof ScrollBarPanel)
        ;//ignore
      else if(child.isFloater())
        addFloater(child);
      else
        addToRow(child);
    }
    calculateConsumedDimentions();
  }

  private void addToRow(Panel child)
  {
    if(!currentRow.isEmpty() && !currentRow.fits(child))
      newRow();
    currentRow.add(child);
  }

  protected Aligner buildAligner(Box rectangle)
  {
    Style style = panel.getProp().getStyle();
    return new Aligner(rectangle, style.getCompiledHorizontalAlignment().getAlignment(), style.getCompiledVerticalAlignment().getAlignment());
  }

  protected void resetRows()
  {
    rows.clear();
    newRow();
  }

  private void newRow()
  {
    currentRow = new Row(panel.getChildConsumableArea().width);
    rows.add(currentRow);
  }

  private void calculateConsumedDimentions()
  {
    resetConsumedDimensions();
    for(Row row : rows)
    {
      consumedHeight += row.height;
      if(row.width > consumedWidth)
        consumedWidth = row.width;
    }
  }

  private void resetConsumedDimensions()
  {
    consumedWidth = 0;
    consumedHeight = 0;
  }

  public PropPanel getPanel()
  {
    return panel;
  }

  public void establishScrollBars()
  {
    Style style = panel.getStyle();
    if(panel.getVerticalScrollBar() == null && style.getCompiledVerticalScrollbar().isOn())
      panel.addVerticalScrollBar();
    else if(panel.getVerticalScrollBar() != null &&  style.getCompiledVerticalScrollbar().isOff())
      panel.removeVerticalScrollBar();
    if(panel.getHorizontalScrollBar() == null &&  style.getCompiledHorizontalScrollbar().isOn())
      panel.addHorizontalScrollBar();
    else if(panel.getHorizontalScrollBar() != null && style.getCompiledHorizontalScrollbar().isOff())
      panel.removeHorizontalScrollBar();

  }

  private class Row
  {
    private LinkedList<Panel> items;
    private int maxWidth;
    public int width;
    public int height;

    public Row(int maxWidth)
    {
      this.maxWidth = maxWidth;
      width = 0;
      height = 0;
      items = new LinkedList<Panel>();
    }

    public void add(Panel panel)
    {
      items.add(panel);
      width += panel.getWidth();
      if(panel.getHeight() > height)
        height = panel.getHeight();
    }

    public boolean isEmpty()
    {
      return items.size() == 0;
    }

    public boolean fits(Panel panel)
    {
      return (width + panel.getWidth()) <= maxWidth;
    }

    public void layoutComponents(int x, int y)
    {
      for(Panel panel : items)
      {
        panel.setLocation(x, y);
        x += panel.getWidth();
      }
    }
  }
}

