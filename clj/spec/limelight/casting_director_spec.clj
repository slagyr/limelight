(ns limelight.casting-director-spec
  (:use
    [speclj.core]
    [limelight.common :only (add-props)]
    [limelight.casting-director]
    [limelight.scene :only (new-scene)]
    [limelight.prop :only (new-prop)]
    [limelight.production :only (new-production)])
  (:import
    [limelight.casting-director CastingDirector]))

(defn setup-files [fs options]
  (doall (map (fn [[filename content]] (.createTextFile fs filename content)) options)))

(defn actions-for [prop event]
  (-> prop
    (.peer)
    (deref)
    (.getEventHandler)
    (.getActions event)))

(describe "CastingDirector"
  (with production (new-production (limelight.model.FakeProduction. "MockProduction")))
  (with scene (new-scene {:path "root"}))
  (with prop (first (add-props @scene (new-prop {}))))
  (with casting-director (new-casting-director @scene))
  (with fs (limelight.io.FakeFileSystem/installed))
  (before
    (do
      (.setCastingDirector @(.peer @scene) (limelight.model.api.FakeCastingDirector.))
      (.setProduction @(.peer @scene) (.peer @production))))

  (it "can be created"
    (should= {} @(.cast @casting-director)))

  (it "has limelight lineage"
    (should (isa? CastingDirector limelight.model.api.CastingDirector)))

  (it "includes one player from scene"
    (setup-files @fs {"/root/players/test_player.clj" "(on-mouse-clicked [_])"})
    (.castPlayer @casting-director @prop "test-player")
    (should= 1 (count (actions-for @prop limelight.ui.events.panel.MouseClickedEvent))))

  (it "includes one player from production"
    (setup-files @fs {"/MockProduction/players/test_player.clj" "(on-mouse-clicked [_])"})
    (.castPlayer @casting-director @prop "test-player")
    (should= 1 (count (actions-for @prop limelight.ui.events.panel.MouseClickedEvent))))

  (for [[event name] {limelight.ui.events.panel.MouseClickedEvent "mouse-clicked"
                      limelight.ui.events.panel.MousePressedEvent "mouse-pressed"}]
    (it (str "handles " name " events")
      (setup-files @fs {"/MockProduction/players/test_player.clj" (str "(on-" name " [_] (def *message* \"" name "\"))")})
      (.castPlayer @casting-director @prop "test-player")
      (let [actions (actions-for @prop event)]
        (should= 1 (count actions))
        (should-not-throw (doseq [action actions] (.invoke action nil)))
        (should= name @(ns-resolve (@(.cast @casting-director) "test-player") '*message*)))))
  )

