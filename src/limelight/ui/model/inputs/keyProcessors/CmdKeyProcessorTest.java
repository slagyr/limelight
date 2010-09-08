package limelight.ui.model.inputs.keyProcessors;


import limelight.ui.events.KeyEvent;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CmdKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    setUpSingleLine();
    processor = CmdKeyProcessor.instance;
    modifiers = 4;
  }

  @Test
  public void canSelectAll()
  {
    model.setText("Bob");

    processor.processKey(press(KeyEvent.KEY_A), model);

    assertEquals(3, model.getCaretIndex());
    assertEquals(0, model.getSelectionIndex());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void canPasteAtCursor()
  {
    model.setText("Bob");
    model.copyText(" Dole");

    processor.processKey(press(KeyEvent.KEY_V), model);

    assertEquals(model.getText().length(), model.getCaretIndex());
    assertEquals("Bob Dole", model.getText());
    if(!model.isSelectionActivated())
      assertEquals(0, model.getSelectionIndex());
  }

  @Test
  public void canProcessRightArrow()
  {
    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertEquals(model.getText().length(), model.getCaretIndex());
    assertEquals(0, model.getSelectionIndex());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void canProcessLeftArrow()
  {
    processor.processKey(press(KeyEvent.KEY_LEFT), model);

    assertEquals(0, model.getCaretIndex());
    assertEquals(0, model.getSelectionIndex());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void canProcessUpArrow()
  {
    processor.processKey(press(KeyEvent.KEY_UP), model);

    assertEquals(0, model.getCaretIndex());
    assertEquals(0, model.getSelectionIndex());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void canProcessDownArrow()
  {
    processor.processKey(press(KeyEvent.KEY_DOWN), model);

    assertEquals(model.getText().length(), model.getCaretIndex());
    assertEquals(0, model.getSelectionIndex());
    assertEquals(false, model.isSelectionActivated());
  }
}
