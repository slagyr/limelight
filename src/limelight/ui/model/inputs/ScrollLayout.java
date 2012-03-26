//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model.inputs;

import limelight.styles.Style;
import limelight.ui.Panel;
import limelight.ui.model.Layout;
import limelight.ui.model.PropPanel;
import limelight.ui.model.PropPanelLayout;

import java.awt.*;
import java.util.LinkedList;
import java.util.Map;

public class ScrollLayout extends PropPanelLayout
{
  private ScrollBarPanel scrollBar;

  public ScrollLayout(ScrollBarPanel scrollBar)
  {
    this.scrollBar = scrollBar;
  }

  @Override
  public void doExpansion(Panel thePanel)
  {

  }

  @Override
  public void doContraction(Panel thePanel)
  {
    PropPanel panel = (PropPanel) thePanel;
    int dx = scrollBar.isHorizontal() ? scrollBar.getValue() : 0;
    int dy = scrollBar.isVertical() ? scrollBar.getValue() : 0;

    LinkedList<PropPanelLayout.Row> rows = buildRows(panel);
    Dimension consumedDimensions = new Dimension();
    calculateConsumedDimentions(rows, consumedDimensions);
    layoutRows(panel, consumedDimensions, rows, dx, dy);
  }

  @Override
  public void doFinalization(Panel panel)
  {
    panel.markAsDirty();
  }

  public void doLayout(Panel aPanel, Map<Panel, Layout> panelsToLayout)
  {
//    PropPanel panel = (PropPanel) aPanel;
//    int dx = scrollBar.isHorizontal() ? scrollBar.getValue() : 0;
//    int dy = scrollBar.isVertical() ? scrollBar.getValue() : 0;
//
//    LinkedList<PropPanelLayout.Row> rows = buildRows(panel);
//    Dimension consumedDimensions = new Dimension();
//    calculateConsumedDimentions(rows, consumedDimensions);
//    layoutRows(panel, consumedDimensions, rows, dx, dy);
//
//    panel.markAsDirty();
//    panel.wasLaidOut();
  }

  public void layoutRows(PropPanel panel, Dimension consumeDimension, LinkedList<Row> rows, int dx, int dy)
  {
    Style style = panel.getStyle();
    int y = style.getCompiledVerticalAlignment().getY(consumeDimension.height, panel.getChildConsumableBounds());
    y = Math.max(0, y);
    y -= dy;
    for(Row row : rows)
    {
      int x = style.getCompiledHorizontalAlignment().getX(row.width, panel.getChildConsumableBounds());
      x = Math.max(0, x);
      x -= dx;
      row.layoutComponents(x, y, style.getCompiledVerticalAlignment());
      y += row.height;
    }
  }
}
