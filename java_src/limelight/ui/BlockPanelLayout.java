package limelight.ui;

public class BlockPanelLayout extends PanelLayout
{
  public BlockPanelLayout(ParentPanel panel)
  {
    super(panel);
  }

  public void doLayout()
	{
    Style style = panel.getBlock().getStyle();
    if(style.changed(Style.WIDTH) || style.changed(Style.HEIGHT))
      panel.snapToSize();

    if(panel.getChildren().size() == 0)
			return;

    doLayoutOnChildren();
    area = panel.getChildConsumableArea();
    reset();
    buildRows();

    if(consumedHeight > area.height || consumedWidth > area.width)
    {
      try
      {
        ScrollPanel scrollPanel = new ScrollPanel(panel.getChildren());
        panel.clearChildren();
        panel.add(scrollPanel);
        doLayout();
      }
      catch (SterilePanelException e)
      {
        e.printStackTrace();
      }
    }

    Aligner aligner = buildAligner(area);
    layoutRows(aligner);
  }
}
