package limelight.ui.model.inputs.keyProcessors;

import org.junit.Before;
import org.junit.Test;

import java.awt.event.KeyEvent;

public class ExpandedNormalKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    setUpMultiLine();
    processor = ExpandedNormalKeyProcessor.instance;
    modifier = 0;
  }

  @Test
  public void canProcessTheReturnKey()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_ENTER, '\r');

    processor.processKey(mockEvent, model);

    assertTextState(2, "H\rere are four words");
  }

  @Test
  public void canProcessTheTabKey()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_TAB, '\t');

    processor.processKey(mockEvent, model);

    assertTextState(2, "H\tere are four words");
  }

  @Test
  public void canProcessCharacters()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_A, 'a');

    processor.processKey(mockEvent, model);

    assertTextState(2, "Haere are four words");
  }

  @Test
  public void canProcessBackSpace()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_BACK_SPACE);

    processor.processKey(mockEvent, model);

    assertTextState(0, "ere are four words");
  }

  @Test
  public void canProcessRightArrow()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_RIGHT);

    processor.processKey(mockEvent, model);

    assertSelection(2, 0, false);
  }

  @Test
  public void canProcessLeftArrow()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_LEFT);

    processor.processKey(mockEvent, model);

    assertSelection(0, 0, false);
  }

  @Test
  public void canProcessUpArrowAndDoNothingWhenAllTheWayUp()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_UP);
    model.setText("This is\nMulti lined.");
    model.setCaretIndex(3);

    processor.processKey(mockEvent, model);

    assertSelection(3, 0, false);
  }

  @Test
  public void canProcessUpArrowAndMoveUpALine()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_UP);
    model.setText("This is\nMulti lined.");
    model.setCaretIndex(11);

    processor.processKey(mockEvent, model);

    assertSelection(3, 0, false);
  }

  @Test
  public void canProcessDownArrowAndDoNothingWhenAtBottom()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_DOWN);
    model.setText("This is\nMulti lined.");
    model.setCaretIndex(11);

    processor.processKey(mockEvent, model);

    assertSelection(11, 0, false);
  }

  @Test
  public void canProcessDownArrowAndMoveDownALine()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_DOWN);
    model.setText("This is\nMulti lined.");
    model.setCaretIndex(3);

    processor.processKey(mockEvent, model);

    assertSelection(11, 0, false);
  }

  @Test
  public void canProcessDownArrowAndMoveDownALineToTheLastCharacterIfTheFirstLineRunsPastTheSecond()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_DOWN);
    model.setText("This is a longer\nMulti lined.");
    model.setCaretIndex(15);

    processor.processKey(mockEvent, model);

    assertSelection(29, 0, false);
  }

  @Test
  public void shouldGoToTheEndOfPreviousLineEvenIfItEndsWithNewline() throws Exception
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_UP);
    model.setText("Some more text\nand some more");
    model.setCaretIndex(28);

    processor.processKey(mockEvent, model);

    assertSelection(13, 0, false);
  }

  @Test
  public void shouldGoToTheEndOfNextLineEvenIfItEndsWithNewline() throws Exception
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_DOWN);
    model.setText("blah\nasdf\nasdf");
    model.setCaretIndex(4);

    processor.processKey(mockEvent, model);

    assertSelection(9, 0, false);
  }
}
