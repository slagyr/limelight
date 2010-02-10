package limelight.ui.model.inputs;

import java.awt.event.KeyEvent;
import java.awt.font.TextHitInfo;

public abstract class KeyProcessor
{
  protected TextModel modelInfo;

  public KeyProcessor(TextModel modelInfo)
  {
    this.modelInfo = modelInfo;
  }

  public abstract void processKey(KeyEvent event);

  protected boolean isACharacter(int keyCode)
  {
    return (keyCode > 40 && keyCode < 100 || keyCode == 222 || keyCode == 32);
  }

  protected void insertCharIntoTextBox(char c)
  {
    if(c == KeyEvent.CHAR_UNDEFINED)
      return;
    modelInfo.text.insert(modelInfo.getCursorIndex(), c);
    modelInfo.setCursorIndex(modelInfo.getCursorIndex() + 1);
  }


  protected boolean isMoveRightEvent(int keyCode)
  {
    return keyCode == KeyEvent.VK_RIGHT && modelInfo.getCursorIndex() < modelInfo.getText().length();
  }

  protected boolean isMoveLeftEvent(int keyCode)
  {
    return keyCode == KeyEvent.VK_LEFT && modelInfo.getCursorIndex() > 0;
  }

  protected boolean isMoveUpEvent(int keyCode)
  {
    return keyCode == KeyEvent.VK_UP && modelInfo.getLineNumberOfIndex(modelInfo.cursorIndex) > 0;
  }

  protected boolean isMoveDownEvent(int keyCode)
  {
    return keyCode == KeyEvent.VK_DOWN && modelInfo.getLineNumberOfIndex(modelInfo.cursorIndex) < modelInfo.textLayouts.size() - 1;
  }

  protected void initSelection()
  {
    modelInfo.selectionOn = true;
    modelInfo.setSelectionIndex(modelInfo.getCursorIndex());
  }

  protected int findNearestWordToTheLeft()
  {
    return modelInfo.findWordsLeftEdge(modelInfo.getCursorIndex() - 1);
  }

  protected int findNearestWordToTheRight()
  {
    return findNextWordSkippingSpacesOrNewLines(modelInfo.findWordsRightEdge(modelInfo.getCursorIndex()));
  }

  private int findNextWordSkippingSpacesOrNewLines(int startIndex)
  {
    for (int i = startIndex; i <= modelInfo.getText().length() - 1; i++)
    {
      if (modelInfo.isAtStartOfWord(i))
        return i;
    }
    return modelInfo.getText().length();
  }

  protected void selectAll()
  {
    modelInfo.selectionOn = true;
    modelInfo.setCursorIndex(modelInfo.getText().length());
    modelInfo.setSelectionIndex(0);
  }


  protected void moveCursorUpALine()
  {
    if (modelInfo.getLastKeyPressed() == KeyEvent.VK_DOWN)
    {
      modelInfo.setCursorIndex(modelInfo.getLastCursorIndex());
      return;
    }
    int currentLine = modelInfo.getLineNumberOfIndex(modelInfo.cursorIndex);
    int charCount = 0;
    for (int i = 0; i < currentLine - 1; i++)
      charCount += modelInfo.textLayouts.get(i).getText().length();
    int xPos = modelInfo.getXPosFromIndex(modelInfo.cursorIndex);
    int previousLineLength = modelInfo.textLayouts.get(currentLine - 1).getText().length();
    int newCursorIndex = charCount + previousLineLength -1;
    if (modelInfo.getXPosFromIndex(newCursorIndex) < xPos)
    {
      modelInfo.setCursorIndex(newCursorIndex);
    }
    else
    {
      TextHitInfo hitInfo = modelInfo.textLayouts.get(currentLine - 1).hitTestChar(xPos, 5);
      newCursorIndex = hitInfo.getCharIndex() + charCount;
      modelInfo.setCursorIndex(newCursorIndex);
    }
  }

  protected void moveCursorDownALine()
  {
    if (modelInfo.getLastKeyPressed() == KeyEvent.VK_UP)
    {
      modelInfo.setCursorIndex(modelInfo.getLastCursorIndex());
      return;
    }
    int currentLine = modelInfo.getLineNumberOfIndex(modelInfo.cursorIndex);
    int charCount = 0;
    for (int i = 0; i <= currentLine; i++)
      charCount += modelInfo.textLayouts.get(i).getText().length();
    int xPos = modelInfo.getXPosFromIndex(modelInfo.cursorIndex);
    int nextLineLength = modelInfo.textLayouts.get(currentLine + 1).getText().length();
    int newCursorIndex = charCount + nextLineLength -1;
    if(currentLine+1 == modelInfo.textLayouts.size() -1)
        newCursorIndex ++;
    if (modelInfo.getXPosFromIndex(newCursorIndex) < xPos)
    {
      modelInfo.setCursorIndex(newCursorIndex);
    }
    else
    {
      TextHitInfo hitInfo = modelInfo.textLayouts.get(currentLine + 1).hitTestChar(xPos, 5);
      newCursorIndex = hitInfo.getCharIndex() + charCount;
      modelInfo.setCursorIndex(newCursorIndex);
    }

  }


  protected boolean isAnExtraKey(int keyCode)
  {
    return keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_TAB;
  }

  protected void sendCursorToStartOfLine()
  {
    int currentLine = modelInfo.getLineNumberOfIndex(modelInfo.getCursorIndex());

    if (currentLine == 0)
    {
      modelInfo.setCursorIndex(0);
    }
    else
    {
      modelInfo.setCursorIndex(modelInfo.getIndexOfLastCharInLine(currentLine - 1) + 1);
    }
  }

  protected void sendCursorToEndOfLine()
  {
    int currentLine = modelInfo.getLineNumberOfIndex(modelInfo.getCursorIndex());
    modelInfo.setCursorIndex(modelInfo.getIndexOfLastCharInLine(currentLine));
  }
}

