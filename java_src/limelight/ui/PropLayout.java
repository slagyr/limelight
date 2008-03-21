package limelight.ui;


import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

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

  private static long totalDuration = 0;
  private static int calls = 0;

  public PropLayout(Panel panel)
  {
    this.panel = panel;
    rows = new LinkedList<Row>();
    inScrollMode = false;
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
    panel.setSize(panel.getMaximumSize());

    reset();
    buildRows();

    //TODO Hack - March 17, 08
    if("auto".equals(panel.getStyle().getHeight()))
      area = new Rectangle(area.x, area.y, area.width, consumedHeight);

    if(!inScrollMode && (consumedHeight > area.height || consumedWidth > area.width))
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
      doNormalLayout();
      collapseAutoDimensions();
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
      panel.setSize(consumedWidth, consumedHeight);
    else if(hasAutoWidth)
      panel.setSize(consumedWidth, panel.getHeight());
    else if(hasAutoHeight)
      panel.setSize(panel.getWidth(), consumedHeight);
  }

  private void doNormalLayout()
  {
    Aligner aligner = buildAligner(panel.getRectangleInsidePadding());
    aligner.addConsumedHeight(consumedHeight);
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
      if (!currentRow.isEmpty() && !currentRow.fits(component))
        newRow();
      currentRow.add(component);
    }
    calculateConsumedDimentions();
  }

  private Aligner buildAligner(limelight.ui.Rectangle rectangle)
  {
    return new Aligner(rectangle, panel.getStyle().getHorizontalAlignment(), panel.getStyle().getVerticalAlignment());
  }

  private void reset()
	{
    area = panel.getRectangleInsidePadding();
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
      int y = aligner.startingY();
      for(Row row : rows)
      {
        int x = aligner.startingX(row.width);
        row.layoutComponents(x, y);
        y += row.height;
      }
    }
  }
}


