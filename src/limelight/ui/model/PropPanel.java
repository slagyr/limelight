//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.Context;
import limelight.LimelightException;
import limelight.Log;
import limelight.events.Event;
import limelight.events.EventAction;
import limelight.model.api.CastingDirector;
import limelight.styles.*;
import limelight.styles.abstrstyling.StyleValue;
import limelight.ui.*;
import limelight.ui.Panel;
import limelight.model.api.PropProxy;
import limelight.ui.events.panel.MouseEnteredEvent;
import limelight.ui.events.panel.MouseExitedEvent;
import limelight.ui.events.panel.MouseWheelEvent;
import limelight.ui.events.panel.PanelEvent;
import limelight.ui.model.inputs.ScrollBarPanel;
import limelight.ui.painting.*;
import limelight.util.Box;
import limelight.util.EmptyMap;
import limelight.util.Options;
import limelight.util.Util;

import java.awt.*;
import java.util.*;
import java.util.List;

public class PropPanel extends ParentPanelBase implements Prop, PaintablePanel, ChangeablePanel, StyleObserver
{
  private static final Map<String, Object> EMPTY_OPTIONS = new EmptyMap<String, Object>();
  public static final PlayerRecruiter playerRecruiter = new PlayerRecruiter(); 

  private final PropProxy proxy;
  private String id;
  private String name;
  private final ScreenableStyle style;
  private final RichStyle hoverStyle;
  private Border borderShaper;
  private TextAccessor textAccessor;
  private Box marginedBounds;
  private Box borderedBounds;
  private Box paddedBounds;
  private Box childConsumableBounds;
  private PaintAction afterPaintAction;
  private ScrollBarPanel verticalScrollbar;
  private ScrollBarPanel horizontalScrollbar;
  private boolean sizeChangePending = true;
  private Painter painter = DefaultPainter.instance;
  private Cursor preHoverCursor;
  private Map<String, Object> options;
  public Dimension greediness = new Dimension(0, 0);
  public boolean borderChanged = true;

  public PropPanel(PropProxy proxy)
  {
    this.proxy = proxy;
    textAccessor = TempTextAccessor.instance();
    style = new ScreenableStyle();
    hoverStyle = new RichStyle();
    style.addObserver(this);
    getEventHandler().add(MouseWheelEvent.class, MouseWheelAction.instance);
    getEventHandler().add(MouseEnteredEvent.class, HoverOnAction.instance);
    getEventHandler().add(MouseExitedEvent.class, HoverOffAction.instance);
  }

  public PropPanel(PropProxy proxy, Map<String, Object> options)
  {
    this(proxy);
    addOptions(options);
  }

  public String getText()
  {
    return textAccessor.getText();
  }

  public void setText(String text) throws LimelightException
  {
    if(!Util.equal(text, getText()))
    {
      markAsNeedingLayout(); // TODO MDM - This is questionable...  The text panel would know if layout is needed.
    }
    textAccessor.setText(text, this);
  }

  public TextAccessor getTextAccessor()
  {
    return textAccessor;
  }

  public void setTextAccessor(TextAccessor textAccessor)
  {
    this.textAccessor = textAccessor;
  }

  public PropProxy getProxy()
  {
    return proxy;
  }

  @Override
  public Panel getOwnerOfPoint(Point point)
  {
    if(verticalScrollbar != null && verticalScrollbar.getAbsoluteBounds().contains(point))
      return verticalScrollbar;
    else
      if(horizontalScrollbar != null && horizontalScrollbar.getAbsoluteBounds().contains(point))
        return horizontalScrollbar;

    return super.getOwnerOfPoint(point);
  }

  public synchronized Box getMarginedBounds()
  {
    if(marginedBounds == null)
    {
      Box bounds = new Box(0, 0, getWidth(), getHeight());
      marginedBounds = bounds;
      Style style = getStyle();
      marginedBounds.shave(style.getCompiledTopMargin().pixelsFor(bounds.height),
        style.getCompiledRightMargin().pixelsFor(bounds.width),
        style.getCompiledBottomMargin().pixelsFor(bounds.height),
        style.getCompiledLeftMargin().pixelsFor(bounds.width));
    }
    return marginedBounds;
  }

  public synchronized Box getBorderedBounds()
  {
    if(borderedBounds == null)
    {
      Box bounds = getMarginedBounds();
      borderedBounds = (Box) bounds.clone();
      Style style = getStyle();
      borderedBounds.shave(style.getCompiledTopBorderWidth().pixelsFor(bounds.height),
        style.getCompiledRightBorderWidth().pixelsFor(bounds.width),
        style.getCompiledBottomBorderWidth().pixelsFor(bounds.height),
        style.getCompiledLeftBorderWidth().pixelsFor(bounds.width));
    }
    return borderedBounds;
  }

  public synchronized Box getPaddedBounds()
  {
    if(paddedBounds == null)
    {
      Box bounds = getBorderedBounds();
      paddedBounds = (Box) bounds.clone();
      Style style = getStyle();
      paddedBounds.shave(style.getCompiledTopPadding().pixelsFor(bounds.height),
        style.getCompiledRightPadding().pixelsFor(bounds.width),
        style.getCompiledBottomPadding().pixelsFor(bounds.height),
        style.getCompiledLeftPadding().pixelsFor(bounds.width));
    }
    return paddedBounds;
  }

  public Box getChildConsumableBounds()
  {
    if(childConsumableBounds == null)
    {
      Box boxInsidePadding = getPaddedBounds();
      int width = verticalScrollbar == null ? boxInsidePadding.width : boxInsidePadding.width - verticalScrollbar.getWidth();
      int height = horizontalScrollbar == null ? boxInsidePadding.height : boxInsidePadding.height - horizontalScrollbar.getHeight();
      childConsumableBounds = new Box(boxInsidePadding.x, boxInsidePadding.y, width, height);
    }
    return childConsumableBounds;
  }

  public void consumableAreaChanged()
  {
    Style style = getStyle();
    if(!needsLayout() && style != null && style.hasDynamicDimension())
      super.consumableAreaChanged();
  }

  @Override
  public Layout getDefaultLayout()
  {
    return PropPanelLayout.instance;
  }

  public void updateBorder()
  {
    if(borderShaper != null)
    {
      borderShaper.setBounds(getMarginedBounds());
      if(borderChanged)
      {
        borderShaper.updateDimentions();
        borderChanged = false;
      }
    }
  }

  public void paintOn(Graphics2D graphics)
  {
    if(!laidOut)
      return;

    painter.paint(graphics, this);

    if(afterPaintAction != null)
    {
      afterPaintAction.invoke(graphics);
    }
  }

  public ScreenableStyle getStyle()
  {
    return style;
  }

  public RichStyle getHoverStyle()
  {
    return hoverStyle;
  }

  public Border getBorderShaper()
  {
    if(borderShaper == null)
      borderShaper = new Border(getStyle(), getMarginedBounds());
    return borderShaper;
  }

  public void setCursor(Cursor cursor)
  {
    getRoot().setCursor(cursor);
  }

  @Override
  public String toString()
  {
    return getClass().getSimpleName() + " - " + getName();
  }

  public void setAfterPaintAction(PaintAction action)
  {
    afterPaintAction = action;
  }

  public PaintAction getAfterPaintAction()
  {
    return afterPaintAction;
  }

  @Override
  public boolean isFloater()
  {
    return getStyle().getCompiledFloat().isOn();
  }

  @Override
  public void doFloatLayout()
  {
    FloaterLayout.instance.doLayout(this);
  }

  //TODO super.clearCache() deals with absolute positioning.  Here the boxes are all relative.  They're uneccessarily being cleared.
  @Override
  public synchronized void clearCache()
  {
    super.clearCache();
    marginedBounds = null;
    borderedBounds = null;
    paddedBounds = null;
    childConsumableBounds = null;
  }

  public void styleChanged(StyleAttribute attribute, StyleValue value)
  {
    if(isIlluminated() && getParent() != null && getRoot() != null)
      attribute.applyChange(this, value);
  }

  public ScrollBarPanel getVerticalScrollbar()
  {
    return verticalScrollbar;
  }

  public ScrollBarPanel getHorizontalScrollbar()
  {
    return horizontalScrollbar;
  }

  public void addVerticalScrollBar()
  {
    verticalScrollbar = new ScrollBarPanel(ScrollBarPanel.VERTICAL);
    add(verticalScrollbar);
    childConsumableBounds = null;
  }

  public void addHorizontalScrollBar()
  {
    horizontalScrollbar = new ScrollBarPanel(ScrollBarPanel.HORIZONTAL);
    add(horizontalScrollbar);
    childConsumableBounds = null;
  }

  public void removeVerticalScrollBar()
  {
    remove(verticalScrollbar);
    verticalScrollbar = null;
    childConsumableBounds = null;
  }

  public void removeHorizontalScrollBar()
  {
    remove(horizontalScrollbar);
    horizontalScrollbar = null;
    childConsumableBounds = null;
  }

  public void playSound(String filename)
  {
    Context.instance().audioPlayer.playAuFile(filename);
  }

  public boolean isSizeChangePending()
  {
    return sizeChangePending;
  }

  public void setSizeChangePending(boolean value)
  {
    sizeChangePending = value;
  }

  public void propagateSizeChangeUp()
  {
    doPropagateSizeChangeUp(getParent());
  }

  public void propagateSizeChangeDown()
  {
    doPropagateSizeChangeDown();
  }

  public void resetPendingSizeChange()
  {
    sizeChangePending = false;
  }

  public boolean isBorderChanged()
  {
    return borderChanged;
  }

  public void setBorderChanged(boolean value)
  {
    borderChanged = value;
  }

  @Override
  protected boolean canRemove(Panel child)
  {
    return child != verticalScrollbar && child != horizontalScrollbar;
  }

  public void setPainter(Painter instance)
  {
    painter = instance;
  }

  public Painter getPainter()
  {
    return painter;
  }

  @Override
  public boolean hasFocus()
  {
    return textAccessor.hasFocus();
  }

  @Override
  public void illuminate()
  {
    Map<String, Object> illuminateOptions = options == null ? EMPTY_OPTIONS : options;

    Object idObject = illuminateOptions.remove("id");
    if(idObject != null && !idObject.toString().isEmpty())
      id = idObject.toString();

    Object nameObject = illuminateOptions.remove("name");
    if(nameObject != null)
      name = nameObject.toString();

    illuminateStyles(illuminateOptions.remove("styles"));

    if(id != null)
      getRoot().addToIndex(this);

    illuminatePlayers(illuminateOptions.remove("players"));

    Options.apply(this, illuminateOptions);
    Options.apply(getStyle(), illuminateOptions);

    proxy.applyOptions(illuminateOptions);

    for(Map.Entry<String, Object> entry : illuminateOptions.entrySet())
      Log.warn("Prop named '" + name + "' has unused option: " + entry.getKey() + " => " + entry.getValue());

    options = null;

    super.illuminate();
  }

  @Override
  public void delluminate()
  {
    getRoot().removeFromIndex(this);
    style.tearDown();
    hoverStyle.tearDown();
    super.delluminate();
  }

  public void addOptions(Map<String, Object> newOptions)
  {
    if(isIlluminated())
      throw new LimelightException("Cannot add options to an illuminated Prop");

    if(options == null)
      options = new HashMap<String, Object>(newOptions);
    else
    {
      for(Map.Entry<String, Object> entry : newOptions.entrySet())
        options.put(entry.getKey(), entry.getValue());
    }
  }

  public String getId()
  {
    return id;
  }

  public String getName()
  {
    return name;
  }

  public List<PropPanel> getChildPropPanels()
  {
    List<PropPanel> childProps = new LinkedList<PropPanel>();
    for(Panel child : getChildren())
    {
      if(child instanceof PropPanel)
        childProps.add((PropPanel) child);
    }
    return childProps;
  }

  public List<PropPanel> findByName(String name)
  {
    List<PropPanel> results = new LinkedList<PropPanel>();
    if(name == null)
      return results;
    findByName(name, results);
    return results;
  }

  private void findByName(String name, List<PropPanel> results)
  {
    if(name.equals(getName()))
      results.add(this);
    for(Panel panel : getChildren())
    {
      if(panel instanceof PropPanel)
        ((PropPanel)panel).findByName(name, results);
    }
  }

  // PRIVATE ///////////////////////////////////////////////////////////////////////////////////////////////////////////

  private void illuminateStyles(Object stylesObject)
  {
    String allStyles = stylesObject == null ? "" : stylesObject.toString();

    if(name != null)
      allStyles = name + "," + allStyles;

    Map<String, RichStyle> store = getRoot().getStyles();
    String[] styleNames = allStyles.split("[ ,]+");
    for(String styleName : styleNames)
      addStyleNamed(styleName, store);
  }

  private void addStyleNamed(String styleName, Map<String, RichStyle> store)
  {
    if(styleName != null && !styleName.isEmpty())
    {
      RichStyle style = store.get(styleName);
      if(style != null)
        getStyle().addExtension(style);
      else
        if(!styleName.equals(name))
          System.err.println("Prop named '" + name + "' attempting to use missing style '" + styleName + "'"); //TODO - MDM - This should get logged

      RichStyle hoverStyle = store.get(styleName + ".hover");
      if(hoverStyle != null)
      {
        getHoverStyle().addExtension(hoverStyle);
        getStyle().setDefault(Style.CURSOR, "hand");
      }
    }
  }

  private void illuminatePlayers(Object playersObject)
  {
    ArrayList<String> playerNames = new ArrayList<String>();
    String allPlayers = playersObject == null ? "" : playersObject.toString();
    if(name != null)
      playerNames.add(name);

    for(String playerName : allPlayers.split("[ ,]+"))
    {
      if(!playerNames.contains(playerName))
        playerNames.add(playerName);
    }

    for(String playerName : playerNames)
    {
      if(!playerName.isEmpty())
      {
        final Scene scene = getRoot();
        final CastingDirector director = scene.getCastingDirector();
        playerRecruiter.recruit(this, playerName, director);
      }
    }
  }

  private static class MouseWheelAction implements EventAction
  {
    public static MouseWheelAction instance = new MouseWheelAction();

    public void invoke(Event e)
    {
      PanelEvent event = (PanelEvent)e;
      if(!(event.getRecipient() instanceof PropPanel))
        return;

      MouseWheelEvent wheelEvent = (MouseWheelEvent) event;
      PropPanel panel = (PropPanel) event.getRecipient();
      ScrollBarPanel scrollBar = wheelEvent.isVertical() ? panel.getVerticalScrollbar() : panel.getHorizontalScrollbar();
      if(scrollBar != null)
        scrollBar.setValue(scrollBar.getValue() + wheelEvent.getUnitsToScroll());
      else
        if(panel.getParent() != null)
          event.dispatch(panel.getParent());
    }
  }

  private static class HoverOnAction implements EventAction
  {
    public static HoverOnAction instance = new HoverOnAction();

    public void invoke(Event e)
    {
      PanelEvent event = (PanelEvent)e;
      final PropPanel panel = (PropPanel) event.getRecipient();
      if(panel.getRoot() == null)
        return;
      //TODO MDM - If the prop has no suface area (perhasps it's a floater that floated out of bounds), does it still get the mouseExited event?
      if(!panel.getStyle().hasScreen())
        panel.getStyle().applyScreen(panel.getHoverStyle()); // TODO - MDM - This seems inefficient considering most of the time, there's no change in styles.

      Cursor currentCursor = panel.getRoot().getCursor();
      Cursor hoverCursor = panel.getStyle().getCompiledCursor().getCursor();
      if(hoverCursor != currentCursor)
      {
        panel.preHoverCursor = currentCursor;
        panel.getRoot().setCursor(hoverCursor);
      }
    }
  }

  private static class HoverOffAction implements EventAction
  {
    public static HoverOffAction instance = new HoverOffAction();

    public void invoke(Event e)
    {
      PanelEvent event = (PanelEvent)e;
      final PropPanel panel = (PropPanel) event.getRecipient();
      if(panel.getStyle().hasScreen())
        panel.getStyle().removeScreen();

      if(panel.preHoverCursor != null)
      {
        final Scene root = panel.getRoot();
        // TODO MDM - If the panel is removed from the scene, the cursor is never changed.  Perhaps the right way to do this is in the MouseListener... when ever entering a panel, get the cursor and change it.... when exiting a panel, pop the cursor
        if(root != null)
          root.setCursor(panel.preHoverCursor);
        panel.preHoverCursor = null;
      }
    }
  }
}

