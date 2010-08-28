package limelight.ui.model.inputs.keyProcessors;


import limelight.ui.events.KeyEvent;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AltKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    setUpSingleLine();
    processor = AltKeyProcessor.instance;
    modifiers = 8;
  }

//  @Test
//  public void canProcessSpecialKeys()
//  {
//    mockEvent = new MockKeyEvent(modifiers, KeyEvent.KEY_A, 'a');
//
//    processor.processKey(mockEvent, model);
//
//    assertTextState(2, 0, "Haere are four words");
//  }

  @Test
  public void canProcessRightArrowAndJumpToTheNextWord()
  {
    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertEquals(5, model.getCaretIndex());
  }

  @Test
  public void canProcessRightArrowAndSkipOverExtraSpacesToNextWord()
  {
    model.setText("Here are    many  spaces");
    model.setCaretIndex(5);

    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertEquals(12, model.getCaretIndex());
  }

  @Test
  public void canProcessRightArrowAndJumpToTheNextWordInATinyString()
  {
    model.setText("H s");
    model.setCaretIndex(0);

    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertEquals(2, model.getCaretIndex());
  }

  @Test
  public void canProcessRightArrowAndStopAtReturnCharacters()
  {
    model.setText("Here is some\ntext");
    model.setCaretIndex(9);

    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertEquals(13, model.getCaretIndex());
  }

   @Test
  public void canProcessRightArrowAndStopAtReturnCharactersUnlessItIsAChainOfReturnChars()
  {
    model.setText("Here is some\n\n\ntext");
    model.setCaretIndex(9);

    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertEquals(15, model.getCaretIndex());
  }

   @Test
  public void canProcessRightArrowAndWontStopAtReturnCharactersIfPreceededByASpace()
  {
    model.setText("Here is some \ntext");
    model.setCaretIndex(9);

    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertEquals(14, model.getCaretIndex());
  }

  @Test
  public void canProcessLeftArrowAndJumpToThePreviousWord()
  {
    model.setCaretIndex(9);

    processor.processKey(press(KeyEvent.KEY_LEFT), model);

    assertEquals(5, model.getCaretIndex());
  }

  @Test
  public void canProcessLeftArrowAndSkipOverExtraSpacesToPreviousWord()
  {
    model.setText("Here are    many  spaces");
    model.setCaretIndex(12);

    processor.processKey(press(KeyEvent.KEY_LEFT), model);

    assertEquals(5, model.getCaretIndex());
  }

}
