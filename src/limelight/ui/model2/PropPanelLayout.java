package limelight.ui.model2;

import limelight.styles.Style;
import limelight.util.Box;
import limelight.util.Aligner;
import limelight.ui.Panel;

import java.util.LinkedList;

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

  public void doLayout()
	{
    Style style = panel.getProp().getStyle();
    if(style.changed(Style.WIDTH) || style.changed(Style.HEIGHT))
      panel.snapToSize();

    if(panel.getChildren().size() == 0)
    {
      collapseAutoDimensions();
      return;
    }

    doLayoutOnChildren();
    reset();
    buildRows();
    collapseAutoDimensions();

    boolean switched = false;//switchToScrollModeIfNeeded();

    if(!switched)
    {
      Aligner aligner = buildAligner(panel.getChildConsumableArea());
      layoutRows(aligner);
      layoutFloaters();
    }
  }

  private void collapseAutoDimensions()
  {
    boolean hasAutoWidth = "auto".equals(panel.getStyle().getWidth());
    boolean hasAutoHeight = "auto".equals(panel.getStyle().getHeight());
    if(hasAutoWidth && hasAutoHeight)
      panel.setSize(consumedWidth + horizontalInsets(), consumedHeight + verticalInsets());
    else if(hasAutoWidth)
      panel.setSize(consumedWidth + horizontalInsets(), panel.getHeight());
    else if(hasAutoHeight)
      panel.setSize(panel.getWidth(), consumedHeight + verticalInsets());
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
      child.doLayout();
  }

  protected void layoutRows(Aligner aligner)
  {
    aligner.addConsumedHeight(consumedHeight);
    int y = aligner.startingY();
    for(Row row : rows)
    {
      int x = aligner.startingX(row.width);
      row.layoutComponents(x, y);
      y += row.height;
    }
  }

  private void layoutFloaters()
  {
    if(floaters == null)
      return;
    for (Panel floater : floaters)
      layoutFloater(floater);
  }

  private void layoutFloater(Panel floater)
  {
    Style style = floater.getStyle();
    int x = style.asInt(style.getX());
    int y = style.asInt(style.getY());
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
    for(Panel child : panel.getChildren())
    {
      if(child.isFloater())
        addFloater(child);
      else
        addToRow(child);
    }
    calculateConsumedDimentions();
  }

  private void addToRow(Panel child)
  {
    if (!currentRow.isEmpty() && !currentRow.fits(child))
      newRow();
    currentRow.add(child);
  }

  protected Aligner buildAligner(Box rectangle)
  {
    Style style = panel.getProp().getStyle();
    return new Aligner(rectangle, style.getHorizontalAlignment(), style.getVerticalAlignment());
  }

  protected void reset()
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
    consumedWidth = 0;
    consumedHeight = 0;
    for (Row row : rows)
    {
      consumedHeight += row.height;
      if(row.width > consumedWidth)
        consumedWidth = row.width;
    }
  }

  public PropPanel getPanel()
  {
    return panel;
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
      for (Panel panel : items)
      {
        panel.setLocation(x, y);
        x += panel.getWidth();
      }
    }
  }
}

