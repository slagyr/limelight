//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.events.MousePressedEvent;
import limelight.ui.events.MouseReleasedEvent;
import limelight.ui.model.MockRootPanel;
import limelight.ui.model.PropPanel;
import limelight.ui.api.MockProp;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ButtonPanelTest
{
  private ButtonPanel panel;
  private PropPanel parent;
  private MockRootPanel root;

  @Before
  public void setUp() throws Exception
  {
    panel = new ButtonPanel();
    parent = new PropPanel(new MockProp());
    parent.add(panel);
    root = new MockRootPanel();
    root.add(parent);
  }

  @Test
  public void canBeBuffered() throws Exception
  {
    assertEquals(false, panel.canBeBuffered());
  }

  @Test
  public void settingParentSetsTextAccessor() throws Exception
  {
    parent.setText("blah");
    assertEquals("blah", panel.getText());
  }

  @Test
  public void settingParentSteralizesParent() throws Exception
  {
    assertEquals(true, parent.isSterilized());
  }

  @Test
  public void shouldDefaultStyles() throws Exception
  {
    assertEquals("128", panel.getStyle().getWidth());
    assertEquals("27", panel.getStyle().getHeight());
  }
  
  @Test
  public void pressingMouse() throws Exception
  {
    assertEquals(0, root.dirtyRegions.size());

    panel.getEventHandler().dispatch(new MousePressedEvent(panel, 0, null, 0));

    assertEquals(1, root.dirtyRegions.size());
    assertEquals(panel.getBoundingBox(), root.dirtyRegions.get(0));
  }
  
  @Test
  public void releasingMouse() throws Exception
  {
    assertEquals(0, root.dirtyRegions.size());

    panel.getEventHandler().dispatch(new MouseReleasedEvent(panel, 0, null, 0));

    assertEquals(1, root.dirtyRegions.size());
    assertEquals(panel.getBoundingBox(), root.dirtyRegions.get(0));
  }
}
