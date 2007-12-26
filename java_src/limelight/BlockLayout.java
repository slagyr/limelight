package limelight;

import java.awt.*;
import java.util.*;

public class BlockLayout implements LayoutManager
{
	private Panel panel;
	private LinkedList<Row> rows;
	private Row currentRow;
	private Aligner aligner;

	public BlockLayout(Panel panel)
	{
		this.panel = panel;
		rows = new LinkedList<Row>();
	}

	public void addLayoutComponent(String string, Component component)
	{
//		System.out.println("addLayoutComponent(" + string + ", " + component + ")");
	}

	public void removeLayoutComponent(Component component)
	{
//		System.out.println("removeLayoutComponent(" + component + ")");
	}

	public Dimension preferredLayoutSize(Container container)
	{
//		System.out.println("prefereedLayoutSize(" + container + ")");
		return container.getPreferredSize();
	}

	public Dimension minimumLayoutSize(Container container)
	{
//		System.out.println("minimumLayoutSize(" + container + ")");
		return container.getMinimumSize();
	}

	public void layoutContainer(Container container)
	{
		Component[] components = container.getComponents();
		if(components.length == 0)
			return;
    reset(container);
		buildRows(components);

    int y = aligner.startingY();
    for(Row row : rows)
    {
      int x = aligner.startingX(row.width);
      row.layoutComponents(x, y);
      y += row.height;
    }
	}

	private void buildRows(Component[] components)
	{
    for(Component component : components)
    {

      Panel panel = ((Panel) component);
      panel.snapToDesiredSize();
      panel.snapOffsets();

      if (!currentRow.isEmpty() && !currentRow.fits(component))
      {
        aligner.addConsumedHeight(currentRow.height);
        newRow();
      }
      currentRow.add(panel);
    }
		aligner.addConsumedHeight(currentRow.height);
	}

	private void reset(Container container)
	{
		Block block = ((Panel) container).getBlock();
    aligner = new Aligner(block.getPanel().getRectangleInsidePadding(), block.getStyle().getHorizontalAlignment(), block.getStyle().getVerticalAlignment());
    rows.clear();
		newRow();
	}

	private void newRow()
	{
		currentRow = new Row(aligner.getWidth());
		rows.add(currentRow);
	}

	private class Row
	{
		private LinkedList<Component> items;
		private int maxWidth;
		public int width;
		public int height;

		public Row(int maxWidth)
		{
			this.maxWidth = maxWidth;
			width = 0;
			height = 0;
			items = new LinkedList<Component>();
		}

		public void add(Panel panel)
		{
			items.add(panel);
			width += panel.getWidth();
			if(panel.getHeight() + panel.getYOffset() > height)
				height = panel.getHeight() + panel.getYOffset();
		}

		public boolean isEmpty()
		{
			return items.size() == 0;
		}

		public boolean fits(Component component)
		{
			return (width + component.getWidth()) <= maxWidth;
		}

		public void layoutComponents(int x, int y)
		{
      for (Component component : items)
      {
        Panel panel = (Panel)component;
        panel.setLocation(x + panel.getXOffset(), y + panel.getYOffset());
        x += component.getWidth() + panel.getXOffset();
      }
		}
	}
}
