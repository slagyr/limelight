package limelight.ui;

import java.util.LinkedList;

public abstract class PanelLayout
{
	private LinkedList<Row> rows;
	private Row currentRow;
  protected ParentPanel panel;
  protected Rectangle area;
  protected int consumedHeight;
  protected int consumedWidth;

  public PanelLayout(ParentPanel panel)
  {
    this.panel = panel;
    rows = new LinkedList<Row>();
	}

  public abstract void doLayout();

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

  protected void buildRows()
	{
    for(Panel child : panel.getChildren())
    {
      if (!currentRow.isEmpty() && !currentRow.fits(child))
        newRow();
      currentRow.add(child);
    }
    calculateConsumedDimentions();
  }

  protected Aligner buildAligner(Rectangle rectangle)
  {
    Style style = panel.getBlock().getStyle();
    return new Aligner(rectangle, style.getHorizontalAlignment(), style.getVerticalAlignment());
  }

  protected void reset()
	{
    rows.clear();
		newRow();
	}

	private void newRow()
	{
		currentRow = new Row(area.width);
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

  public ParentPanel getPanel()
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

