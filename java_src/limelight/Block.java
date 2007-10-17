package limelight;

import java.util.*;

public class Block
{
	private Panel panel;

	private String text;
	private Style style;
	private Page page;
	private String name;
	private LinkedList<Block> children;

	public Block()
	{
		panel = new Panel(this);
		children = new LinkedList<Block>();
		style = new Style();
		style.setBorderColor("white");
		style.setBackgroundColor("white");
		style.setBackgroundImageFillStrategy("repeat");
		panel.addMouseListener(new BlockMouseListener(this));
	}

	public void add(Block child)
	{
		children.add(child);
		panel.add(child.getPanel());
		child.setPage(page);
	}

	public Panel getPanel()
	{
		return panel;
	}

	public Style getStyle()
	{
		return style;
	}

	public Page getPage()
	{
		return page;
	}

	public void setPage(Page page)
	{
		this.page = page;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String value)
	{
		text = value;
	}

	public void setX(int x)
	{
		panel.setLocation(x, panel.getLocation().y);
	}

	public void setY(int y)
	{
		panel.setLocation(panel.getLocation().x, y);
	}

	public void mouseEntered()
	{
	}

	public void mouseExited()
	{

	}

	public void loadStyle()
	{
		if(name != null)
		{
			Style newStyle = page.getStyles().get(name);
			if(newStyle != null)
				style = newStyle;
		}
		for(Iterator iterator = children.iterator(); iterator.hasNext();)
		{
			Block block = (Block) iterator.next();
			block.loadStyle();
		}
	}
}
