package limelight.ui.model.inputs.keyProcessors;


import org.junit.Before;
import org.junit.Test;

import java.awt.event.KeyEvent;

public class CmdKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    setUpSingleLine();
    processor = CmdKeyProcessor.instance;
    modifier = 4;
  }

  @Test
  public void canSelectAll()
  {
    model.setText("Bob");
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_A);

    processor.processKey(mockEvent, model);

    assertSelection(3, 0, true);
  }

  @Test
  public void canPasteAtCursor()
  {
    model.setText("Bob");
    model.copyText(" Dole");
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_V);

    processor.processKey(mockEvent, model);

    assertTextState(model.getText().length(), 0, "Bob Dole");
  }

  @Test
  public void canProcessRightArrow()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_RIGHT);

    processor.processKey(mockEvent, model);

    assertSelection(model.getText().length(), 0, false);
  }

  @Test
  public void canProcessLeftArrow()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_LEFT);

    processor.processKey(mockEvent, model);

    assertSelection(0, 0, false);
  }

  @Test
  public void canProcessUpArrow()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_UP);

    processor.processKey(mockEvent, model);

    assertSelection(0, 0, false);
  }

  @Test
  public void canProcessDownArrow()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_DOWN);

    processor.processKey(mockEvent, model);

    assertSelection(model.getText().length(), 0, false);
  }
}
