(ns limelight.casting-director-test
  (:use
    [limelight.common]
    [limelight.casting-director]
    [limelight.scene :only (new-scene)]
    [limelight.prop :only (new-prop)]
    [limelight.production :only (new-production)]
    [limelight.test-help :only (fake-fs *fs*)]
    [lazytest.describe :only (describe testing it with given do-it)]
    [lazytest.context :only (fn-context)])
  (:import [limelight.casting-director CastingDirector]))

(defn setup-scene []
  (let [production (new-production (limelight.model.FakeProduction. "MockProduction"))
        scene (new-scene {:path "root"})
        _ (.setCastingDirector @(.peer scene) (limelight.model.api.FakeCastingDirector.))
        _ (.setProduction @(.peer scene) (.peer production))]
    scene))


(defn setup-prop []
  (let [scene (setup-scene)
        [prop] (add-props scene (new-prop {}))]
    [prop scene]))

(defn setup-fs [options]
  (let [fs (limelight.io.FakeFileSystem/installed)]
    (doall (map (fn [[filename content]] (.createTextFile fs filename content)) options))
    fs))

(defn actions-for [prop event]
  (-> prop
    (.peer)
    (deref)
    (.getEventHandler)
    (.getActions event)))

(describe "CastingDirector"

  (testing "creation"
    (given [scene (new-scene {})
            casting-director (new-casting-director scene)]
      (it "has en empty cast" (= {} @(.cast casting-director)))))

  (testing "lineage"
    (it "inherits from API" (isa? CastingDirector limelight.model.api.CastingDirector)))

  (testing "includes one player from scene"
    (given [fs (setup-fs {"/root/players/test_player.clj" "(on-mouse-clicked (fn [_]))"})
            [prop scene] (setup-prop)
            casting-director (new-casting-director scene)
            _ (.castPlayer casting-director prop "test-player")]
      (it (= 1 (count (actions-for prop limelight.ui.events.panel.MouseClickedEvent))))))

  (testing "includes one player from production"
    (given [fs (setup-fs {"/MockProduction/players/test_player.clj" "(on-mouse-clicked (fn [_]))"})
            [prop scene] (setup-prop)
            casting-director (new-casting-director scene)
            _ (.castPlayer casting-director prop "test-player")]
      (it (= 1 (count (actions-for prop limelight.ui.events.panel.MouseClickedEvent)))))))

;(doseq [[event name] {limelight.ui.events.panel.MouseClickedEvent "mouse-clicked"
;                      limelight.ui.events.panel.MousePressedEvent "mouse-pressed"}]
  (describe "Player Events"
    (given [fs (setup-fs {})
            scene (setup-scene)
            casting-director (new-casting-director scene)]
      (testing "mouse clicked"
        (given [_ (.createTextFile fs "/MockProduction/players/test_player.clj" "(on-mouse-clicked (fn [_] (def *message* \"mouse-clicked\")))")
                [prop] (add-props scene (new-prop {}))
                _ (.castPlayer casting-director prop "test-player")
                actions (actions-for prop limelight.ui.events.panel.MouseClickedEvent)]
          (it "has a mouse-clicked action" (= 1 (count actions)))
          (it "executes actions without exception" (try (doseq [action actions] (.invoke action nil)) true (catch Exception e false)))
          (it "is the action we added" (= "mouse-clicked" @(ns-resolve (@(.cast casting-director) "test-player") '*message*)))))))
;)
