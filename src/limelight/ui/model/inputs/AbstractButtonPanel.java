//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model.inputs;

import limelight.events.Event;
import limelight.events.EventAction;
import limelight.styles.ScreenableStyle;
import limelight.ui.Fonts;
import limelight.ui.Panel;
import limelight.ui.events.panel.ButtonPushedEvent;
import limelight.ui.events.panel.CharTypedEvent;
import limelight.ui.events.panel.MouseClickedEvent;
import limelight.ui.events.panel.PanelEvent;
import limelight.ui.model.TextPanel;

import java.awt.*;
import java.awt.font.TextLayout;

public abstract class AbstractButtonPanel extends InputPanel
{
  protected AbstractButtonPanel()
  {
    getEventHandler().add(MouseClickedEvent.class, PushAction.instance);
    getEventHandler().add(CharTypedEvent.class, PushAction.instance);
    getEventHandler().add(ButtonPushedEvent.class, PropogateToParentAction.instance);
  }

  public void paintOn(Graphics2D graphics)
  {
    ButtonTextPainter.paintOn(graphics, this);
  }

  private static boolean isPushEvent(PanelEvent event)
  {
    return event instanceof MouseClickedEvent || ((event instanceof CharTypedEvent) && ((CharTypedEvent) event).getChar() == ' ');
  }

  private static class PushAction implements EventAction
  {
    public static PushAction instance = new PushAction();

    public void invoke(Event e)
    {
      PanelEvent event = (PanelEvent)e;
      if(event.isConsumed())
        return;

      if(isPushEvent(event))
      {
        final Panel panel = event.getRecipient();
        new ButtonPushedEvent().dispatch(panel);
      }
    }
  }

  protected static class ButtonTextPainter
  {
    public static void paintOn(Graphics2D graphics, AbstractButtonPanel panel)
    {
      final String text = panel.getText();

      if(text == null || text.length() == 0)
        return;

      final ScreenableStyle style = panel.getStyle();
      graphics.setColor(style.getCompiledTextColor().getColor());

      TextLayout textLayout = new TextLayout(text, Fonts.fromStyle(style), TextPanel.staticFontRenderingContext);
      int height = (int) ((textLayout.getAscent() + textLayout.getDescent() + textLayout.getLeading()) + 0.5);
      int width = (int) (textLayout.getBounds().getWidth() + textLayout.getBounds().getX() + 0.5);
      final Dimension textDimensions = new Dimension(width, height);

      int textX = style.getCompiledHorizontalAlignment().getX(textDimensions.width, panel.getBounds());
      float textY = style.getCompiledVerticalAlignment().getY(textDimensions.height, panel.getBounds()) + textLayout.getAscent();
      textLayout.draw(graphics, textX, textY + 1);
    }
  }

}
