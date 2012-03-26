package limelight.ui.model;

import limelight.ui.Panel;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class LayoutJobTest
{
  public FakeScene root;
  public MockParentPanel panel1;
  public MockParentPanel panel2;
  public MockParentPanel panel3;
  public FakeLayout rootLayout;
  public FakeLayout layout1;
  public FakeLayout layout2;
  public FakeLayout layout3;
  public LayoutJob job;
  public HashMap<Panel,Layout> panelsNeedingLayout;

  @Before
  public void setUp() throws Exception
  {
    root = new FakeScene();
    panel1 = new MockParentPanel();
    panel2 = new MockParentPanel();
    panel3 = new MockParentPanel();
    root.add(panel1);
    panel1.add(panel2);
    panel2.add(panel3);

    panelsNeedingLayout = new HashMap<Panel, Layout>();
  }

  @Test
  public void panelsAndLayoutsAreSorted() throws Exception
  {
    rootLayout = new FakeLayout(false);
    layout1 = new FakeLayout(false);
    layout2 = new FakeLayout(false);
    layout3 = new FakeLayout(false);

    panelsNeedingLayout.put(root, rootLayout);
    panelsNeedingLayout.put(panel3, layout3);
    panelsNeedingLayout.put(panel2, layout2);
    panelsNeedingLayout.put(panel1, layout1);

    job = LayoutJob.prepare(panelsNeedingLayout);

    assertEquals(root, job.getPanels().get(0));
    assertEquals(panel1, job.getPanels().get(1));
    assertEquals(panel2, job.getPanels().get(2));
    assertEquals(panel3, job.getPanels().get(3));

    assertEquals(rootLayout, job.getLayouts().get(0));
    assertEquals(layout1, job.getLayouts().get(1));
    assertEquals(layout2, job.getLayouts().get(2));
    assertEquals(layout3, job.getLayouts().get(3));
  }

  private FakeLayout prepareSingleLayout()
  {
    FakeLayout layout = new FakeLayout(false);
    panelsNeedingLayout.put(root, layout);
    panelsNeedingLayout.put(panel3, layout);
    panelsNeedingLayout.put(panel2, layout);
    panelsNeedingLayout.put(panel1, layout);
    job = LayoutJob.prepare(panelsNeedingLayout);
    return layout;
  }

  @Test
  public void expansion() throws Exception
  {
    FakeLayout layout = prepareSingleLayout();

    job.doExpansions();

    assertEquals(root, layout.expansions.get(0));
    assertEquals(panel1, layout.expansions.get(1));
    assertEquals(panel2, layout.expansions.get(2));
    assertEquals(panel3, layout.expansions.get(3));
  }

  @Test
  public void contractions() throws Exception
  {
    FakeLayout layout = prepareSingleLayout();

    job.doContractions();

    assertEquals(root, layout.contractions.get(3));
    assertEquals(panel2, layout.contractions.get(1));
    assertEquals(panel1, layout.contractions.get(2));
    assertEquals(panel3, layout.contractions.get(0));
  }

  @Test
  public void finalizations() throws Exception
  {
    FakeLayout layout = prepareSingleLayout();

    job.doFinalizations();

    assertEquals(root, layout.finalizations.get(0));
    assertEquals(panel1, layout.finalizations.get(1));
    assertEquals(panel2, layout.finalizations.get(2));
    assertEquals(panel3, layout.finalizations.get(3));
  }
}