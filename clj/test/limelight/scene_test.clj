(ns limelight.scene-test
  (:use
    [limelight.scene]
    [lazytest.describe :only (describe testing it with given)]
    [lazytest.context :only (fn-context)])
  (:import [limelight.scene Scene]
           [limelight.casting-director CastingDirector]))

(describe "Scene"
  (testing "lineage"
    (it "implements the API" (isa? Scene limelight.model.api.SceneProxy)))

  (testing "creation"
    (given [scene (new-scene (hash-map :name "Bill"))]
      (it "has a peer" (not (nil? @(.peer scene))))
      (it "implements getPeer" (= @(.peer scene) (.getPeer scene)))
      (it "peer is PropPanel" (= limelight.ui.model.ScenePanel (type @(.peer scene))))
      (it "has a casting-director" (= CastingDirector (type @(.casting-director scene))))
      (it "peer has casting-director" (= @(.casting-director scene) (.getCastingDirector @(.peer scene)))))))



