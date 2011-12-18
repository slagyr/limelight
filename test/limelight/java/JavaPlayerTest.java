package limelight.java;

import limelight.io.FakeFileSystem;
import limelight.model.CastingDirector;
import limelight.model.api.FakePlayerRecruiter;
import limelight.model.api.FakePropProxy;
import limelight.model.api.Player;
import limelight.ui.events.panel.CastEvent;
import limelight.ui.events.panel.MouseClickedEvent;
import limelight.ui.model.PropPanel;
import limelight.ui.model.ScenePanel;
import limelight.util.Opts;
import limelight.util.Util;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JavaPlayerTest
{
  public Object playerObj;
  public PropPanel prop;

  @Before
  public void setUp() throws Exception
  {
    FakeFileSystem fs = FakeFileSystem.installed();
    JavaProductionTest.writeSamplePlayerTo(fs.outputStream("/testProduction/classes/SamplePlayer.class"));
    playerObj = JavaPlayerRecruiter.resolvePlayer(new PlayerClassLoader("/testProduction/classes"), "SamplePlayer");
    prop = new PropPanel(new FakePropProxy());
  }

  @Test
  public void playerCanAddEvents() throws Exception
  {
    final Document doc = Xml.stringToDoc("<player class='SamplePlayer'><onMouseClicked>sampleAction</onMouseClicked></player>");
    final Player player = new JavaPlayer(playerObj, "/testProduction/aScene/players/foo.xml", doc.getDocumentElement(), "limelight.ui.events.panel.");

    player.cast(prop);

    assertEquals(1, prop.getEventHandler().getActions(MouseClickedEvent.class).size());
    new MouseClickedEvent(0, null, 1).dispatch(prop);
    assertEquals(1, playerObj.getClass().getField("invocations").get(playerObj));
  }

  @Test
  public void onCastEvent() throws Exception
  {
    final Document doc = Xml.stringToDoc("<player class='SamplePlayer'><onCast>sampleActionWithEvent</onCast></player>");
    final Player player = new JavaPlayer(playerObj, "/testProduction/aScene/players/foo.xml", doc.getDocumentElement(), "limelight.ui.events.panel.");

    player.cast(prop);

    assertEquals(0, prop.getEventHandler().getActions(CastEvent.class).size());
    assertEquals(1, playerObj.getClass().getField("invocations").get(playerObj));
    assertEquals(CastEvent.class, playerObj.getClass().getField("event").get(playerObj).getClass());
  }
}
