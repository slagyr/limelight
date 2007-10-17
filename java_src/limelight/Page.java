package limelight;

import java.util.Hashtable;

public class Page extends Block
{
	private Hashtable<String, Style> styles;

	public Page()
	{
		super();
		setPage(this);
		styles = new Hashtable<String, Style>();
	}

	public Hashtable<String, Style> getStyles()
	{
		return styles;
	}
}


