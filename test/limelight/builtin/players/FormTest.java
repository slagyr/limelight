package limelight.builtin.players;

import limelight.model.api.FakePropProxy;
import limelight.model.api.FakeSceneProxy;
import limelight.ui.events.panel.CastEvent;
import limelight.ui.model.PropPanel;
import limelight.ui.model.ScenePanel;
import limelight.util.Opts;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FormTest
{
  private Form form;
  private PropPanel propPanel;

  @Before
  public void setUp() throws Exception
  {
    form = new Form();
    propPanel = new PropPanel(new FakePropProxy());
    new ScenePanel(new FakeSceneProxy()).add(propPanel);
    form.install(new CastEvent(propPanel));
  }

  @Test
  public void whenEmpty() throws Exception
  {
    assertEquals(new Opts(), form.getData());
  }
//
//  @Test
//  public void withTextFieldAsChild() throws Exception
//  {
//    final PropPanel child = new PropPanel(new FakePropProxy());
//    child.addOptions(Opts.with("id", "field1"));
//    final TextBox textBox = new TextBox();
//    textBox.install(new CastEvent(child));
//    child.setText("value1");
//
//    assertEquals(Opts.with("field1", "value1"), form.getData());
//  }
}
