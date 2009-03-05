//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import junit.framework.TestCase;
import limelight.ui.model.PropPanel;
import limelight.ui.api.MockProp;

import java.awt.event.MouseEvent;
import java.awt.event.ItemEvent;
import java.awt.*;

public class ComboBoxPanelTest extends TestCase
{
  private ComboBoxPanel panel;
  private PropPanel parent;

  public void setUp() throws Exception
  {
    panel = new ComboBoxPanel();
    parent = new PropPanel(new MockProp());
    parent.add(panel);
  }

  public void testCanBeBuffered() throws Exception
  {
    assertEquals(false, panel.canBeBuffered());
  }

  public void testSettingParentSetsTextAccessor() throws Exception
  {
    panel.getComboBox().addItem("foo");
    panel.getComboBox().addItem("blah");
    parent.setText("blah");
    assertEquals("blah", panel.getComboBox().getSelectedItem().toString());
  }

  public void testSettingParentSteralizesParent() throws Exception
  {
    assertEquals(true, parent.isSterilized());
  }

  public void testMouseClickedEventsGetPassedToParent() throws Exception
  {
    MockProp prop = (MockProp)parent.getProp();
    ItemEvent event = new ItemEvent(panel.getComboBox(), 1, "blah", 0);
    panel.getComboBox().getItemListeners()[0].itemStateChanged(event);

    assertNotNull(prop.changedValue);
    assertEquals(panel.getComponent(), ((AWTEvent)prop.changedValue).getSource());
  }

  public void testMouseClickedWontFireOnSelect() throws Exception
  {
    MockProp prop = (MockProp)parent.getProp();
    ItemEvent event = new ItemEvent(panel.getComboBox(), 1, "blah", 2);
    panel.getComboBox().getItemListeners()[0].itemStateChanged(event);

    assertNull(prop.changedValue);
  }
    
  public void testMouseClickedWillFireOnDeselect() throws Exception
  {
    MockProp prop = (MockProp)parent.getProp();
    ItemEvent event = new ItemEvent(panel.getComboBox(), 1, "blah", 1);
    panel.getComboBox().getItemListeners()[0].itemStateChanged(event);

    assertNotNull(prop.changedValue);
  }
}
