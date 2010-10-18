(ns limelight.test.stage
  (:use
    [limelight.stage]
    [lazytest.describe :only (describe testing it with)]
    [lazytest.context :only (fn-context)]))

(describe "Stage lineage"
  (it (some #{limelight.model.api.StageProxy} (supers limelight.stage.Stage))))


