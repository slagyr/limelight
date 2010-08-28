//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.model.PropPanel;
import limelight.ui.api.MockProp;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ComboBoxPanelTest
{
  private ComboBoxPanel panel;
  private PropPanel parent;

  @Before
  public void setUp() throws Exception
  {
    panel = new ComboBoxPanel();
    parent = new PropPanel(new MockProp());
    parent.add(panel);
  }

  @Test
  public void canBeBuffered() throws Exception
  {
    assertEquals(false, panel.canBeBuffered());
  }

//  public void testSettingParentSetsTextAccessor() throws Exception
//  {
//    panel.addItem("foo");
//    panel.addItem("blah");
//    parent.setText("blah");
//    assertEquals("blah", panel.getSelectedItem().toString());
//  }

  @Test
  public void settingParentSteralizesParent() throws Exception
  {
    assertEquals(true, parent.isSterilized());
  }
//
//  public void testMouseClickedEventsGetPassedToParent() throws Exception
//  {
//    MockProp prop = (MockProp)parent.getProp();
//    ItemEvent event = new ItemEvent(panel, 1, "blah", 0);
//    panel.getComboBox().getItemListeners()[0].itemStateChanged(event);
//
//    assertNotNull(prop.changedValue);
//    assertEquals(panel.getComponent(), ((AWTEvent)prop.changedValue).getSource());
//  }
//
//  public void testMouseClickedWontFireOnSelect() throws Exception
//  {
//    MockProp prop = (MockProp)parent.getProp();
//    ItemEvent event = new ItemEvent(panel.getComboBox(), 1, "blah", 2);
//    panel.getComboBox().getItemListeners()[0].itemStateChanged(event);
//
//    assertNull(prop.changedValue);
//  }
//
//  public void testMouseClickedWillFireOnDeselect() throws Exception
//  {
//    MockProp prop = (MockProp)parent.getProp();
//    ItemEvent event = new ItemEvent(panel.getComboBox(), 1, "blah", 1);
//    panel.getComboBox().getItemListeners()[0].itemStateChanged(event);
//
//    assertNotNull(prop.changedValue);
//  }
}
