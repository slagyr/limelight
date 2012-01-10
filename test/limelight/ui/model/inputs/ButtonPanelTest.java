//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model.inputs;

import limelight.model.api.FakePropProxy;
import limelight.ui.events.panel.MousePressedEvent;
import limelight.ui.events.panel.MouseReleasedEvent;
import limelight.ui.events.panel.ValueChangedEvent;
import limelight.ui.model.FakeScene;
import limelight.ui.model.PropPanel;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;

public class ButtonPanelTest
{
  private ButtonPanel panel;
  private PropPanel parent;
  private FakeScene root;

  @Before
  public void setUp() throws Exception
  {
    panel = new ButtonPanel();
    parent = new PropPanel(new FakePropProxy());
    parent.add(panel);
    root = new FakeScene();
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
    assertEquals("center", panel.getStyle().getHorizontalAlignment());
    assertEquals("center", panel.getStyle().getVerticalAlignment());
    assertEquals("Arial", panel.getStyle().getFontFace());
    assertEquals("bold", panel.getStyle().getFontStyle());
    assertEquals("12", panel.getStyle().getFontSize());
    assertEquals("#000000ff", panel.getStyle().getTextColor());
  }

  @Test
  public void propPainterReset() throws Exception
  {
    assertSame(ButtonPanel.BottonPropPainter.instance, parent.getPainter());
  }

  @Test
  public void settingTextIsaDirtyJob() throws Exception
  {
    assertEquals(0, root.dirtyRegions.size());

    panel.setText("New Text");

    assertEquals(true, root.dirtyRegions.contains(panel.getBounds()));
  }

  @Test
  public void pressingMouse() throws Exception
  {
    assertEquals(0, root.dirtyRegions.size());

    new MousePressedEvent(0, null, 0).dispatch(panel);

    assertEquals(1, root.dirtyRegions.size());
    assertEquals(panel.getBounds(), root.dirtyRegions.get(0));
  }

  @Test
  public void releasingMouse() throws Exception
  {
    assertEquals(0, root.dirtyRegions.size());

    new MouseReleasedEvent(0, null, 0).dispatch(panel);

    assertEquals(1, root.dirtyRegions.size());
    assertEquals(panel.getBounds(), root.dirtyRegions.get(0));
  }

  @Test
  public void valuChangedEventInvokedWhenChangingText() throws Exception
  {
    panel.setText("foo");
    final MockEventAction action = new MockEventAction();
    panel.getEventHandler().add(ValueChangedEvent.class, action);

    panel.setText("foo");
    assertEquals(false, action.invoked);

    panel.setText("bar");
    assertEquals(true, action.invoked);
  }
}
