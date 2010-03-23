package limelight.ui.model.inputs;

import com.android.ninepatch.NinePatch;
import limelight.ui.model.Drawable;
import limelight.ui.model.inputs.painters.TextPanelBackgroundPainter;
import limelight.ui.model.inputs.painters.TextPanelCursorPainter;
import limelight.ui.model.inputs.painters.TextPanelSelectionPainter;
import limelight.ui.model.inputs.painters.TextPanelTextPainter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class TextPanelPainterComposite
{
  private TextPanelPainter cursorPainter;
  private TextPanelPainter panelBackgroundPainter;
  private TextPanelPainter selectionPainter;
  private TextPanelPainter textPainter;

  public TextPanelPainterComposite(TextModel boxInfo)
  {
    cursorPainter = new TextPanelCursorPainter(boxInfo);
    try
    {
      Drawable normalDrawable = NinePatch.load(ImageIO.read(getClass().getClassLoader().getResource("limelight/ui/images/text_box.9.png")), true, true);
      Drawable focusDrawable = NinePatch.load(ImageIO.read(getClass().getClassLoader().getResource("limelight/ui/images/text_box_focus.9.png")), true, true);
      panelBackgroundPainter = new TextPanelBackgroundPainter(boxInfo, normalDrawable, focusDrawable);
    }
    catch(IOException e)
    {
      throw new RuntimeException(e);
    }

    selectionPainter = new TextPanelSelectionPainter(boxInfo);
    textPainter = new TextPanelTextPainter(boxInfo);
  }

  public void paint(Graphics2D graphics)
  {
    panelBackgroundPainter.paint(graphics);
    selectionPainter.paint(graphics);
    textPainter.paint(graphics);
    cursorPainter.paint(graphics);
  }

  public TextPanelPainter getCursorPainter()
  {
    return cursorPainter;
  }

  public TextPanelPainter getPanelBackgroundPainter()
  {
    return panelBackgroundPainter;
  }

  public TextPanelPainter getSelectionPainter()
  {
    return selectionPainter;
  }

  public TextPanelPainter getTextPainter()
  {
    return textPainter;
  }

  public void setCursorPainter(TextPanelPainter cursorPainter)
  {
    this.cursorPainter = cursorPainter;
  }

  public void setPanelBackgroundPainter(TextPanelPainter panelBackgroundPainter)
  {
    this.panelBackgroundPainter = panelBackgroundPainter;
  }

  public void setSelectionPainter(TextPanelPainter selectionPainter)
  {
    this.selectionPainter = selectionPainter;
  }

  public void setTextPainter(TextPanelPainter textPainter)
  {
    this.textPainter = textPainter;
  }
}
