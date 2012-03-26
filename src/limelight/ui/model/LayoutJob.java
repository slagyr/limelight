package limelight.ui.model;

import limelight.ui.Panel;
import limelight.ui.PanelUtil;
import limelight.util.Pair;

import java.util.*;

public class LayoutJob
{
  private List<Panel> panels;
  private List<Layout> layouts;

  private LayoutJob(ArrayList<Panel> panels, ArrayList<Layout> layouts)
  {
    this.panels = panels;
    this.layouts = layouts;
  }

  public static LayoutJob prepare(Map<Panel, Layout> panelLayoutMap)
  {
    ArrayList<Pair<Panel, Integer>> sortList = new ArrayList<Pair<Panel, Integer>>();
    for(Panel panel : panelLayoutMap.keySet())
      sortList.add(new Pair<Panel, Integer>(panel, PanelUtil.depthOf(panel)));
    Collections.sort(sortList, LayoutComparator.instance);

    ArrayList<Panel> panels = new ArrayList<Panel>();
    ArrayList<Layout> layouts = new ArrayList<Layout>();

    for(Pair<Panel, Integer> pair : sortList)
    {
      panels.add(pair.a);
      layouts.add(panelLayoutMap.get(pair.a));
    }
    return new LayoutJob(panels, layouts);
  }

  public List<Panel> getPanels()
  {
    return panels;
  }

  public List<Layout> getLayouts()
  {
    return layouts;
  }

  public void doExpansions()
  {
    for(int i = 0; i < panels.size(); i++)
      layouts.get(i).doExpansion(panels.get(i));
  }

  public void doContractions()
  {
    for(int i = panels.size() - 1; i >= 0; i--)
      layouts.get(i).doContraction(panels.get(i));
  }

  public void doFinalizations()
  {
    for(int i = 0; i < panels.size(); i++)
      layouts.get(i).doFinalization(panels.get(i));
  }

  public static void go(HashMap<Panel, Layout> panelsNeedingLayout)
  {
    LayoutJob job = prepare(panelsNeedingLayout);
    job.doLayouts();
  }

  private void doLayouts()
  {
    doExpansions();
    doContractions();
    doFinalizations();
  }

  private static class LayoutComparator implements Comparator<Pair<Panel, Integer>>
  {
    public static LayoutComparator instance = new LayoutComparator();

    public int compare(Pair<Panel, Integer> a, Pair<Panel, Integer> b)
    {
      return a.b.compareTo(b.b);
    }
  }
}
