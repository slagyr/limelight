package limelight.ui.model.inputs.keyProcessors;


import limelight.ui.events.KeyEvent;
import limelight.ui.text.TextLocation;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

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

    assertEquals(TextLocation.at(0, 3), model.getCaretLocation());
    assertEquals(TextLocation.origin, model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void canPasteAtCursor()
  {
    model.setText("Bob");
    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(" Dole"), model);

    processor.processKey(press(KeyEvent.KEY_V), model);

    assertEquals(model.getEndLocation(), model.getCaretLocation());
    assertEquals("Bob Dole", model.getText());
  }

  @Test
  public void canProcessRightArrow()
  {
    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertEquals(model.getEndLocation(), model.getCaretLocation());
    assertEquals(TextLocation.origin, model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void canProcessLeftArrow()
  {
    processor.processKey(press(KeyEvent.KEY_LEFT), model);

    assertEquals(TextLocation.origin, model.getCaretLocation());
    assertEquals(TextLocation.origin, model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void canProcessUpArrow()
  {
    processor.processKey(press(KeyEvent.KEY_UP), model);

    assertEquals(TextLocation.origin, model.getCaretLocation());
    assertEquals(TextLocation.origin, model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void canProcessDownArrow()
  {
    processor.processKey(press(KeyEvent.KEY_DOWN), model);

    assertEquals(model.getEndLocation(), model.getCaretLocation());
    assertEquals(TextLocation.origin, model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }
}
