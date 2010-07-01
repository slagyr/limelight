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
    setUpSingleLine();
    processor = AltKeyProcessor.instance;
    modifier = 8;
  }

  @Test
  public void canProcessSpecialKeys()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_A, 'a');

    processor.processKey(mockEvent, model);

    assertTextState(2, "Haere are four words");
  }

  @Test
  public void canProcessRightArrowAndJumpToTheNextWord()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_RIGHT);

    processor.processKey(mockEvent, model);

    assertEquals(5, model.getCaretIndex());
  }

  @Test
  public void canProcessRightArrowAndSkipOverExtraSpacesToNextWord()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_RIGHT);
    model.setText("Here are    many  spaces");
    model.setCaretIndex(5);

    processor.processKey(mockEvent, model);

    assertEquals(12, model.getCaretIndex());
  }

  @Test
  public void canProcessRightArrowAndJumpToTheNextWordInATinyString()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_RIGHT);
    model.setText("H s");
    model.setCaretIndex(0);

    processor.processKey(mockEvent, model);

    assertEquals(2, model.getCaretIndex());
  }

  @Test
  public void canProcessRightArrowAndStopAtReturnCharacters()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_RIGHT);
    model.setText("Here is some\ntext");
    model.setCaretIndex(9);

    processor.processKey(mockEvent, model);

    assertEquals(13, model.getCaretIndex());
  }

   @Test
  public void canProcessRightArrowAndStopAtReturnCharactersUnlessItIsAChainOfReturnChars()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_RIGHT);
    model.setText("Here is some\n\n\ntext");
    model.setCaretIndex(9);

    processor.processKey(mockEvent, model);

    assertEquals(15, model.getCaretIndex());
  }

   @Test
  public void canProcessRightArrowAndWontStopAtReturnCharactersIfPreceededByASpace()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_RIGHT);
    model.setText("Here is some \ntext");
    model.setCaretIndex(9);

    processor.processKey(mockEvent, model);

    assertEquals(14, model.getCaretIndex());
  }

  @Test
  public void canProcessLeftArrowAndJumpToThePreviousWord()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_LEFT);
    model.setCaretIndex(9);

    processor.processKey(mockEvent, model);

    assertEquals(5, model.getCaretIndex());
  }

  @Test
  public void canProcessLeftArrowAndSkipOverExtraSpacesToPreviousWord()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_LEFT);
    model.setText("Here are    many  spaces");
    model.setCaretIndex(12);

    processor.processKey(mockEvent, model);

    assertEquals(5, model.getCaretIndex());
  }

}
