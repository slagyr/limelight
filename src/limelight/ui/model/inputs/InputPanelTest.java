//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.events.panel.*;
import limelight.ui.model.FakeScene;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class InputPanelTest
{
  private FakeScene root;
  private InputPanel panel;

  @Before
  public void setUp() throws Exception
  {
    panel = new TestableInputPanel();
    root = new FakeScene();
    root.add(panel);
  }
  
  @Test
  public void canBeFocused() throws Exception
  {
    assertEquals(0, root.dirtyRegions.size());

    new FocusGainedEvent().dispatch(panel);

    assertEquals(2, root.dirtyRegions.size());
    assertEquals(panel.getBounds(), root.dirtyRegions.get(0));
    assertEquals(root.getBounds(), root.dirtyRegions.get(0));
  }
  
  @Test
  public void canBeUnfocused() throws Exception
  {
    new FocusGainedEvent().dispatch(panel);
    root.dirtyRegions.clear();
    new FocusLostEvent().dispatch(panel);

    assertEquals(2, root.dirtyRegions.size());
    assertEquals(panel.getBounds(), root.dirtyRegions.get(0));
    assertEquals(root.getBounds(), root.dirtyRegions.get(0));
  }

  @Test
  public void delegatesEventsToParent() throws Exception
  {
    final PanelEventHandler handler = panel.getEventHandler();
    assertEquals(true, handler.getActions(MousePressedEvent.class).contains(PropogateToParentAction.instance));
    assertEquals(true, handler.getActions(MouseReleasedEvent.class).contains(PropogateToParentAction.instance));
    assertEquals(true, handler.getActions(MouseClickedEvent.class).contains(PropogateToParentAction.instance));
    assertEquals(true, handler.getActions(MouseMovedEvent.class).contains(PropogateToParentAction.instance));
    assertEquals(true, handler.getActions(MouseDraggedEvent.class).contains(PropogateToParentAction.instance));
    assertEquals(true, handler.getActions(MouseEnteredEvent.class).contains(PropogateToParentAction.instance));
    assertEquals(true, handler.getActions(MouseExitedEvent.class).contains(PropogateToParentAction.instance));
    assertEquals(true, handler.getActions(MouseWheelEvent.class).contains(PropogateToParentAction.instance));
    assertEquals(true, handler.getActions(KeyPressedEvent.class).contains(PropogateToParentAction.instance));
    assertEquals(true, handler.getActions(KeyReleasedEvent.class).contains(PropogateToParentAction.instance));
    assertEquals(true, handler.getActions(CharTypedEvent.class).contains(PropogateToParentAction.instance));
    assertEquals(true, handler.getActions(FocusGainedEvent.class).contains(PropogateToParentAction.instance));
    assertEquals(true, handler.getActions(FocusLostEvent.class).contains(PropogateToParentAction.instance));
    assertEquals(true, handler.getActions(ValueChangedEvent.class).contains(PropogateToParentAction.instance));
  }
}
