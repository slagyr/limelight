package limelight.ui;

public class ScrollViewLayout extends PanelLayout
{
  public ScrollViewLayout(ParentPanel panel)
  {
    super(panel);
  }

  public void doLayout()
  {
    panel.snapToSize();

    if(panel.getChildren().size() == 0)
			return;

    doLayoutOnChildren();
    area = panel.getChildConsumableArea();
    reset();
    buildRows();

    Aligner aligner = buildAligner(area);
    layoutRows(aligner);
  }

  public int getConsumedWidth()
  {
    return consumedWidth;
  }

  public int getConsumedHeight()
  {
    return consumedHeight;
  }
}
