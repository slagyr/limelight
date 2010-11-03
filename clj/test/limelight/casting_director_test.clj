(ns limelight.casting-director-test
  (:use
    [limelight.casting-director]
    [limelight.scene :only (new-scene)]
    [limelight.prop :only (new-prop add)]
    [limelight.production :only (new-production)]
    [limelight.test-help :only (fake-fs *fs*)]
    [lazytest.describe :only (describe testing it with given)]
    [lazytest.context :only (fn-context)])
  (:import [limelight.casting-director CastingDirector]))

(defn setup-prop [options]
  (let [production (new-production (limelight.model.FakeProduction. "MockProduction"))
        scene (new-scene {:path "root"})
        _ (.setCastingDirector @(.peer scene) (limelight.model.api.FakeCastingDirector.))
        _ (.setProduction @(.peer scene) (.peer production))
        prop (add scene (new-prop {}))]
    [prop scene]))

(defn setup-fs [options]
  (let [fs (limelight.io.FakeFileSystem/installed)]
    (doall (map (fn [[filename content]] (.createTextFile fs filename content)) options))
    fs))

(describe "CastingDirector"

  (testing "creation"
    (given [scene (new-scene {}) 
            casting-director (new-casting-director scene)]
      (it "has en empty cast" (= {} @(.cast casting-director)))))

  (testing "lineage"
    (it "inherits from API" (isa? CastingDirector limelight.model.api.CastingDirector)))

  (testing "includes one player from scene"
    (given [fs (setup-fs {"/root/players/test_player.clj" "(on-mouse-clicked (fn [_]))"})
            [prop scene] (setup-prop {})
            casting-director (new-casting-director scene)
            _ (.castPlayer casting-director prop "test-player")]
      (it (= 1 (-> prop
        (.peer)
        (deref)
        (.getEventHandler)
        (.getActions limelight.ui.events.panel.MouseClickedEvent)
        (count))))))

  (testing "includes one player from production"
    (given [fs (setup-fs {"/MockProduction/players/test_player.clj" "(on-mouse-clicked (fn [_]))"})
            [prop scene] (setup-prop {})
            casting-director (new-casting-director scene)
            _ (.castPlayer casting-director prop "test-player")]
      (it (= 1 (-> prop
        (.peer)
        (deref)
        (.getEventHandler)
        (.getActions limelight.ui.events.panel.MouseClickedEvent)
        (count))))))
  )
