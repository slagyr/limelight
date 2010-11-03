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
        _ (.setCastingDirector (.peer production) (limelight.model.api.FakeCastingDirector.))
        scene (new-scene {:path "root"})
        _ (.setProduction @(.peer scene) (.peer production))
        prop (add scene (new-prop {}))]
    prop))

(defn setup-fs [options]
  (let [fs (limelight.io.FakeFileSystem/installed)]
    (doall (map (fn [[filename content]] (println filename ": " content) (.createTextFile fs filename content)) options))
    fs))

(describe "CastingDirector"

  (testing "lineage"
    (it "inherits from API" (isa? CastingDirector limelight.model.api.CastingDirector)))

  (testing "includes one player from scene"
    (given [fs (setup-fs {"/root/players/test_player.clj" "(on-mouse-clicked (fn [_]))"})
            prop (setup-prop {})
            casting-director (CastingDirector.)
            _ (.castPlayer casting-director prop "test-player")]
      (it (= 1 (-> prop
        (.peer)
        (deref)
        (.getEventHandler)
        (.getActions limelight.ui.events.panel.MouseClickedEvent)
        (count))))))

  (testing "includes one player from production"
    (given [fs (setup-fs {"/MockProduction/players/test_player.clj" "(on-mouse-clicked (fn [_]))"})
            prop (setup-prop {})
            casting-director (CastingDirector.)
            _ (.castPlayer casting-director prop "test-player")]
      (it (= 1 (-> prop
        (.peer)
        (deref)
        (.getEventHandler)
        (.getActions limelight.ui.events.panel.MouseClickedEvent)
        (count))))))
  )


