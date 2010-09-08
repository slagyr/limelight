//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.RadioButtonGroup;
import limelight.ui.events.ButtonPushedEvent;
import limelight.ui.events.MouseClickedEvent;
import limelight.ui.model.PropPanel;
import limelight.ui.api.MockProp;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class RadioButtonPanelTest
{
  private RadioButtonPanel panel;
  private PropPanel parent;
  private RadioButtonGroup group;

  @Before
  public void setUp() throws Exception
  {
    panel = new RadioButtonPanel();
    parent = new PropPanel(new MockProp());
    parent.add(panel);

    group = new RadioButtonGroup();
    group.add(panel);
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
    assertEquals("21", panel.getStyle().getWidth());
    assertEquals("21", panel.getStyle().getHeight());
  }

  @Test
  public void pushSelectesButton() throws Exception
  {
    assertEquals(false, panel.isSelected());

    new ButtonPushedEvent(panel).dispatch(panel);

    assertEquals(true, panel.isSelected());
  }
}
