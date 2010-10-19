(ns limelight.test.scene
  (:use
    [limelight.scene]
    [lazytest.describe :only (describe testing it with)]
    [lazytest.context :only (fn-context)]))

(describe "Scene lineage"
  (it (some #{limelight.model.api.SceneProxy} (supers limelight.scene.Scene))))


