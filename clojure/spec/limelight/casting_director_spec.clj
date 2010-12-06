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
  (after-all (limelight.Context/removeInstance))
  (before-all (limelight.Context/removeInstance))

  (with production (new-production (limelight.model.FakeProduction. "MockProduction")))
  (with scene (new-scene {:path "root"}))
  (with prop (first (add-props @scene (new-prop {}))))
  (with casting-director (new-casting-director @scene))
  (with fs (limelight.io.FakeFileSystem/installed))

  (before
    (do
      @fs ; load the file system
      (.setCastingDirector @(.peer @scene) (limelight.model.api.FakeCastingDirector.))
      (.setProduction @(.peer @scene) (.peer @production)))
    (.setStage @(.peer @scene) (.getDefaultStage (.peer (.getTheater @production)))))

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

  (for [[event name] {(limelight.ui.events.panel.MouseClickedEvent. 0 nil 0) "mouse-clicked"
                      (limelight.ui.events.panel.MousePressedEvent. 0 nil 0) "mouse-pressed"
                      (limelight.ui.events.panel.MouseReleasedEvent. 0 nil 0) "mouse-released"
                      (limelight.ui.events.panel.MouseMovedEvent. 0 nil 0) "mouse-moved"
                      (limelight.ui.events.panel.MouseDraggedEvent. 0 nil 0) "mouse-dragged"
                      (limelight.ui.events.panel.MouseEnteredEvent. 0 nil 0) "mouse-entered"
                      (limelight.ui.events.panel.MouseExitedEvent. 0 nil 0) "mouse-exited"
                      (limelight.ui.events.panel.MouseWheelEvent. 0, nil, 1, 0, 0, 0) "mouse-wheel"
                      (limelight.ui.events.panel.KeyPressedEvent. 0 0 0) "key-pressed"
                      (limelight.ui.events.panel.KeyReleasedEvent. 0 0 0) "key-released"
                      (limelight.ui.events.panel.CharTypedEvent. 0 \a) "char-typed"
                      (limelight.ui.events.panel.FocusGainedEvent.) "focus-gained"
                      (limelight.ui.events.panel.FocusLostEvent.) "focus-lost"
                      (limelight.ui.events.panel.ButtonPushedEvent.) "button-pushed"
                      (limelight.ui.events.panel.ValueChangedEvent.) "value-changed"
                      (limelight.ui.events.panel.SceneOpenedEvent.) "scene-opened"
                      }]
    (it (str "handles " name " events")
      (let [actions-before (actions-for @prop (class event))]
        (setup-files @fs {"/MockProduction/players/test_player.clj" (str "(on-" name " (def *message* \"" name "\"))")})
        (.castPlayer @casting-director @prop "test-player")
        (let [actions-after (actions-for @prop (class event))]
          (should= 1 (- (count actions-after) (count actions-before)))
          (should-not-throw (.dispatch event @(.peer @prop)))
          (should= name @(ns-resolve (@(.cast @casting-director) "test-player") '*message*))))))

  (it "handles on-cast events"
    (setup-files @fs {"/MockProduction/players/test_player.clj" (str "(on-cast [_] (def *message* \"casted\"))")})
    (.castPlayer @casting-director @prop "test-player")
    (should= "casted" @(ns-resolve (@(.cast @casting-director) "test-player") '*message*)))

  (it "the bindings are optional"
    (setup-files @fs {"/MockProduction/players/test_player.clj" (str "(on-cast (def *message* \"casted\"))")})
    (.castPlayer @casting-director @prop "test-player")
    (should= "casted" @(ns-resolve (@(.cast @casting-director) "test-player") '*message*)))

)

(run-specs)
