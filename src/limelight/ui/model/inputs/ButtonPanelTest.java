//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.events.MousePressedEvent;
import limelight.ui.events.MouseReleasedEvent;
import limelight.ui.events.ValueChangedEvent;
import limelight.ui.model.MockRootPanel;
import limelight.ui.model.PropPanel;
import limelight.ui.api.MockProp;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;

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
  public void pressingMouse() throws Exception
  {
    assertEquals(0, root.dirtyRegions.size());

    new MousePressedEvent(panel, 0, null, 0).dispatch(panel);

    assertEquals(1, root.dirtyRegions.size());
    assertEquals(panel.getBounds(), root.dirtyRegions.get(0));
  }

  @Test
  public void releasingMouse() throws Exception
  {
    assertEquals(0, root.dirtyRegions.size());

    new MouseReleasedEvent(panel, 0, null, 0).dispatch(panel);

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
