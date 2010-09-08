package limelight.ui.model.inputs.keyProcessors;


import limelight.ui.events.KeyEvent;
import limelight.ui.text.TextLocation;
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

  @Test
  public void canProcessRightArrowAndJumpToTheNextWord()
  {
    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertEquals(TextLocation.at(0, 5), model.getCaretLocation());
  }

  @Test
  public void canProcessRightArrowAndSkipOverExtraSpacesToNextWord()
  {
    model.setText("Here are    many  spaces");
    model.setCaretLocation(TextLocation.at(0, 5));

    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertEquals(TextLocation.at(0, 12), model.getCaretLocation());
  }

  @Test
  public void canProcessRightArrowAndJumpToTheNextWordInATinyString()
  {
    model.setText("H s");
    model.setCaretLocation(TextLocation.origin);

    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertEquals(TextLocation.at(0, 2), model.getCaretLocation());
  }

  @Test
  public void canProcessRightArrowAndStopAtReturnCharacters()
  {
    model.setText("Here is some\ntext");
    model.setCaretLocation(TextLocation.at(0, 9));

    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertEquals(TextLocation.at(0, 13), model.getCaretLocation());
  }

   @Test
  public void canProcessRightArrowAndStopAtReturnCharactersUnlessItIsAChainOfReturnChars()
  {
    model.setText("Here is some\n\n\ntext");
    model.setCaretLocation(TextLocation.at(0, 9));

    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertEquals(TextLocation.at(0, 15), model.getCaretLocation());
  }

   @Test
  public void canProcessRightArrowAndWontStopAtReturnCharactersIfPreceededByASpace()
  {
    model.setText("Here is some \ntext");
    model.setCaretLocation(TextLocation.at(0, 9));

    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertEquals(TextLocation.at(0, 14), model.getCaretLocation());
  }

  @Test
  public void canProcessLeftArrowAndJumpToThePreviousWord()
  {
    model.setCaretLocation(TextLocation.at(0, 9));

    processor.processKey(press(KeyEvent.KEY_LEFT), model);

    assertEquals(TextLocation.at(0, 5), model.getCaretLocation());
  }

  @Test
  public void canProcessLeftArrowAndSkipOverExtraSpacesToPreviousWord()
  {
    model.setText("Here are    many  spaces");
    model.setCaretLocation(TextLocation.at(0, 12));

    processor.processKey(press(KeyEvent.KEY_LEFT), model);

    assertEquals(TextLocation.at(0, 5), model.getCaretLocation());
  }

}
