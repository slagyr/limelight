package limelight.ui;

import limelight.Rectangle;
import java.util.LinkedList;

public class PanelLayout
{
	private LinkedList<Row> rows;
	private Row currentRow;
  private BlockPanel panel;
  private Rectangle area;
  private int consumedHeight;

  public PanelLayout(BlockPanel panel)
  {
    this.panel = panel;
    rows = new LinkedList<Row>();
	}

  public void doLayout()
	{
		if(panel.getChildren().size() == 0)
			return;

    reset();
    buildRows();

    Aligner aligner = buildAligner(panel.getRectangleInsidePadding());
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
    for(AbstractPanel child : panel.getChildren())
    {
      child.snapToSize();
      if (!currentRow.isEmpty() && !currentRow.fits(child))
        newRow();
      currentRow.add(child);
    }
    calculateConsumedDimentions();
  }

  private Aligner buildAligner(Rectangle rectangle)
  {
    Block block = panel.getBlock();
    return new Aligner(rectangle, block.getStyle().getHorizontalAlignment(), block.getStyle().getVerticalAlignment());
  }

  private void reset()
	{
    area = panel.getRectangleInsidePadding();
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

  public BlockPanel getPanel()
  {
    return panel;
  }

  private class Row
  {
    private LinkedList<AbstractPanel> items;
    private int maxWidth;
    public int width;
    public int height;

    public Row(int maxWidth)
    {
      this.maxWidth = maxWidth;
      width = 0;
      height = 0;
      items = new LinkedList<AbstractPanel>();
    }

    public void add(AbstractPanel panel)
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

    public boolean fits(AbstractPanel panel)
    {
      return (width + panel.getWidth()) <= maxWidth;
    }

    public void layoutComponents(int x, int y)
    {
      for (AbstractPanel panel : items)
      {
        panel.setLocation(x, y);
        x += panel.getWidth();
      }
    }
  }
}

