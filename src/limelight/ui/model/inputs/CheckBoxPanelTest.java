//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.events.MouseClickedEvent;
import limelight.ui.model.MockRootPanel;
import limelight.ui.model.PropPanel;
import limelight.ui.api.MockProp;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class CheckBoxPanelTest
{
  private CheckBoxPanel panel;
  private PropPanel parent;
  private MockRootPanel root;

  @Before
  public void setUp() throws Exception
  {
    panel = new CheckBoxPanel();
    parent = new PropPanel(new MockProp());
    parent.add(panel);
    root = new MockRootPanel();
    root.add(parent);
  }
  
  @Test
  public void isButton() throws Exception
  {
    assertEquals(true, AbstractButtonPanel.class.isInstance(panel));
  }

  @Test
  public void canBeBuffered() throws Exception
  {
    assertEquals(false, panel.canBeBuffered());
  }

  @Test
  public void settingParentSetsTextAccessor() throws Exception
  {
    parent.setText("on");
    assertEquals("on", panel.getText());
  }

  @Test
  public void settingParentSteralizesParent() throws Exception
  {
    assertEquals(true, parent.isSterilized());
  }

  @Test
  public void shouldDefaultStyles() throws Exception
  {
    assertEquals("20", panel.getStyle().getWidth());
    assertEquals("20", panel.getStyle().getHeight());
  }

  @Test
  public void pushingSelectesCheckBox() throws Exception
  {
    assertEquals(false, panel.isSelected());

    panel.getEventHandler().dispatch(new MouseClickedEvent(panel, 0, null, 0));

    assertEquals(true, panel.isSelected());
  }

  
}
