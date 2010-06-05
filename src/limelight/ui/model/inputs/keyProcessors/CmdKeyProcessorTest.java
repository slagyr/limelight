package limelight.ui.model.inputs.keyProcessors;


import org.junit.Before;
import org.junit.Test;

import java.awt.event.KeyEvent;

public class CmdKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    textBoxSetUp();
    processor = CmdKeyProcessor.instance;
    modifier = 4;
  }

  @Test
  public void canSelectAll()
  {
    modelInfo.setText("Bob");
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_A);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(3, 0, true);
  }

  @Test
  public void canPasteAtCursor()
  {
    modelInfo.setText("Bob");
    modelInfo.copyText(" Dole");
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_V);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertTextState(modelInfo.getText().length(), "Bob Dole");
  }

  @Test
  public void canProcessRightArrow()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_RIGHT);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(modelInfo.getText().length(), 0, false);
  }

  @Test
  public void canProcessLeftArrow()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_LEFT);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(0, 0, false);
  }

  @Test
  public void canProcessUpArrow()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_UP);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(0, 0, false);
  }

  @Test
  public void canProcessDownArrow()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_DOWN);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(modelInfo.getText().length(), 0, false);
  }
}
