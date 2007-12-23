package limelight;

import java.awt.*;

public abstract class Painter
{
  protected Panel panel;

  public Painter(Panel panel)
  {
    this.panel = panel;
  }

  public Painter()
  {
  }

  public abstract void paint(Graphics2D graphics);

  protected int resolveInt(String value)
	{
		try
		{
			return Integer.parseInt(value);
		}
		catch(NumberFormatException e)
		{
			return 0;
		}
	}

  protected Style getStyle()
  {
    return panel.getBlock().getStyle();
  }
}
