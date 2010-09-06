package limelight.ui.events;

import limelight.ui.MockPanel;
import limelight.ui.Panel;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static junit.framework.Assert.assertEquals;

public class MouseWheelEventTest
{
  private Panel panel;
  private MouseWheelEvent event;

  @Before
  public void setUp() throws Exception
  {
    panel = new MockPanel();
    event = new MouseWheelEvent(panel, 123, new Point(1, 2), 0, MouseWheelEvent.UNIT_SCROLL, 3, 4);
  }
  
  @Test
  public void canGetAttributes() throws Exception
  {
    assertEquals(123, event.getModifiers());
    assertEquals(new Point(1, 2), event.getAbsoluteLocation());
    assertEquals(0, event.getClickCount());
    assertEquals(MouseWheelEvent.UNIT_SCROLL, event.getScrollType());
    assertEquals(3, event.getScrollAmount());
    assertEquals(4, event.getWheelRotation());
  }

  @Test
  public void isVerticalAndIsHorizontal() throws Exception
  {
    event.setModifiers(0);

    assertEquals(true, event.isVertical());
    assertEquals(false, event.isHorizontal());

    event.setModifiers(ModifiableEvent.SHIFT_MASK);

    assertEquals(false, event.isVertical());
    assertEquals(true, event.isHorizontal());
  }
  
  @Test
  public void isInheritable() throws Exception
  {
    assertEquals(true, event.isInheritable());
  }
}
