package limelight.ui.model.inputs.keyProcessors;


import limelight.ui.events.KeyEvent;
import limelight.ui.text.TextLocation;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SelectionOnCmdKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    setUpSingleLine();
    model.startSelection(TextLocation.at(0, 4));
    processor = SelectionOnCmdKeyProcessor.instance;
    modifiers = 4;
  }

  @Test
  public void canSelectAll()
  {
    processor.processKey(press(KeyEvent.KEY_A), model);

    assertEquals(model.getEndLocation(), model.getCaretLocation());
    assertEquals(TextLocation.origin, model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
  }

  @Test
  public void canPasteAtCursor()
  {
    model.copyText("oot");

    processor.processKey(press(KeyEvent.KEY_V), model);

    assertEquals(TextLocation.at(0, 4), model.getCaretLocation());
    assertEquals("Hoot are four words", model.getText());
    assertEquals(false, model.isSelectionActivated());
    assertEquals(TextLocation.at(0, 4), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 1), model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void canCopySelectedText()
  {
    processor.processKey(press(KeyEvent.KEY_C), model);

    assertEquals(TextLocation.at(0, 1), model.getCaretLocation());
    assertEquals("Here are four words", model.getText());
    assertEquals(true, model.isSelectionActivated());
    assertEquals(TextLocation.at(0, 1), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 4), model.getSelectionLocation());
    assertEquals(true, model.isSelectionActivated());
    assertEquals("ere", model.getClipboardContents());
  }

  @Test
  public void canCutSelectedText()
  {
    processor.processKey(press(KeyEvent.KEY_X), model);

    assertEquals(TextLocation.at(0, 1), model.getCaretLocation());
    assertEquals("H are four words", model.getText());
    assertEquals(false, model.isSelectionActivated());
    assertEquals(TextLocation.at(0, 1), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 1), model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
    assertEquals("ere", model.getClipboardContents());
  }

  @Test
  public void canProcessRightArrowAndJumpToRightEdgeAndEndSelection()
  {
    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertEquals(model.getEndLocation(), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 4), model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void canProcessLeftArrowAndJumpToLeftEdgeAndEndSelection()
  {
    processor.processKey(press(KeyEvent.KEY_LEFT), model);

    assertEquals(TextLocation.origin, model.getCaretLocation());
    assertEquals(TextLocation.at(0, 4), model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }

   @Test
  public void canProcessUpArrowAndJumpToTheStart()
  {
    processor.processKey(press(KeyEvent.KEY_UP), model);

    assertEquals(TextLocation.origin, model.getCaretLocation());
    assertEquals(TextLocation.at(0, 4), model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void canProcessDownArrowAndJumpToTheEnd()
  {
    processor.processKey(press(KeyEvent.KEY_DOWN), model);

    assertEquals(model.getEndLocation(), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 4), model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }

}
