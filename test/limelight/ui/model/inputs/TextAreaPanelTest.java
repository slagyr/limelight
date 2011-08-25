//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.model.api.FakePropProxy;
import limelight.ui.MockGraphics;
import limelight.ui.events.panel.*;
import limelight.ui.model.FakeScene;
import limelight.ui.model.MockStage;
import limelight.ui.model.PropPanel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static junit.framework.Assert.assertEquals;

public class TextAreaPanelTest extends Assert
{
  TextAreaPanel panel;
  PropPanel parent;
  MockGraphics graphics;
  TextModel model;
  private FakeScene root;

  @Before
  public void setUp()
  {
    panel = new TextAreaPanel();
    parent = new PropPanel(new FakePropProxy());
    parent.add(panel);

    root = new FakeScene();
    root.add(parent);
    root.setStage(new MockStage());

    graphics = new MockGraphics();
    model = panel.getModel();
    model.setText("Some Text");
  }
  
  @Test
  public void shouldDefaultStyles() throws Exception
  {
    assertEquals("150", panel.getStyle().getWidth());
    assertEquals("75", panel.getStyle().getHeight());
    assertEquals("#ffffffff", panel.getStyle().getBackgroundColor());
  }

  @Test
  public void shouldHaveDefaultPadding()
  {
    assertEquals("2", panel.getStyle().getTopPadding());
    assertEquals("2", panel.getStyle().getRightPadding());
    assertEquals("2", panel.getStyle().getBottomPadding());
    assertEquals("2", panel.getStyle().getLeftPadding());
  }

  @Test
  public void shouldHaveDefaultBorders()
  {
    assertEquals("4", panel.getStyle().getTopBorderWidth());
    assertEquals("4", panel.getStyle().getRightBorderWidth());
    assertEquals("4", panel.getStyle().getBottomBorderWidth());
    assertEquals("4", panel.getStyle().getLeftBorderWidth());
  }
  
  @Test
  public void shouldHaveDefaultAlignment() throws Exception
  {
    assertEquals("top", panel.getStyle().getVerticalAlignment());
    assertEquals("left", panel.getStyle().getHorizontalAlignment());
  }
  
  @Test
  public void shouldHaveDefaultCursor() throws Exception
  {
    assertEquals("text", panel.getStyle().getCursor());
  }

  @Test
  public void typingTestOnSelectionWillReplaceTheSelection() throws Exception
  {
    panel.setText("blah");

//dispatching: MousePressedEvent: source=limelight.ui.model.inputs.TextAreaPanel@4d7777a1 recipient=limelight.ui.model.inputs.TextAreaPanel@4d7777a1 modifiers=16 absLocation=java.awt.Point[x=140,y=306] clickCount=1
//dispatching: FocusGainedEvent: source=limelight.ui.model.inputs.TextAreaPanel@4d7777a1 recipient=limelight.ui.model.inputs.TextAreaPanel@4d7777a1
//dispatching: MouseReleasedEvent: source=limelight.ui.model.inputs.TextAreaPanel@4d7777a1 recipient=limelight.ui.model.inputs.TextAreaPanel@4d7777a1 modifiers=16 absLocation=java.awt.Point[x=140,y=306] clickCount=1
//dispatching: MouseClickedEvent: source=limelight.ui.model.inputs.TextAreaPanel@4d7777a1 recipient=limelight.ui.model.inputs.TextAreaPanel@4d7777a1 modifiers=16 absLocation=java.awt.Point[x=140,y=306] clickCount=1
//dispatching: MousePressedEvent: source=limelight.ui.model.inputs.TextAreaPanel@4d7777a1 recipient=limelight.ui.model.inputs.TextAreaPanel@4d7777a1 modifiers=16 absLocation=java.awt.Point[x=140,y=306] clickCount=2
//dispatching: MouseReleasedEvent: source=limelight.ui.model.inputs.TextAreaPanel@4d7777a1 recipient=limelight.ui.model.inputs.TextAreaPanel@4d7777a1 modifiers=16 absLocation=java.awt.Point[x=140,y=306] clickCount=2
//dispatching: MouseClickedEvent: source=limelight.ui.model.inputs.TextAreaPanel@4d7777a1 recipient=limelight.ui.model.inputs.TextAreaPanel@4d7777a1 modifiers=16 absLocation=java.awt.Point[x=140,y=306] clickCount=2
//dispatching: KeyPressedEvent: source=limelight.ui.model.inputs.TextAreaPanel@584b62a7 recipient=limelight.ui.model.inputs.TextAreaPanel@584b62a7 modifiers=0 keyCode=65/41 location=1
//dispatching: CharTypedEvent: source=limelight.ui.model.inputs.TextAreaPanel@584b62a7 recipient=limelight.ui.model.inputs.TextAreaPanel@584b62a7 modifiers=0 char=97/a
//dispatching: KeyReleasedEvent: source=limelight.ui.model.inputs.TextAreaPanel@584b62a7 recipient=limelight.ui.model.inputs.TextAreaPanel@584b62a7 modifiers=0 keyCode=65/41 location=1
//dispatching: KeyPressedEvent: source=limelight.ui.model.inputs.TextAreaPanel@584b62a7 recipient=limelight.ui.model.inputs.TextAreaPanel@584b62a7 modifiers=4 keyCode=157/9d location=2

    new MousePressedEvent(16, new Point(5, 5), 1).dispatch(panel);
    new FocusGainedEvent().dispatch(panel);
    new MouseReleasedEvent(16, new Point(5, 5), 1).dispatch(panel);
    new MouseClickedEvent(16, new Point(5, 5), 1).dispatch(panel);
    new MousePressedEvent(16, new Point(5, 5), 2).dispatch(panel);
    new MouseReleasedEvent(16, new Point(5, 5), 2).dispatch(panel);
    new MouseClickedEvent(16, new Point(5, 5), 2).dispatch(panel);
    new KeyPressedEvent(0, KeyEvent.KEY_A, KeyEvent.LOCATION_STANDARD);
    new CharTypedEvent(0, 'a').dispatch(panel);
    new KeyReleasedEvent(0, KeyEvent.KEY_A, KeyEvent.LOCATION_STANDARD);

    assertEquals("a", panel.getText());
  }
}
