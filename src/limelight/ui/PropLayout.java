//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui;


import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Iterator;

public class PropLayout implements LayoutManager
{
	private LinkedList<Row> rows;
	private Row currentRow;
  private Panel panel;
  private boolean inScrollMode;
  private JPanel scrollView;
  private limelight.ui.Rectangle area;
  private int consumedWidth;
  private int consumedHeight;
  private JScrollPane scrollPane;
  private LinkedList<Panel> floaters;

  private static long totalDuration = 0;
  private static int calls = 0;

  public PropLayout(Panel panel)
  {
    this.panel = panel;
    rows = new LinkedList<Row>();
    inScrollMode = false;
	}

  public boolean isInScrollMode()
  {
    return inScrollMode;
  }

  public JPanel getScrollView()
  {
    return scrollView;
  }

  public void addLayoutComponent(String string, Component component)
	{
	}

	public void removeLayoutComponent(Component component)
	{
	}

	public Dimension preferredLayoutSize(Container container)
	{
		return container.getPreferredSize();
	}

	public Dimension minimumLayoutSize(Container container)
	{
		return container.getMinimumSize();
	}

  private Component[] getComponents()
  {
    if(inScrollMode)
      return scrollView.getComponents();
    else
      return panel.getComponents();
  }

  public void layoutContainer(Container container)
	{
long start = System.currentTimeMillis();
    Dimension d = panel.getMaximumSize();
    panel.setSize(d);

    reset();
    buildRows();

    //TODO Hack - March 17, 08
    if("auto".equals(panel.getStyle().getHeight()))
      area = new Rectangle(area.x, area.y, area.width, consumedHeight);

    boolean vertical_scrollbar_required = (consumedHeight > area.height) && !"off".equals(panel.getStyle().getVerticalScrollbar());
    boolean horizontal_scrollbar_required = (consumedWidth > area.width) && !"off".equals(panel.getStyle().getHorizontalScrollbar());
    if(!inScrollMode && (vertical_scrollbar_required || horizontal_scrollbar_required))
      enterScrollMode();
    if(inScrollMode && consumedHeight <= area.height && consumedWidth <= area.width)
      exitScrollMode();

    if(inScrollMode)
    {
      scrollPane.setSize(area.width,  area.height);
      scrollView.setSize(consumedWidth, consumedHeight);
      scrollPane.doLayout();
      collapseAutoDimensions();
    }
    else
    {
      collapseAutoDimensions();
      doNormalLayout();
    }
long duration = System.currentTimeMillis() - start;
totalDuration += duration;
calls++;
//System.err.println("duration = " + duration + "\t\t" + totalDuration + "\t" + calls);
  }

  private void collapseAutoDimensions()
  {
    boolean hasAutoWidth = "auto".equals(panel.getStyle().getWidth());
    boolean hasAutoHeight = "auto".equals(panel.getStyle().getHeight());
    if(hasAutoWidth && hasAutoHeight)
      panel.setSize(consumedWidth + horizontalInsets(), consumedHeight + verticalInsets());
    else if(hasAutoWidth)
      panel.setSize(consumedWidth + horizontalInsets(), panel.getHeight());
    else if(hasAutoHeight)
      panel.setSize(panel.getWidth(), consumedHeight + verticalInsets());
  }

  public int horizontalInsets()
  {
    return panel.getRectangle().width - panel.getRectangleInsidePadding().width;
  }

  public int verticalInsets()
  {
    return panel.getRectangle().height - panel.getRectangleInsidePadding().height;
  }

  private void doNormalLayout()
  {
    Aligner aligner = buildAligner(panel.getRectangleInsidePadding());
    aligner.addConsumedHeight(consumedHeight);
    layoutRows(aligner);
    layoutFloaters();
  }

  private void layoutFloaters()
  {
    if(floaters == null)
      return;
    for (Panel floater : floaters)
      layoutFloater(floater);
  }

  private void layoutFloater(Panel floater)
  {
    Style style = floater.getStyle();
    int x = style.asInt(style.getX());
    int y = style.asInt(style.getY());
    floater.setLocation(area.x + x, area.y + y);
  }

  private void layoutRows(Aligner aligner)
  {
    int y = aligner.startingY();
    for(Row row : rows)
    {
      int x = aligner.startingX(row.width);
      row.layoutComponents(x, y);
      y += row.height;
    }
  }

  private void enterScrollMode()
  { 
    scrollView = new JPanel();
    scrollView.addMouseListener(panel.getListener());
    scrollView.setLayout(new ScrollViewLayout(scrollView));
    scrollView.setOpaque(false);
    scrollView.setSize(consumedWidth, consumedHeight);
    scrollView.setLocation(0, 0);
    for (Component component : getComponents())
      scrollView.add(component);

    scrollPane = new JScrollPane(scrollView);
    scrollPane.getViewport().setOpaque(false);
    scrollPane.getViewport().setBackground(Color.blue);
    scrollPane.setOpaque(false);
    scrollPane.setBackground(Color.orange);
    scrollPane.setBorder(BorderFactory.createEmptyBorder());
    scrollPane.setSize(area.width,  area.height);
    scrollPane.setLocation(area.x, area.y);
    scrollPane.getVerticalScrollBar().setUnitIncrement((int)(area.height * 0.1));
    scrollPane.getHorizontalScrollBar().setUnitIncrement((int)(area.width * 0.1));
    panel.replaceChildren(new Component[] {scrollPane});
    scrollPane.doLayout();

    inScrollMode = true;
  }

  private void exitScrollMode()
  {
    panel.replaceChildren(scrollView.getComponents());
    inScrollMode = false;
  }

  private void buildRows()
	{
    for(Component component : getComponents())
    {
      component.doLayout();
      if(isFloater(component))
        addFloater(component);
      else
        addToRow(component);
    }
    calculateConsumedDimentions();
  }

  private void addToRow(Component component)
  {
    if(!currentRow.isEmpty() && !currentRow.fits(component))
      newRow();
    currentRow.add(component);
  }

  private boolean isFloater(Component component)
  {
    return component instanceof Panel && ((Panel)component).isFloater();
  }

  private void addFloater(Component component)
  {
    if(floaters == null)
      floaters = new LinkedList<Panel>();
    floaters.add((Panel)component);
  }

  private Aligner buildAligner(limelight.ui.Rectangle rectangle)
  {
    return new Aligner(rectangle, panel.getStyle().getHorizontalAlignment(), panel.getStyle().getVerticalAlignment());
  }

  private void reset()
	{
    area = panel.getRectangleInsidePadding();
    floaters = null;
    rows.clear();
		newRow();
	}

	private void newRow()
	{
		currentRow = new Row(area.width);
		rows.add(currentRow);
	}

  private void calculateConsumedDimentions()
  {
    consumedWidth = 0;
    consumedHeight = 0;
    for (Row row : rows)
    {
      consumedHeight += row.height;
      if(row.width > consumedWidth)
        consumedWidth = row.width;
    }
  }

  public Panel getPanel()
  {
    return panel;
  }

  private class Row
  {
    private LinkedList<Component> items;
    private int maxWidth;
    public int width;
    public int height;

    public Row(int maxWidth)
    {
      this.maxWidth = maxWidth;
      width = 0;
      height = 0;
      items = new LinkedList<Component>();
    }

    public void add(Component component)
    {
      items.add(component);
      width += component.getWidth();
      if(component.getHeight() > height)
        height = component.getHeight();
    }

    public boolean isEmpty()
    {
      return items.size() == 0;
    }

    public boolean fits(Component component)
    {      
      return (width + component.getWidth()) <= maxWidth;
    }

    public void layoutComponents(int x, int y)
    {
      for (Component component : items)
      {
        component.setLocation(x, y);
        x += component.getWidth();      
      }
    }
  }

  private class ScrollViewLayout implements LayoutManager
  {
    private JPanel view;

    private ScrollViewLayout(JPanel panel)
    {
      this.view = panel;
    }

    public void addLayoutComponent(String s, Component component)
    {
    }

    public void removeLayoutComponent(Component component)
    {
    }

    public Dimension preferredLayoutSize(Container container)
    {
      return view.getSize();
    }

    public Dimension minimumLayoutSize(Container container)
    {
      return view.getSize();
    }

    public void layoutContainer(Container container)
    {                                       
      String horizontalAlignment = panel.getStyle().getHorizontalAlignment();
      Aligner aligner = new Aligner(new limelight.ui.Rectangle(0, 0, view.getWidth(), view.getHeight()), horizontalAlignment, "top");
      layoutRows(aligner);
    }
  }
}


