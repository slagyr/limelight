package limelight.ui.model.inputs;

import limelight.ui.model.TextPanel;
import limelight.ui.text.TypedLayout;

import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;

public class Lineator
{
  public static void parseTextForMultipleLayouts(TextModel model, ArrayList<TypedLayout> lines)
  {
    String text = model.getText();
    AttributedString attrString = new AttributedString(text);
    attrString.addAttribute(TextAttribute.FONT, model.getFont());
    AttributedCharacterIterator iterator = attrString.getIterator();

    ArrayList<Integer> newLineCharIndices = findNewLineCharIndices(text);

    LineBreakMeasurer breaker = new LineBreakMeasurer(iterator, TextPanel.getRenderContext());
    int lastCharIndex = 0, newLineCharIndex = 0;
    while(breaker.getPosition() < iterator.getEndIndex())
    {
      lastCharIndex = addNewLayoutForTheNextLine(text, model, lines, breaker, lastCharIndex, newLineCharIndex, newLineCharIndices);
      if(layoutEndedOnNewLineChar(lastCharIndex, newLineCharIndex, newLineCharIndices))
        newLineCharIndex++;
    }
    addBlankLayoutIfLastLineIsEmpty(text, model, lines);
  }

  private static void addBlankLayoutIfLastLineIsEmpty(String text, TextModel model, ArrayList<TypedLayout> lines)
  {
    if(text.length() > 0 && isTheVeryLastCharANewLineChar(text))
      lines.add(model.createLayout(""));
  }

  private static int addNewLayoutForTheNextLine(String text, TextModel model, ArrayList<TypedLayout> lines, LineBreakMeasurer breaker, int lastCharIndex, int newLineCharIndex, ArrayList<Integer> newLineCharIndices)
  {
    int firstCharIndex = lastCharIndex;
    lastCharIndex = firstCharIndex + getNextLayout(model, breaker, newLineCharIndex, newLineCharIndices).getCharacterCount();
    String layoutText = text.substring(firstCharIndex, lastCharIndex);
    lines.add(model.createLayout(layoutText));
    return lastCharIndex;
  }

  private static boolean layoutEndedOnNewLineChar(int lastCharIndex, int returnCharIndex, ArrayList<Integer> newLineCharIndices)
  {
    return thereAreMoreReturnCharacters(returnCharIndex, newLineCharIndices) && lastCharIndex == newLineCharIndices.get(returnCharIndex) + 1;
  }

  private static TextLayout getNextLayout(TextModel model, LineBreakMeasurer breaker, int returnCharIndex, ArrayList<Integer> newLineCharIndices)
  {
    TextLayout layout;
    if(thereAreMoreReturnCharacters(returnCharIndex, newLineCharIndices))
      layout = breaker.nextLayout(model.getContainer().getWidth(), newLineCharIndices.get(returnCharIndex) + 1, false);
    else
      layout = breaker.nextLayout(model.getContainer().getWidth());
    return layout;
  }

  private static boolean isTheVeryLastCharANewLineChar(String text)
  {
    return text.endsWith("\n") || text.endsWith("\r\n");
  }

  private static boolean thereAreMoreReturnCharacters(int returnCharIndex, ArrayList<Integer> newLineCharIndices)
  {
    return newLineCharIndices != null && returnCharIndex < newLineCharIndices.size();
  }

  public static ArrayList<Integer> findNewLineCharIndices(String text)
  {
    ArrayList<Integer> indices = new ArrayList<Integer>();
    for(int i = 0; i < text.length(); i++)
    {
      if(text.charAt(i) == '\n')
        indices.add(i);
      else if(text.charAt(i) == '\r' && text.length() > (i+1) && text.charAt(i+1) == '\n')
      {
        indices.add(i+1);
        i++;
      }
    }
    return indices;
  }
}
