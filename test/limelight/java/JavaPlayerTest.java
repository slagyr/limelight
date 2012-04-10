package limelight.java;

import limelight.io.FakeFileSystem;
import limelight.model.api.FakePropProxy;
import limelight.model.api.FakeSceneProxy;
import limelight.model.api.Player;
import limelight.ui.events.panel.CastEvent;
import limelight.ui.events.panel.MouseClickedEvent;
import limelight.ui.model.FakeScene;
import limelight.ui.model.PropPanel;
import limelight.ui.model.ScenePanel;
import limelight.util.Opts;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JavaPlayerTest
{
  public PropPanel prop;
  private Class<?> samplePlayerClass;

  @Before
  public void setUp() throws Exception
  {
    FakeFileSystem fs = FakeFileSystem.installed();
    JavaProductionTest.writeSamplePlayerTo(fs.outputStream("/testProduction/classes/SamplePlayer.class"));
    samplePlayerClass = new PlayerClassLoader("/testProduction/classes").loadClass("SamplePlayer");
    prop = new PropPanel(new FakePropProxy());
    new FakeScene().add(prop);
  }

  private Object lastSamplePlayer() throws NoSuchFieldException, IllegalAccessException
  {
    final Field lastInstanceField = samplePlayerClass.getField("lastInstance");
    return lastInstanceField.get(samplePlayerClass);
  }

  @Test
  public void playerCanAddEvents() throws Exception
  {
    final Document doc = Xml.stringToDoc("<player class='SamplePlayer'><onMouseClicked>sampleAction</onMouseClicked></player>");
    final Player player = new JavaPlayer("foo", "/testProduction/aScene/players/foo.xml", samplePlayerClass, doc.getDocumentElement(), "limelight.ui.events.panel.");

    Object playerObj = player.cast(prop);

    assertEquals("SamplePlayer", playerObj.getClass().getName());
    assertEquals(1, prop.getEventHandler().getActions(MouseClickedEvent.class).size());
    new MouseClickedEvent(0, null, 1).dispatch(prop);
    assertEquals(1, samplePlayerClass.getField("invocations").get(lastSamplePlayer()));
  }

  @Test
  public void onCastEvent() throws Exception
  {
    final Document doc = Xml.stringToDoc("<player class='SamplePlayer'><onCast>sampleActionWithEvent</onCast></player>");
    final Player player = new JavaPlayer("foo", "/testProduction/aScene/players/foo.xml", samplePlayerClass, doc.getDocumentElement(), "limelight.ui.events.panel.");

    player.cast(prop);

    assertEquals(0, prop.getEventHandler().getActions(CastEvent.class).size());
    assertEquals(1, samplePlayerClass.getField("invocations").get(lastSamplePlayer()));
    assertEquals(CastEvent.class, samplePlayerClass.getField("event").get(lastSamplePlayer()).getClass());
  }

  @Test
  public void applyOptions() throws Exception
  {
    final Thing thing = new Thing();
    prop.getBackstage().put("thing", thing);
    final Player player = new JavaPlayer("thing", "path", null, null, null);

    Map<String, Object> result = player.applyOptions(prop, Opts.with("value", 1234));
    assertEquals(0, result.size());
    assertEquals(1234, thing.value);
  }

  public static class Thing
  {
    public Object value;

    public void setValue(Object value)
    {
      this.value = value;
    }
  }
}
