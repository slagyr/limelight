package limelight.ui;

import java.util.LinkedList;

public class PanelLayout
{
	private LinkedList<Row> rows;
	private Row currentRow;
  private ParentPanel panel;
  private Rectangle area;
  private int consumedHeight;
  private Style style;

  public PanelLayout(ParentPanel panel)
  {
    this.panel = panel;
    rows = new LinkedList<Row>();
	}

  public void doLayout()
	{
    style = panel.getBlock().getStyle();
    if(style.changed(Style.WIDTH) || style.changed(Style.HEIGHT))
      snapPanelToSize(panel);

    if(panel.getChildren().size() == 0)
			return;

    doLayoutOnChildren();
    area = panel.getChildConsumableArea();
    reset();
    buildRows();

    Aligner aligner = buildAligner(area);
    layoutRows(aligner);
  }

  private void doLayoutOnChildren()
  {
    for(Panel child : panel.getChildren())
      child.doLayout();
  }

  private void layoutRows(Aligner aligner)
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

  private void buildRows()
	{
    for(Panel child : panel.getChildren())
    {
      if (!currentRow.isEmpty() && !currentRow.fits(child))
        newRow();
      currentRow.add(child);
    }
    calculateConsumedDimentions();
  }

  private Aligner buildAligner(Rectangle rectangle)
  {
    return new Aligner(rectangle, style.getHorizontalAlignment(), style.getVerticalAlignment());
  }

  private void reset()
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
    int consumedWidth = 0;
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

  public void snapPanelToSize(Panel panel)
  {
    style = panel.getBlock().getStyle();
    if(panel.getParent() != null)
    {
      Rectangle r = panel.getParent().getChildConsumableArea();
      panel.setWidth(translateDimension(style.getWidth(), r.width));
      panel.setHeight(translateDimension(style.getHeight(), r.height));
    }
    else
    {
      panel.setWidth(style.asInt(style.getWidth()));
      panel.setHeight(style.asInt(style.getHeight()));
    }
  }

  private int translateDimension(String sizeString, int maxSize)
  {
    if (sizeString == null)
      return 0;
    else if (sizeString.endsWith("%"))
    {
      double percentage = Double.parseDouble(sizeString.substring(0, sizeString.length() - 1));
      return (int) ((percentage * 0.01) * (double) maxSize);
    }
    else
    {
      return Integer.parseInt(sizeString);
    }
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

