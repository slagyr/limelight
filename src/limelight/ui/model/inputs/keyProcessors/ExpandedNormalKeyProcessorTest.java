package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.events.KeyEvent;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExpandedNormalKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    setUpMultiLine();
    processor = ExpandedNormalKeyProcessor.instance;
    modifiers = 0;
  }

  @Test
  public void canProcessBackSpace()
  {
    processor.processKey(press(KeyEvent.KEY_BACK_SPACE), model);

    assertEquals(0, model.getCaretIndex());
    assertEquals("ere are four words", model.getText());
    if(!model.isSelectionActivated())
      assertEquals(0, model.getSelectionIndex());
  }

  @Test
  public void canProcessRightArrow()
  {
    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertEquals(2, model.getCaretIndex());
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
  public void canProcessUpArrowAndDoNothingWhenAllTheWayUp()
  {
    model.setText("This is\nMulti lined.");
    model.setCaretIndex(3);

    processor.processKey(press(KeyEvent.KEY_UP), model);

    assertEquals(3, model.getCaretIndex());
    assertEquals(0, model.getSelectionIndex());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void canProcessUpArrowAndMoveUpALine()
  {
    model.setText("This is\nMulti lined.");
    model.setCaretIndex(11);

    processor.processKey(press(KeyEvent.KEY_UP), model);

    assertEquals(3, model.getCaretIndex());
    assertEquals(0, model.getSelectionIndex());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void canProcessDownArrowAndDoNothingWhenAtBottom()
  {
    model.setText("This is\nMulti lined.");
    model.setCaretIndex(11);

    processor.processKey(press(KeyEvent.KEY_DOWN), model);

    assertEquals(11, model.getCaretIndex());
    assertEquals(0, model.getSelectionIndex());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void canProcessDownArrowAndMoveDownALine()
  {
    model.setText("This is\nMulti lined.");
    model.setCaretIndex(3);

    processor.processKey(press(KeyEvent.KEY_DOWN), model);

    assertEquals(11, model.getCaretIndex());
    assertEquals(0, model.getSelectionIndex());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void canProcessDownArrowAndMoveDownALineToTheLastCharacterIfTheFirstLineRunsPastTheSecond()
  {
    model.setText("This is a longer\nMulti lined.");
    model.setCaretIndex(15);

    processor.processKey(press(KeyEvent.KEY_DOWN), model);

    assertEquals(29, model.getCaretIndex());
    assertEquals(0, model.getSelectionIndex());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void shouldGoToTheEndOfPreviousLineEvenIfItEndsWithNewline() throws Exception
  {
    model.setText("Some more text\nand some more");
    model.setCaretIndex(28);

    processor.processKey(press(KeyEvent.KEY_UP), model);

    assertEquals(13, model.getCaretIndex());
    assertEquals(0, model.getSelectionIndex());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void shouldGoToTheEndOfNextLineEvenIfItEndsWithNewline() throws Exception
  {
    model.setText("blah\nasdf\nasdf");
    model.setCaretIndex(4);

    processor.processKey(press(KeyEvent.KEY_DOWN), model);

    assertEquals(9, model.getCaretIndex());
    assertEquals(0, model.getSelectionIndex());
    assertEquals(false, model.isSelectionActivated());
  }
}
