package limelight.ui.model.inputs.keyProcessors;

import org.junit.Before;
import org.junit.Test;

import java.awt.event.KeyEvent;

public class ExpandedNormalKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    textAreaSetUp();
    processor = ExpandedNormalKeyProcessor.instance;
    modifier = 0;
  }

  @Test
  public void canProcessTheReturnKey()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_ENTER, '\r');

    processor.processKey(mockEvent, modelInfo);

    asserter.assertTextState(2, "H\rere are four words");
  }

  @Test
  public void canProcessTheTabKey()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_TAB, '\t');

    processor.processKey(mockEvent, modelInfo);

    asserter.assertTextState(2, "H\tere are four words");
  }

  @Test
  public void canProcessCharacters()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_A, 'a');

    processor.processKey(mockEvent, modelInfo);

    asserter.assertTextState(2, "Haere are four words");
  }

  @Test
  public void canProcessBackSpace()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_BACK_SPACE);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertTextState(0, "ere are four words");
  }

  @Test
  public void canProcessRightArrow()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_RIGHT);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(2, 0, false);
  }

  @Test
  public void canProcessLeftArrow()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_LEFT);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(0, 0, false);
  }

  @Test
  public void canProcessUpArrowAndDoNothingWhenAllTheWayUp()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_UP);
    modelInfo.setText("This is\nMulti lined.");
    modelInfo.setCursorIndex(3);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(3, 0, false);
  }

  @Test
  public void canProcessUpArrowAndMoveUpALine()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_UP);
    modelInfo.setText("This is\nMulti lined.");
    modelInfo.setCursorIndex(11);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(3, 0, false);
  }

//    @Test
//    public void canProcessDownArrowAndDoNothingWhenAtBottom()
//    {
//      mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_DOWN);
//      modelInfo.setText("This is\nMulti lined.");
//      modelInfo.cursorIndex = 11;
//
//      processor.processKey(mockEvent);
//
//      asserter.assertSelection(11, 0, false);
//    }

  @Test
  public void canProcessDownArrowAndMoveDownALine()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_DOWN);
    modelInfo.setText("This is\nMulti lined.");
    modelInfo.setCursorIndex(3);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(10, 0, false);
  }

//    @Test
//    public void canProcessDownArrowAndMoveDownALineToTheLastCharacterIfTheFirstLineRunsPastTheSecond()
//    {
//      mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_DOWN);
//      modelInfo.setText("This is a longer\nMulti lined.");
//      modelInfo.cursorIndex = 15;
//
//      processor.processKey(mockEvent);
//
//      asserter.assertSelection(29, 0, false);
//    }

  @Test
  public void willRecallTheLastCursorPlaceToJumpBackTo()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_DOWN);
    modelInfo.setText("This is\nMulti lined.");
    modelInfo.setLastKeyPressed(KeyEvent.VK_UP);
    modelInfo.setCursorIndex(3);
    modelInfo.setLastCursorIndex(11);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(11, 0, false);
  }

//    @Test
//    public void shouldGoToTheEndOfPreviousLineEvenIfItEndsWithNewline() throws Exception
//    {
//      mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_UP);
//      modelInfo.setText("Some more text\nand some more");
//      modelInfo.cursorIndex = 28;
//
//      processor.processKey(mockEvent);
//
//      asserter.assertSelection(14, 0, false);
//    }

  @Test
  public void shouldGoToTheEndOfNextLineEvenIfItEndsWithNewline() throws Exception
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_DOWN);
    modelInfo.setText("blah\nasdf\nasdf");
    modelInfo.setCursorIndex(4);

    processor.processKey(mockEvent, modelInfo);

    asserter.assertSelection(9, 0, false);
  }
}
