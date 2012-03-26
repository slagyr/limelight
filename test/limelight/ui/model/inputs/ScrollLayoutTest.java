//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model.inputs;

import limelight.model.api.FakePropProxy;
import limelight.ui.model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ScrollLayoutTest
{
  private PropPanel parent;

  @Before
  public void setUp() throws Exception
  {
    Scene root = new FakeScene();
    parent = new PropPanel(new FakePropProxy());
    root.add(parent);
    parent.getStyle().setWidth("100");
    parent.getStyle().setHeight("100");
  }

  @Test
  public void scrollContentThatIsntAlignedTopLeft() throws Exception
  {
    parent.getStyle().setScrollbars("on");
    parent.getStyle().setAlignment("center");
    PropPanel panel = addChildWithSize(parent, "200", "200");
    PropPanelLayout.instance.doLayout(parent, null);

    parent.getVerticalScrollbar().setValue(1);
    ScrollLayout.instance.doContraction(parent);
    assertEquals(-1, panel.getY());

    parent.getHorizontalScrollbar().setValue(2);
    ScrollLayout.instance.doContraction(parent);
    assertEquals(-2, panel.getX());
  }

  private PropPanel addChildWithSize(ParentPanelBase parent, String width, String height)
  {
    PropPanel panel = new PropPanel(new FakePropProxy());
    panel.getStyle().setWidth(width);
    panel.getStyle().setHeight(height);
    parent.add(panel);
    return panel;
  }
}
