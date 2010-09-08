package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.events.KeyEvent;
import limelight.ui.text.TextLocation;
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

    assertEquals(TextLocation.origin, model.getCaretLocation());
    assertEquals("ere are four words", model.getText());
  }

  @Test
  public void canProcessRightArrow()
  {
    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertEquals(TextLocation.at(0, 2), model.getCaretLocation());
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
  public void canProcessUpArrowAndDoNothingWhenAllTheWayUp()
  {
    model.setText("This is\nMulti lined.");
    model.setCaretLocation(TextLocation.at(0, 3));

    processor.processKey(press(KeyEvent.KEY_UP), model);

    assertEquals(TextLocation.at(0, 3), model.getCaretLocation());
    assertEquals(TextLocation.origin, model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void canProcessUpArrowAndMoveUpALine()
  {
    model.setText("This is\nMulti lined.");
    model.setCaretLocation(TextLocation.at(1, 3));

    processor.processKey(press(KeyEvent.KEY_UP), model);

    assertEquals(TextLocation.at(0, 3), model.getCaretLocation());
    assertEquals(TextLocation.origin, model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void canProcessDownArrowAndDoNothingWhenAtBottom()
  {
    model.setText("This is\nMulti lined.");
    model.setCaretLocation(TextLocation.at(1, 3));

    processor.processKey(press(KeyEvent.KEY_DOWN), model);

    assertEquals(TextLocation.at(1, 3), model.getCaretLocation());
    assertEquals(TextLocation.origin, model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void canProcessDownArrowAndMoveDownALine()
  {
    model.setText("This is\nMulti lined.");
    model.setCaretLocation(TextLocation.at(0, 3));

    processor.processKey(press(KeyEvent.KEY_DOWN), model);

    assertEquals(TextLocation.at(1, 3), model.getCaretLocation());
    assertEquals(TextLocation.origin, model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void canProcessDownArrowAndMoveDownALineToTheLastCharacterIfTheFirstLineRunsPastTheSecond()
  {
    model.setText("This is a longer\nMulti lined.");
    model.setCaretLocation(TextLocation.at(0, 15));

    processor.processKey(press(KeyEvent.KEY_DOWN), model);

    assertEquals(model.getEndLocation(), model.getCaretLocation());
    assertEquals(TextLocation.origin, model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void shouldGoToTheEndOfPreviousLineEvenIfItEndsWithNewline() throws Exception
  {
    model.setText("Some more text\nand some more");
    model.setCaretLocation(model.getEndLocation());

    processor.processKey(press(KeyEvent.KEY_UP), model);

    assertEquals(TextLocation.at(0, 13), model.getCaretLocation());
    assertEquals(TextLocation.origin, model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void shouldGoToTheEndOfNextLineEvenIfItEndsWithNewline() throws Exception
  {
    model.setText("blah\nasdf\nasdf");
    model.setCaretLocation(TextLocation.at(0, 4));

    processor.processKey(press(KeyEvent.KEY_DOWN), model);

    assertEquals(TextLocation.at(1, 4), model.getCaretLocation());
    assertEquals(TextLocation.origin, model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }
}
