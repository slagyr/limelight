package limelight.ui.model;

import limelight.ui.events.KeyPressedEvent;
import limelight.ui.events.KeyEvent;
import limelight.ui.model.inputs.TestableInputPanel;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class InputTabbingActionTest
{
  private InputTabbingAction action;
  private MockRootPanel root;
  private TestableInputPanel input1;
  private TestableInputPanel input2;
  private TestableInputPanel input3;

  @Before
  public void setUp() throws Exception
  {
    action = InputTabbingAction.instance;
    root = new MockRootPanel();
    input1 = new TestableInputPanel();
    input2 = new TestableInputPanel();
    input3 = new TestableInputPanel();

    root.add(input1);
    root.add(input2);
    root.add(input3);
  }
  
  @Test
  public void onlyTabsAreHandled() throws Exception
  {
    action.invoke(new KeyPressedEvent(root, 0, KeyEvent.KEY_A, 0));

    assertEquals(root, root.getKeyListener().getFocusedPanel());
  }
  
  @Test
  public void tabbingForward() throws Exception
  {
    action.invoke(new KeyPressedEvent(root, 0, KeyEvent.KEY_TAB, 0));
    assertEquals(input1, root.getKeyListener().getFocusedPanel());

    action.invoke(new KeyPressedEvent(input1, 0, KeyEvent.KEY_TAB, 0));
    assertEquals(input2, root.getKeyListener().getFocusedPanel());

    action.invoke(new KeyPressedEvent(input2, 0, KeyEvent.KEY_TAB, 0));
    assertEquals(input3, root.getKeyListener().getFocusedPanel());

    action.invoke(new KeyPressedEvent(input3, 0, KeyEvent.KEY_TAB, 0));
    assertEquals(input1, root.getKeyListener().getFocusedPanel());
  }
  
  @Test
  public void tabbingBackwards() throws Exception
  {
    action.invoke(new KeyPressedEvent(root, KeyEvent.SHIFT_MASK, KeyEvent.KEY_TAB, 0));
    assertEquals(input3, root.getKeyListener().getFocusedPanel());

    action.invoke(new KeyPressedEvent(input3, KeyEvent.SHIFT_MASK, KeyEvent.KEY_TAB, 0));
    assertEquals(input2, root.getKeyListener().getFocusedPanel());

    action.invoke(new KeyPressedEvent(input2, KeyEvent.SHIFT_MASK, KeyEvent.KEY_TAB, 0));
    assertEquals(input1, root.getKeyListener().getFocusedPanel());

    action.invoke(new KeyPressedEvent(input1, KeyEvent.SHIFT_MASK, KeyEvent.KEY_TAB, 0));
    assertEquals(input3, root.getKeyListener().getFocusedPanel());
  }
  
  @Test
  public void theEventIsConsumed() throws Exception
  {
    final KeyPressedEvent event = new KeyPressedEvent(root, 0, KeyEvent.KEY_TAB, 0);

    action.invoke(event);

    assertEquals(true, event.isConsumed());
  }
}
