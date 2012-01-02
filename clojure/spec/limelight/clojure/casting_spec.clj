;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.clojure.casting-spec
  (:use
    [speclj.core]
    [limelight.clojure.spec-helper]
    [limelight.clojure.core :only (add-props)]
    [limelight.clojure.casting]
    [limelight.clojure.scene :only (new-scene)]
    [limelight.clojure.prop :only (new-prop)]
    [limelight.clojure.production :only (new-production)])
  (:import
    [limelight.clojure.casting PlayerRecruiter]))

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
  (with player-recruiter (new-player-recruiter @scene))
  (with fs (limelight.io.FakeFileSystem/installed))

  (before
    (do
      @fs ; load the file system
      (.setPlayerRecruiter @(.peer @scene) (limelight.model.api.FakePlayerRecruiter.))
      (.setProduction @(.peer @scene) (.peer @production)))
    (.setStage @(.peer @scene) (.getDefaultStage (.peer (.getTheater @production)))))

  (unless-headless

    (it "can be created"
      (should= {} @(.cast @player-recruiter)))

    (it "has limelight lineage"
      (should (isa? PlayerRecruiter limelight.model.api.PlayerRecruiter)))

    (it "creates players with the correct path and ns"
      (setup-files @fs {"/root/players/test_player.clj" "(on-mouse-clicked [_])"})
      (let [player (.recruitPlayer @player-recruiter "test-player" "/root/players")]
        (should= "test-player" (.getName player))
        (should= "/root/players/test_player.clj" (.getPath player))))

    (it "includes one player from scene"
      (setup-files @fs {"/root/players/test_player.clj" "(on-mouse-clicked [_])"})
      (let [player (.recruitPlayer @player-recruiter "test-player" "/root/players")]
        (.cast player @(.peer @prop))
        (should= 1 (count (actions-for @prop limelight.ui.events.panel.MouseClickedEvent)))))

    (it "includes one player from production"
      (setup-files @fs {"/MockProduction/players/test_player.clj" "(on-mouse-clicked [_])"})
      (.cast (.recruitPlayer @player-recruiter "test-player" "/MockProduction/players") @(.peer @prop))
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
                        (limelight.ui.events.panel.IlluminatedEvent. nil) "illuminated"
                        (limelight.ui.events.panel.SceneOpenedEvent.) "scene-opened"
                        }]
      (it (str "handles " name " events")
        (let [actions-before (actions-for @prop (class event))]
          (setup-files @fs {"/MockProduction/players/test_player.clj" (str "(on-" name " (def *message* \"" name "\"))")})
          (.cast (.recruitPlayer @player-recruiter "test-player" "/MockProduction/players") @(.peer @prop))
          (let [actions-after (actions-for @prop (class event))]
            (should= 1 (- (count actions-after) (count actions-before)))
            (should-not-throw (.dispatch event @(.peer @prop)))
            (should= name @(ns-resolve (@(.cast @player-recruiter) "test-player") '*message*))))))

    (it "handles on-cast events"
      (setup-files @fs {"/MockProduction/players/test_player.clj" (str "(on-cast [_] (def *message* \"casted\"))")})
      (.cast (.recruitPlayer @player-recruiter "test-player" "/MockProduction/players") @(.peer @prop))
      (should= "casted" @(ns-resolve (@(.cast @player-recruiter) "test-player") '*message*)))

    (it "the bindings are optional"
      (setup-files @fs {"/MockProduction/players/test_player.clj" (str "(on-cast (def *message* \"casted\"))")})
      (.cast (.recruitPlayer @player-recruiter "test-player" "/MockProduction/players") @(.peer @prop))
      (should= "casted" @(ns-resolve (@(.cast @player-recruiter) "test-player") '*message*)))

    )
  )

(run-specs :stacktrace true)
