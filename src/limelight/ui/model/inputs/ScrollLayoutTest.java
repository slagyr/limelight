//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import junit.framework.TestCase;
import limelight.ui.api.MockProp;
import limelight.ui.model.*;

public class ScrollLayoutTest extends TestCase
{
  private PropPanel parent;

  public void setUp() throws Exception
  {
    ScenePanel root = new ScenePanel(new MockProp());
    root.setFrame(new MockPropFrame());
    parent = new PropPanel(new MockProp());
    root.add(parent);
    parent.getStyle().setWidth("100");
    parent.getStyle().setHeight("100");
  }

  public void testScrollContentThatIsntAlignedTopLeft() throws Exception
  {
    parent.getStyle().setScrollbars("on");
    parent.getStyle().setAlignment("center");
    PropPanel panel = addChildWithSize(parent, "200", "200");
    PropPanelLayout.instance.doLayout(parent);

    parent.getVerticalScrollbar().setValue(1);
    ScrollLayout verticalLayout = new ScrollLayout(parent.getVerticalScrollbar());
    verticalLayout.doLayout(parent);
    assertEquals(-1, panel.getY());
                                                                                            
    parent.getHorizontalScrollbar().setValue(2);
    ScrollLayout horizontalLayout = new ScrollLayout(parent.getHorizontalScrollbar());
    horizontalLayout.doLayout(parent);
    assertEquals(-2, panel.getX());
  }

  private PropPanel addChildWithSize(ParentPanelBase parent, String width, String height)
  {
    PropPanel panel = new PropPanel(new MockProp());
    panel.getStyle().setWidth(width);
    panel.getStyle().setHeight(height);
    parent.add(panel);
    return panel;
  }
}
