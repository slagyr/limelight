//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.styles.Style;
import limelight.styles.styling.GreedyDimensionAttribute;
import limelight.styles.styling.AutoDimensionAttribute;
import limelight.styles.abstrstyling.VerticalAlignmentAttribute;
import limelight.ui.Panel;
import limelight.ui.model.inputs.ScrollBarPanel;
import limelight.util.Box;
import limelight.util.Debug;
import limelight.LimelightException;

import java.util.LinkedList;
import java.util.List;
import java.awt.*;

public class PropPanelLayout implements Layout
{
  public static PropPanelLayout instance = new PropPanelLayout();


  // TODO MDM This gets called ALOT!  Possible speed up by re-using objects, rather then reallocating them. (rows list, rows)
  public void doLayout(Panel thePanel, boolean topLevel)
  {
    PropPanel panel = (PropPanel) thePanel;
    panel.resetLayout();
    FloaterLayout.instance.doLayout(panel);
    Style style = panel.getStyle();
    if(panel.sizeChangePending() || style.hasDynamicDimension())
      snapToSize(panel, topLevel);

    establishScrollBars(panel);

    Dimension consumedDimensions = new Dimension();
    if(!hasNonScrollBarChildren(panel))
      collapseAutoDimensions(panel, consumedDimensions);
    else
    {
      doPreliminaryLayoutOnChildren(panel);
      LinkedList<Row> rows = buildRows(panel);
      distributeGreediness(panel, rows);
      doPostLayoutOnChildren(panel);
      calculateConsumedDimentions(rows, consumedDimensions);
      collapseAutoDimensions(panel, consumedDimensions);
      layoutRows(panel, consumedDimensions, rows);
    }
    layoutScrollBars(panel, consumedDimensions);

    panel.updateBorder();
    panel.markAsDirty();
    panel.wasLaidOut();
  }

  private void distributeGreediness(PropPanel panel, LinkedList<Row> rows)
  {
    for(Row row : rows)
      row.distributeGreeyWidth();
    distributeGreedyHeight(panel, rows);
  }

  private void distributeGreedyHeight(PropPanel panel, LinkedList<Row> rows)
  {
    int consumedHeight = 0;
    int greedyRows = 0;
    for(Row row : rows)
    {
      consumedHeight += row.height;
      if(row.isGreedy())
        greedyRows++;
    }

    int leftOver = panel.getChildConsumableArea().height - consumedHeight;
    if(leftOver > 0 && greedyRows > 0)
    {
      int[] split = splitEvenly(leftOver, greedyRows);
      int splitIndex = 0;
      for(Row row : rows)
      {
        if(row.isGreedy())
          row.addGreedyHeight(split[splitIndex++]);
      }
    }
  }

  private int[] splitEvenly(int amount, int parts)
  {
    int[] split = new int[parts];
    int part = amount / parts;
    int remainder = amount % parts;
    for(int i = 0; i < split.length; i++)
    {
      split[i] = part;
      if(remainder > 0)
      {
        split[i] += 1;
        remainder--;
      }
    }
    return split;
  }

  public boolean overides(Layout other)
  {
    return true;
  }

  public void doLayout(Panel child)
  {
    doLayout(child, true);
  }

  private boolean hasNonScrollBarChildren(PropPanel panel)
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

  private void layoutScrollBars(PropPanel panel, Dimension consumedDimensions)
  {
    ScrollBarPanel vertical = panel.getVerticalScrollBar();
    ScrollBarPanel horizontal = panel.getHorizontalScrollBar();
    Box area = panel.getChildConsumableArea();
    if(vertical != null)
    {
      vertical.setHeight(area.height);
      vertical.setLocation(area.right() + 1, area.y);
      vertical.configure(area.height, consumedDimensions.height);
    }
    if(horizontal != null)
    {
      horizontal.setWidth(area.width);
      horizontal.setLocation(area.x, area.bottom() + 1);
      horizontal.configure(area.width, consumedDimensions.width);
    }
  }

  private void collapseAutoDimensions(PropPanel panel, Dimension consumedDimensions)
  {
    Style style = panel.getStyle();

    int width = style.getCompiledWidth().collapseExcess(panel.getWidth(), consumedDimensions.width + horizontalInsets(panel), style.getCompiledMinWidth(), style.getCompiledMaxWidth());
    int height = style.getCompiledHeight().collapseExcess(panel.getHeight(), consumedDimensions.height + verticalInsets(panel), style.getCompiledMinHeight(), style.getCompiledMaxHeight());

    panel.setSize(width, height);
  }

  public int horizontalInsets(PropPanel panel)
  {
    return panel.getBoundingBox().width - panel.getBoxInsidePadding().width;
  }

  public int verticalInsets(PropPanel panel)
  {
    return panel.getBoundingBox().height - panel.getBoxInsidePadding().height;
  }

  protected void doPreliminaryLayoutOnChildren(PropPanel panel)
  {
    for(Panel child : panel.getChildren())
    {
      if(child.needsLayout())
      {
        if(!(child instanceof PropPanel) || child.getStyle().getCompiledWidth().isAuto())
        {
          child.getDefaultLayout().doLayout(child, true);
        }
        else
        {
          ((PropPanel)child).greediness.setSize(0, 0);
          snapToSize((PropPanel) child, false);
        }
      }
    }
  }

  protected void doPostLayoutOnChildren(PropPanel panel)
  {
    for(Panel child : panel.getChildren())
    {
      if(child.needsLayout())
      {       
        child.getDefaultLayout().doLayout(child, false);
      }
    }
  }

  public void layoutRows(PropPanel panel, Dimension consumedDimension, LinkedList<Row> rows)
  {
    Style style = panel.getProp().getStyle();
    int y = style.getCompiledVerticalAlignment().getY(consumedDimension.height, panel.getChildConsumableArea());
    if(panel.getVerticalScrollBar() != null)
      y -= panel.getVerticalScrollBar().getValue();
    for(Row row : rows)
    {
      int x = style.getCompiledHorizontalAlignment().getX(row.width, panel.getChildConsumableArea());
      if(panel.getHorizontalScrollBar() != null)
        x -= panel.getHorizontalScrollBar().getValue();
      row.layoutComponents(x, y, style.getCompiledVerticalAlignment());
      y += row.height;
    }
  }

  protected LinkedList<Row> buildRows(PropPanel panel)
  {
    LinkedList<Row> rows = new LinkedList<Row>();
    Row currentRow = newRow(panel, rows);

    for(Panel child : panel.getChildren())
    {
      if(!(child instanceof ScrollBarPanel) && !child.isFloater())
      {
        if(!currentRow.isEmpty() && !currentRow.fits(child))
        {
          currentRow = newRow(panel, rows);
        }
        currentRow.add(child);
      }
    }
    return rows;
  }

  private Row newRow(PropPanel panel, LinkedList<Row> rows)
  {
    Row currentRow = new Row(panel.getChildConsumableArea().width);
    rows.add(currentRow);
    return currentRow;
  }

  protected void calculateConsumedDimentions(LinkedList<Row> rows, Dimension consumedDimensions)
  {
    for(Row row : rows)
    {
      row.reCalculatedHeight();
      consumedDimensions.height += row.height;
      if(row.width > consumedDimensions.width)
        consumedDimensions.width = row.width;
    }
  }

  public void establishScrollBars(PropPanel panel)
  {
    Style style = panel.getStyle();
    if(panel.getVerticalScrollBar() == null && style.getCompiledVerticalScrollbar().isOn())
      panel.addVerticalScrollBar();
    else if(panel.getVerticalScrollBar() != null && style.getCompiledVerticalScrollbar().isOff())
      panel.removeVerticalScrollBar();

    if(panel.getHorizontalScrollBar() == null && style.getCompiledHorizontalScrollbar().isOn())
      panel.addHorizontalScrollBar();
    else if(panel.getHorizontalScrollBar() != null && style.getCompiledHorizontalScrollbar().isOff())
      panel.removeHorizontalScrollBar();
  }

  public void snapToSize(PropPanel panel, boolean topLevel)
  {
    if(panel.getParent() == null) // can happen if removed from tree
      return;
    Box maxArea = panel.getParent().getChildConsumableArea();

    Style style = panel.getStyle();
    if(style.getCompiledWidth() instanceof AutoDimensionAttribute && style.getCompiledHeight() instanceof GreedyDimensionAttribute)
      throw new LimelightException("A greedy height is not allowed with auto width.");

    int newWidth = style.getCompiledWidth().calculateDimension(maxArea.width, style.getCompiledMinWidth(), style.getCompiledMaxWidth(), panel.greediness.width);
    int newHeight = style.getCompiledHeight().calculateDimension(maxArea.height, style.getCompiledMinHeight(), style.getCompiledMaxHeight(), panel.greediness.height);

    // TODO MDM - Hacky Hack!!!!  More thought needs to go into the way layouts are down and how greedy fits into it all
//    if(topLevel && style.getCompiledWidth() instanceof GreedyDimensionAttribute && panel.getWidth() > newWidth)
//      newWidth = panel.getWidth();
//    if(topLevel && style.getCompiledHeight() instanceof GreedyDimensionAttribute && panel.getHeight() > newHeight)
//      newHeight = panel.getHeight();

    panel.setSize(newWidth, newHeight);
    panel.resetPendingSizeChange();
  }

  protected class Row
  {
    private final LinkedList<Panel> items;
    private final int maxWidth;
    public int width;
    public int height;
    private int greedyWidths;
    private int greedyHeights;

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

      if(hasGreedyWidth(panel))
        greedyWidths += 1;
      if(hasGreedyHeight(panel))
      {
        greedyHeights += 1;
      }
    }

    public boolean isEmpty()
    {
      return items.size() == 0;
    }

    public boolean fits(Panel panel)
    {
      return (width + panel.getWidth()) <= maxWidth;
    }

    public void layoutComponents(int x, int y, VerticalAlignmentAttribute verticalAlignment)
    {
      Box area = new Box(x, y, width, height);
      for(Panel panel : items)
      {
        int alignedY = verticalAlignment.getY(panel.getHeight(), area);
        panel.setLocation(x, alignedY);
        x += panel.getWidth();
      }
    }

    public void distributeGreeyWidth()
    {
      int leftOver = maxWidth - width;
      if(leftOver > 0 && greedyWidths > 0)
      {
        width = maxWidth;
        int[] splits = splitEvenly(leftOver, greedyWidths);
        int splitIndex = 0;
        for(Panel item : items)
        {
          if(hasGreedyWidth(item))
          {
            PropPanel panel = (PropPanel) item;
            int greedyWidth = splits[splitIndex++];
            panel.greediness.width = greedyWidth;
            item.setSize(item.getWidth() + greedyWidth, item.getHeight());
          }
        }
      }
    }

    private boolean hasGreedyWidth(Panel item)
    {
      return item instanceof PropPanel && item.getStyle().getCompiledWidth() instanceof GreedyDimensionAttribute;
    }

    private boolean hasGreedyHeight(Panel item)
    {
      return item instanceof PropPanel && item.getStyle().getCompiledHeight() instanceof GreedyDimensionAttribute;
    }

    public boolean isGreedy()
    {
      return greedyHeights > 0;
    }

    public void addGreedyHeight(int extraHeight)
    {
      height += extraHeight;
      for(Panel item : items)
      {
        if(item instanceof PropPanel && item.getStyle().getCompiledHeight() instanceof GreedyDimensionAttribute)
        {
          PropPanel panel = (PropPanel)item;
          panel.greediness.height = extraHeight;
          item.setSize(item.getWidth(), height);
        }
      }
    }

    public void reCalculatedHeight()
    {
      height = 0;
      for(Panel item : items)
      {
        if(item.getHeight() > height)
          height = item.getHeight();
      }
    }
  }

}

