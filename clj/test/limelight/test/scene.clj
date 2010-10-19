(ns limelight.test.scene
  (:use
    [limelight.scene]
    [lazytest.describe :only (describe testing it with given)]
    [lazytest.context :only (fn-context)]))

(describe "Scene"
  (testing "lineage"
    (it "implements the API" (some #{limelight.model.api.SceneProxy} (supers limelight.scene.Scene))))

  (testing "creation"
    (given [scene (new-scene (hash-map :name "Bill"))]
      (it "has a peer" (not (nil? @(.peer scene))))
      (it "implements getPeer" (= @(.peer scene) (.getPeer scene)))
      (it "peer is PropPanel" (= limelight.ui.model.ScenePanel (type @(.peer scene)))))))


