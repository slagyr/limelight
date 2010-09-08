package limelight.ui.events;

import limelight.ui.MockPanel;
import limelight.ui.Panel;
import org.junit.Before;
import org.junit.Test;

import java.awt.Point;

import static junit.framework.Assert.assertEquals;

public class MouseEventTest
{
  private MockPanel panel;
  private MouseEvent event;
  private Point location;

  private static class TestableMouseEvent extends MouseEvent
  {
    public TestableMouseEvent(Panel panel, int modifiers, Point location, int clickCount)
    {
      super(panel, modifiers, location, clickCount);
    }
  }

  @Before
  public void setUp() throws Exception
  {
    panel = new MockPanel();
    location = new Point(123, 456);
    event = new TestableMouseEvent(panel, 321, location, 1);
  }
  
  @Test
  public void constructorFields() throws Exception
  {
    assertEquals(321, event.getModifiers());
    assertEquals(location, event.getAbsoluteLocation());
    assertEquals(1, event.getClickCount());
  }
  
  @Test
  public void locationIsRelativeToPanel() throws Exception
  {
    panel.setLocation(23, 56);

    Point relativeLocation = event.getLocation();

    assertEquals(100, relativeLocation.x);
    assertEquals(400, relativeLocation.y);
  }
  
  @Test
  public void locationIsRelativeToChangedPanel() throws Exception
  {
    panel.setLocation(23, 56);
    assertEquals(100, event.getLocation().x);
    assertEquals(400, event.getLocation().y);

    MockPanel recipient = new MockPanel();
    recipient.setLocation(100, 400);

    event.setRecipient(recipient);

    assertEquals(23, event.getLocation().x);
    assertEquals(56, event.getLocation().y);
  }

  @Test
  public void mouseModifiersOff() throws Exception
  {
    event.setModifiers(0);

    assertEquals(false, event.isButton1());
    assertEquals(false, event.isButton2());
    assertEquals(false, event.isButton3());
  }

  @Test
  public void mouseModifiersOn() throws Exception
  {
    final int all = MouseEvent.BUTTON1_MASK | MouseEvent.BUTTON2_MASK | MouseEvent.BUTTON3_MASK;
    event.setModifiers(all);

    assertEquals(true, event.isButton1());
    assertEquals(true, event.isButton2());
    assertEquals(true, event.isButton3());
  }

  @Test
  public void inhertiableDefaultsToTrue() throws Exception
  {
    assertEquals(true, event.isInheritable());
  }
}
