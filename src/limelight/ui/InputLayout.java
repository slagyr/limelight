//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui;

import java.awt.*;

public class InputLayout implements LayoutManager
{
  public void addLayoutComponent(String string, Component component)
	{
	}

	public void removeLayoutComponent(Component component)
	{
	}

	public Dimension preferredLayoutSize(Container container)
	{
		return container.getPreferredSize();
	}

	public Dimension minimumLayoutSize(Container container)
	{
		return container.getMinimumSize();
	}

  public void layoutContainer(Container parent)
  {
    parent.setSize(parent.getMaximumSize());
    for(Component component : parent.getComponents())
    {
      Dimension size = parent.getSize();
      component.setSize(size);
    }
  }
}
