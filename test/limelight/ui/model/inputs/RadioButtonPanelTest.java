//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model.inputs;

import limelight.ui.RadioButtonGroup;
import limelight.ui.events.panel.ButtonPushedEvent;
import limelight.ui.events.panel.ValueChangedEvent;
import limelight.ui.model.FakeScene;
import limelight.ui.model.PropPanel;
import limelight.model.api.FakePropProxy;
import limelight.ui.model.ScenePanel;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class RadioButtonPanelTest
{
  private RadioButtonPanel panel;
  private PropPanel parent;
  private FakeScene root;

  @Before
  public void setUp() throws Exception
  {
    panel = new RadioButtonPanel();
    parent = new PropPanel(new FakePropProxy());
    parent.add(panel);
    root = new FakeScene();
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
  public void settingTextIsaDirtyJob() throws Exception
  {
    assertEquals(0, root.dirtyRegions.size());

    panel.setText("on");

    assertEquals(true, root.dirtyRegions.contains(panel.getBounds()));
  }

  @Test
  public void settingParentSterilizesParent() throws Exception
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
  public void pushSelectsButton() throws Exception
  {
    assertEquals(false, panel.isSelected());

    new ButtonPushedEvent().dispatch(panel);

    assertEquals(true, panel.isSelected());
  }

  @Test
  public void consumedPushDoesNothing() throws Exception
  {
    assertEquals(false, panel.isSelected());

    new ButtonPushedEvent().consumed().dispatch(panel);

    assertEquals(false, panel.isSelected());
  }

  @Test
  public void valueChangedEventGetsThrownWhenChangingTheValue() throws Exception
  {
    final MockEventAction action = new MockEventAction();
    panel.getEventHandler().add(ValueChangedEvent.class, action);

    assertEquals(false, panel.isSelected());
    panel.setSelected(false);
    assertEquals(false, action.invoked);

    panel.setSelected(true);
    assertEquals(true, action.invoked);
  }

  @Test
  public void multipleRadioButtonsInTheSameGroup() throws Exception
  {
    RadioButtonPanel radio1 = new RadioButtonPanel();
    RadioButtonPanel radio2 = new RadioButtonPanel();
    RadioButtonPanel radio3 = new RadioButtonPanel();
    RadioButtonGroup group = new RadioButtonGroup();
    group.add(radio1);
    group.add(radio2);
    group.add(radio3);

    radio1.setSelected(true);
    checkSelectedRadioButton(group, radio1, radio2, radio3);
    radio2.setSelected(true);
    checkSelectedRadioButton(group, radio2, radio1, radio3);
    radio3.setSelected(true);
    checkSelectedRadioButton(group, radio3, radio2, radio1);
  }

  private void checkSelectedRadioButton(RadioButtonGroup group, RadioButtonPanel expected, RadioButtonPanel... others)
  {
    assertEquals(expected, group.getSelection());
    assertEquals(true, expected.isSelected());
    for(RadioButtonPanel radio : others)
      assertEquals(false, radio.isSelected());
  }

  @Test
  public void settingTheGroupName() throws Exception
  {
    ScenePanel scene = new ScenePanel(new FakePropProxy("scene"));
    PropPanel parent1 = new PropPanel(new FakePropProxy("parent1"));
    RadioButtonPanel radio1 = new RadioButtonPanel();
    parent1.add(radio1);
    PropPanel parent2 = new PropPanel(new FakePropProxy("parent2"));
    RadioButtonPanel radio2 = new RadioButtonPanel();
    parent2.add(radio2);
    scene.add(parent1);
    scene.add(parent2);

    final RadioButtonGroup group = scene.getButtonGroups().get("test_group");
    assertEquals(0, group.getButtons().size());
    radio1.setGroup("test_group");
    assertEquals(1, group.getButtons().size());
    radio2.setGroup("test_group");
    assertEquals(2, group.getButtons().size());

    assertEquals(true, group.getButtons().contains(radio1));
    assertEquals(true, group.getButtons().contains(radio2));
  }

  @Test
  public void changingGroupName() throws Exception
  {
    ScenePanel scene = new ScenePanel(new FakePropProxy("scene"));
    PropPanel parent1 = new PropPanel(new FakePropProxy("parent1"));
    RadioButtonPanel radio1 = new RadioButtonPanel();
    parent1.add(radio1);
    scene.add(parent1);

    final RadioButtonGroup group1 = scene.getButtonGroups().get("group1");
    final RadioButtonGroup group2 = scene.getButtonGroups().get("group2");

    radio1.setGroup("group1");
    radio1.setGroup("group2");

    assertEquals(0, group1.getButtons().size());
    assertEquals(1, group2.getButtons().size());
  }

  @Test
  public void groupName() throws Exception
  {
    panel.setGroup("foo");
    assertEquals("foo", panel.getGroup());
  }
}
