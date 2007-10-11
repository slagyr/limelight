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
		System.out.println("addLayoutComponent(" + string + ", " + component + ")");
	}

	public void removeLayoutComponent(Component component)
	{
		System.out.println("removeLayoutComponent(" + component + ")");
	}

	public Dimension preferredLayoutSize(Container container)
	{
		System.out.println("prefereedLayoutSize(" + container + ")");
		return container.getPreferredSize();
	}

	public Dimension minimumLayoutSize(Container container)
	{
		System.out.println("minimumLayoutSize(" + container + ")");
		return container.getMinimumSize();
	}

	public void layoutContainer(Container container)
	{
		System.out.println("layoutContainer(" + container + ")");
		Component[] components = container.getComponents();
		if(components.length == 0)
			return;
		reset(container);
		buildRows(components);

		int y = aligner.startingY();
		for(Iterator iterator = rows.iterator(); iterator.hasNext();)
		{
			Row row = (Row) iterator.next();
			int x = aligner.startingX(row.width);
			row.layoutComponents(x, y);
			y += row.height;
		}
	}

	private void buildRows(Component[] components)
	{
		for(int i = 0; i < components.length; i++)
		{
			Component component = components[i];
			translateAndSetSizeFor(component);

			if(!currentRow.isEmpty() && !currentRow.fits(component))
			{
				aligner.addConsumedHeight(currentRow.height);
				newRow();
			}
			currentRow.add(component);
		}
		aligner.addConsumedHeight(currentRow.height);
	}

	private void translateAndSetSizeFor(Component component)
	{
		Panel currentPanel = (Panel)component;
		int width = translateDimension(currentPanel.getBlock().getWidth(), panel.getWidth());
		int height = translateDimension(currentPanel.getBlock().getHeight(), panel.getHeight());
		component.setSize(width, height);
	}

	private int translateDimension(String sizeString, int maxSize)
	{
		if(sizeString.endsWith("%"))
		{
			double percentage = Double.parseDouble(sizeString.substring(0, sizeString.length() - 1));
			return (int)((percentage * 0.01) * (double)maxSize);
		}
		else
		{
			return Integer.parseInt(sizeString);
		}
	}

	private void reset(Container container)
	{
		Block block = ((Panel) container).getBlock();
		aligner = new Aligner(block.getRectangleInsidePadding(), block.getHorizontalAlignment(), block.getVerticalAlignment());
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

		public void add(Component component)
		{
			items.add(component);
			width += component.getWidth();
			if(component.getHeight() > height)
				height = component.getHeight();
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
			for(Iterator iterator = items.iterator(); iterator.hasNext();)
			{
				Component component = (Component) iterator.next();
				component.setLocation(x, y);
				x += component.getWidth();
			}
		}
	}
}
