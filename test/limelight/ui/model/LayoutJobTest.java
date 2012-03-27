package limelight.ui.model;

import org.junit.Before;
import org.junit.Test;

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
  public Layouts job;

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
  }

  private FakeLayout prepareSingleLayout()
  {
    FakeLayout layout = new FakeLayout(true);
    root.markAsNeedingLayout(layout);
    panel3.markAsNeedingLayout(layout);
    panel1.markAsNeedingLayout(layout);
    panel2.markAsNeedingLayout(layout);
    return layout;
  }

  @Test
  public void expansion() throws Exception
  {
    FakeLayout layout = prepareSingleLayout();

    Layouts.on(root);

    assertEquals(root, layout.expansions.get(0));
    assertEquals(panel1, layout.expansions.get(1));
    assertEquals(panel2, layout.expansions.get(2));
    assertEquals(panel3, layout.expansions.get(3));
  }

  @Test
  public void contractions() throws Exception
  {
    FakeLayout layout = prepareSingleLayout();

    Layouts.on(root);

    assertEquals(root, layout.contractions.get(3));
    assertEquals(panel2, layout.contractions.get(1));
    assertEquals(panel1, layout.contractions.get(2));
    assertEquals(panel3, layout.contractions.get(0));
  }

  @Test
  public void finalizations() throws Exception
  {
    FakeLayout layout = prepareSingleLayout();

    Layouts.on(root);

    assertEquals(root, layout.finalizations.get(3));
    assertEquals(panel1, layout.finalizations.get(2));
    assertEquals(panel2, layout.finalizations.get(1));
    assertEquals(panel3, layout.finalizations.get(0));
  }
}