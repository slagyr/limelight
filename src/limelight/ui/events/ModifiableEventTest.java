package limelight.ui.events;

import limelight.ui.MockPanel;
import limelight.ui.Panel;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ModifiableEventTest
{
  private Panel panel;
  private ModifiableEvent event;

  private static class TestableModifiableEvent extends ModifiableEvent
  {
    public TestableModifiableEvent(Panel panel, int modifiers)
    {
      super(panel, modifiers);
    }
  }

  @Before
  public void setUp()
  {
    panel = new MockPanel();
    event = new TestableModifiableEvent(panel, 0);
  }

  @Test
  public void allModifiersOff() throws Exception
  {
    assertEquals(false, event.isShiftDown());
    assertEquals(false, event.isControlDown());
    assertEquals(false, event.isCommandDown());
    assertEquals(false, event.isAltDown());
  }
  
  @Test
  public void allModifiersOn() throws Exception
  {
    int all = ModifiableEvent.SHIFT_MASK | ModifiableEvent.CONTROL_MASK | ModifiableEvent.COMMAND_MASK | ModifiableEvent.ALT_MASK;
    event.setModifiers(all);

    assertEquals(true, event.isShiftDown());
    assertEquals(true, event.isControlDown());
    assertEquals(true, event.isCommandDown());
    assertEquals(true, event.isAltDown());
  }


}
