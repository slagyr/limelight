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
    return findNextWordSkippingSpaces(modelInfo.findWordsRightEdge(modelInfo.getCursorIndex()));
  }

  private int findNextWordSkippingSpaces(int startIndex)
  {
    for (int i = startIndex; i <= modelInfo.getText().length() - 1; i++)
    {
      if (modelInfo.getText().charAt(i - 1) == ' ' && modelInfo.getText().charAt(i) != ' ')
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
    if (modelInfo.getXPosFromIndex(charCount + previousLineLength) < xPos)
    {
      modelInfo.setCursorIndex(charCount + previousLineLength -1);
    }
    else
    {
      TextHitInfo hitInfo = modelInfo.textLayouts.get(currentLine - 1).hitTestChar(xPos, 5);
      int index = hitInfo.getCharIndex() + charCount;
      modelInfo.setCursorIndex(index);
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
    if (modelInfo.getXPosFromIndex(charCount + nextLineLength) < xPos)
    {
      modelInfo.setCursorIndex(charCount + nextLineLength -1);
    }
    else
    {
      TextHitInfo hitInfo = modelInfo.textLayouts.get(currentLine + 1).hitTestChar(xPos, 5);
      int index = hitInfo.getCharIndex() + charCount;
      modelInfo.setCursorIndex(index);
    }
  }


  protected boolean isAnExtraKey(int keyCode)
  {
    return keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_TAB;
  }
}

