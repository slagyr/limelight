package limelight.ui.model.inputs;

import limelight.ui.model.*;
import limelight.util.Box;
import limelight.styles.Style;
import limelight.styles.HorizontalAlignment;
import limelight.styles.VerticalAlignment;
import limelight.styles.styling.SimpleHorizontalAlignmentAttribute;
import limelight.styles.styling.SimpleVerticalAlignmentAttribute;
import limelight.Context;

import java.awt.event.MouseEvent;
import java.awt.event.FocusEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.*;
import java.awt.font.TextLayout;

public class TextBox2Panel extends BasePanel implements TextAccessor, InputPanel
{
  private boolean focused;
  private StringBuffer text;
  private static SimpleHorizontalAlignmentAttribute horizontalTextAlignment = new SimpleHorizontalAlignmentAttribute(HorizontalAlignment.LEFT);
  private static SimpleVerticalAlignmentAttribute verticalTextAlignment = new SimpleVerticalAlignmentAttribute(VerticalAlignment.CENTER);
  private Dimension textDimensions;
  private TextLayout textLayout;
  private static Font font = new Font("Arial", Font.PLAIN, 12);
  private boolean cursorOn;
  private int cursorX = 0;
  private Thread cursorThread;
  private int cursorIndex = 0;
  private boolean selectingTextOn;
  private int selectStartX;
  private int textX = 3;
  private int xOffset;

  public TextBox2Panel()
  {
    setSize(150, 25);
  }

  public void setParent(limelight.ui.Panel panel)
  {
    if (panel == null)
      Context.instance().keyboardFocusManager.focusFrame((StageFrame) getRoot().getStageFrame());
    super.setParent(panel);
    if (panel instanceof PropPanel)
    {
      PropPanel propPanel = (PropPanel) panel;
      propPanel.sterilize();
      propPanel.setTextAccessor(this);
    }
  }

  public Box getBoxInsidePadding()
  {
    return getBoundingBox();
  }

  public Box getChildConsumableArea()
  {
    return getBoundingBox();
  }

  public void paintOn(Graphics2D graphics)
  {
    graphics.setColor(Color.lightGray);
    graphics.fillRect(0, 0, width, height);

    if (focused)
      graphics.setColor(Color.green);
    else
      graphics.setColor(Color.gray);

    graphics.drawRect(0, 0, width - 1, height - 1);

    if (text != null && text.length() > 0)
    {
      graphics.setColor(Color.BLACK);
      calculateTextDimensions();

      if (textDimensions.width >= width)
      {
        int newXOffset = textDimensions.width - width + 7;
        if (!(cursorX < width - 4 && newXOffset > xOffset))
          xOffset = newXOffset;
        if (cursorX < 4)
          shiftCursorAndTextRight();
      }
      else
        xOffset = 0;

      textX = horizontalTextAlignment.getX(textDimensions.width, getBoundingBox()) + 3 - xOffset;
      System.out.println(" textX = " + textX + " xOffset = " + xOffset + " width = " + textDimensions.width + " cursor= " + cursorX);
      System.out.println();

      float textY = verticalTextAlignment.getY(textDimensions.height, getBoundingBox()) + textLayout.getAscent();
      setCursorPosition();
      if (selectingTextOn)
      {
        graphics.setColor(Color.cyan);
        if (cursorX > selectStartX)
          graphics.fillRect(selectStartX, 4, cursorX - selectStartX, height - 8);
        else
          graphics.fillRect(cursorX, 4, selectStartX - cursorX, height - 8);

      }
      graphics.setColor(Color.black);
      textLayout.draw(graphics, textX, textY + 1);
    }

    else
      cursorX = 2;

    if (cursorOn)

    {
      graphics.setColor(Color.black);
      graphics.drawLine(cursorX, 4, cursorX, height - 4);
    }

  }

  private void shiftCursorAndTextRight()
  {
    String rightShiftingText = text.substring(cursorIndex / 2, cursorIndex);
    if (rightShiftingText.length() == 0)
    {
      cursorX = 2;
      xOffset = 0;
    }
    else
    {
      TextLayout textLayout = this.textLayout;
      this.textLayout = new TextLayout(rightShiftingText, font, TextPanel.staticFontRenderingContext);
      int x = getWidthDimension();
      xOffset -= x;
      if (xOffset < 0)
        xOffset = 0;
      cursorX += x;

      this.textLayout = textLayout;
    }
  }

  private void setCursorPosition()
  {
    String toCursorString = text.substring(0, cursorIndex);
    if (toCursorString.length() == 0)
      cursorX = 2;
    else
    {
      cursorX = getCursorXFromTextLayout(toCursorString) + 3 - xOffset;
      addTerminatingSpaceWidth(toCursorString);
    }
  }

  private int getCursorXFromTextLayout(String toCursorString)
  {
    TextLayout textLayout = this.textLayout;
    this.textLayout = new TextLayout(toCursorString, font, TextPanel.staticFontRenderingContext);
    int x = getWidthDimension();
    this.textLayout = textLayout;
    return x;
  }

  private void addTerminatingSpaceWidth(String toCursorString)
  {
    if (toCursorString.charAt(toCursorString.length() - 1) == ' ')
    {
      int i = toCursorString.length() - 1;
      while (toCursorString.charAt(i) == ' ' && i > 0)
      {
        cursorX += 3;
        i--;
      }
    }
  }

  private void calculateTextDimensions()
  {
    if (text != null && text.length() > 0)
    {
      textLayout = new TextLayout(text.toString(), font, TextPanel.staticFontRenderingContext);
      int height = getHeightDimension();
      int width = getWidthDimension();
      textDimensions = new Dimension(width, height);
    }
  }

  private int getHeightDimension()
  {
    return (int) ((textLayout.getAscent() + textLayout.getDescent() + textLayout.getLeading()) + 0.5);
  }

  private int getWidthDimension()
  {
    return (int) (textLayout.getBounds().getWidth() + textLayout.getBounds().getX() + 0.5);
  }

  public Style getStyle()
  {
    return getParent().getStyle();
  }

  public void setText(String text)
  {
    this.text = new StringBuffer(text);
    cursorIndex = text.length();
  }

  public String getText()
  {
    if (text == null)
      return null;
    return text.toString();
  }

  public void mousePressed(MouseEvent e)
  {
    super.mousePressed(e);
  }

  public void mouseReleased(MouseEvent e)
  {
    super.mouseReleased(e);
    Context.instance().keyboardFocusManager.focusPanel(this);
    focusGained(new FocusEvent(getRoot().getStageFrame().getWindow(), 0));
    super.buttonPressed(new ActionEvent(this, 0, "blah"));
  }

  public void focusGained(FocusEvent e)
  {
    focused = true;
    markAsDirty();
    startCursor();
    super.focusGained(e);
  }

  public void focusLost(FocusEvent e)
  {
    focused = false;
    stopCursor();
    markAsDirty();
    super.focusLost(e);

  }

  public void keyPressed(KeyEvent e)
  {
    int key = e.getKeyCode();
    if (e.getModifiers() != 1)
      selectingTextOn = false;
    if (key == KeyEvent.VK_BACK_SPACE)
    {
      if (cursorIndex > 0)
      {
        text.deleteCharAt(cursorIndex - 1);
        cursorIndex--;
      }
    }
    else if (key > 40 && key < 100 || key == 222 || key == 32)
    {
      text.insert(cursorIndex, e.getKeyChar());
      cursorIndex++;
    }
    else if (key == 37 && cursorIndex > 0)
    {
      cursorIndex--;
      if (e.getModifiers() == 1 && !selectingTextOn)
      {
        selectingTextOn = true;
        selectStartX = cursorX;
      }
    }
    else if (key == 39 && cursorIndex < text.length())
    {
      cursorIndex++;
      if (e.getModifiers() == 1 && !selectingTextOn)
      {
        selectingTextOn = true;
        selectStartX = cursorX;
      }
    }
    markAsDirty();
  }

  public void keyReleased(KeyEvent e)
  {

  }


  private void startCursor()
  {
    if (cursorThread == null || !cursorThread.isAlive())
    {
      cursorThread = new Thread(new Runnable()
      {
        public void run()
        {
          while (true)
          {
            cursorOn = !cursorOn;
            markAsDirty();

            try
            {
              Thread.sleep(500);
            }
            catch (InterruptedException e)
            {
              e.printStackTrace();
            }
          }
        }
      });
      cursorThread.start();
    }
  }

  private void stopCursor()
  {
    cursorThread.interrupt();
    cursorOn = false;
    markAsDirty();
  }
}
