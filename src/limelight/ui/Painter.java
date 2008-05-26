//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui;

import limelight.ui.*;

import java.awt.*;

public abstract class Painter
{
  protected limelight.ui.Panel panel;

  public Painter(limelight.ui.Panel panel)
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
    return panel.getStyle();
  }
}
