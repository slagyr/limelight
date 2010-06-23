package limelight.ui.model.inputs.keyProcessors;


import org.junit.Before;
import org.junit.Test;

import java.awt.event.KeyEvent;

import static org.junit.Assert.assertEquals;

public class AltKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    textBoxSetUp();
    processor = AltKeyProcessor.instance;
    modifier = 8;
  }

  @Test
  public void canProcessSpecialKeys()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_A, 'a');

    processor.processKey(mockEvent, modelInfo);

    asserter.assertTextState(2, "Haere are four words");
  }

  @Test
  public void canProcessRightArrowAndJumpToTheNextWord()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_RIGHT);

    processor.processKey(mockEvent, modelInfo);

    assertEquals(5, modelInfo.getCaretIndex());
  }

  @Test
  public void canProcessRightArrowAndSkipOverExtraSpacesToNextWord()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_RIGHT);
    modelInfo.setText("Here are    many  spaces");
    modelInfo.setCaretIndex(5);

    processor.processKey(mockEvent, modelInfo);

    assertEquals(12, modelInfo.getCaretIndex());
  }

  @Test
  public void canProcessRightArrowAndJumpToTheNextWordInATinyString()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_RIGHT);
    modelInfo.setText("H s");
    modelInfo.setCaretIndex(0);

    processor.processKey(mockEvent, modelInfo);

    assertEquals(2, modelInfo.getCaretIndex());
  }

  @Test
  public void canProcessRightArrowAndStopAtReturnCharacters()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_RIGHT);
    modelInfo.setText("Here is some\ntext");
    modelInfo.setCaretIndex(9);

    processor.processKey(mockEvent, modelInfo);

    assertEquals(13, modelInfo.getCaretIndex());
  }

   @Test
  public void canProcessRightArrowAndStopAtReturnCharactersUnlessItIsAChainOfReturnChars()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_RIGHT);
    modelInfo.setText("Here is some\n\n\ntext");
    modelInfo.setCaretIndex(9);

    processor.processKey(mockEvent, modelInfo);

    assertEquals(15, modelInfo.getCaretIndex());
  }

   @Test
  public void canProcessRightArrowAndWontStopAtReturnCharactersIfPreceededByASpace()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_RIGHT);
    modelInfo.setText("Here is some \ntext");
    modelInfo.setCaretIndex(9);

    processor.processKey(mockEvent, modelInfo);

    assertEquals(14, modelInfo.getCaretIndex());
  }

  @Test
  public void canProcessLeftArrowAndJumpToThePreviousWord()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_LEFT);
    modelInfo.setCaretIndex(9);

    processor.processKey(mockEvent, modelInfo);

    assertEquals(5, modelInfo.getCaretIndex());
  }

  @Test
  public void canProcessLeftArrowAndSkipOverExtraSpacesToPreviousWord()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_LEFT);
    modelInfo.setText("Here are    many  spaces");
    modelInfo.setCaretIndex(12);

    processor.processKey(mockEvent, modelInfo);

    assertEquals(5, modelInfo.getCaretIndex());
  }

}
