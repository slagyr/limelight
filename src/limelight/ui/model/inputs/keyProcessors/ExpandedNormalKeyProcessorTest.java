package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.events.KeyEvent;
import org.junit.Before;
import org.junit.Test;

public class ExpandedNormalKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    setUpMultiLine();
    processor = ExpandedNormalKeyProcessor.instance;
    modifiers = 0;
  }

//  @Test
//  public void canProcessTheReturnKey()
//  {
//    mockEvent = new MockKeyEvent(modifiers, KeyEvent.KEY_ENTER, '\r');
//
//    processor.processKey(mockEvent, model);
//
//    assertTextState(2, 0, "H\rere are four words");
//  }
//
//  @Test
//  public void canProcessTheTabKey()
//  {
//    mockEvent = new MockKeyEvent(modifiers, KeyEvent.KEY_TAB, '\t');
//
//    processor.processKey(mockEvent, model);
//
//    assertTextState(2, 0, "H\tere are four words");
//  }
//
//  @Test
//  public void canProcessCharacters()
//  {
//    mockEvent = new MockKeyEvent(modifiers, KeyEvent.KEY_A, 'a');
//
//    processor.processKey(mockEvent, model);
//
//    assertTextState(2, 0, "Haere are four words");
//  }

  @Test
  public void canProcessBackSpace()
  {
    processor.processKey(press(KeyEvent.KEY_BACK_SPACE), model);

    assertTextState(0, 0, "ere are four words");
  }

  @Test
  public void canProcessRightArrow()
  {
    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertSelection(2, 0, false);
  }

  @Test
  public void canProcessLeftArrow()
  {
    processor.processKey(press(KeyEvent.KEY_LEFT), model);

    assertSelection(0, 0, false);
  }

  @Test
  public void canProcessUpArrowAndDoNothingWhenAllTheWayUp()
  {
    model.setText("This is\nMulti lined.");
    model.setCaretIndex(3);

    processor.processKey(press(KeyEvent.KEY_UP), model);

    assertSelection(3, 0, false);
  }

  @Test
  public void canProcessUpArrowAndMoveUpALine()
  {
    model.setText("This is\nMulti lined.");
    model.setCaretIndex(11);

    processor.processKey(press(KeyEvent.KEY_UP), model);

    assertSelection(3, 0, false);
  }

  @Test
  public void canProcessDownArrowAndDoNothingWhenAtBottom()
  {
    model.setText("This is\nMulti lined.");
    model.setCaretIndex(11);

    processor.processKey(press(KeyEvent.KEY_DOWN), model);

    assertSelection(11, 0, false);
  }

  @Test
  public void canProcessDownArrowAndMoveDownALine()
  {
    model.setText("This is\nMulti lined.");
    model.setCaretIndex(3);

    processor.processKey(press(KeyEvent.KEY_DOWN), model);

    assertSelection(11, 0, false);
  }

  @Test
  public void canProcessDownArrowAndMoveDownALineToTheLastCharacterIfTheFirstLineRunsPastTheSecond()
  {
    model.setText("This is a longer\nMulti lined.");
    model.setCaretIndex(15);

    processor.processKey(press(KeyEvent.KEY_DOWN), model);

    assertSelection(29, 0, false);
  }

  @Test
  public void shouldGoToTheEndOfPreviousLineEvenIfItEndsWithNewline() throws Exception
  {
    model.setText("Some more text\nand some more");
    model.setCaretIndex(28);

    processor.processKey(press(KeyEvent.KEY_UP), model);

    assertSelection(13, 0, false);
  }

  @Test
  public void shouldGoToTheEndOfNextLineEvenIfItEndsWithNewline() throws Exception
  {
    model.setText("blah\nasdf\nasdf");
    model.setCaretIndex(4);

    processor.processKey(press(KeyEvent.KEY_DOWN), model);

    assertSelection(9, 0, false);
  }
}
