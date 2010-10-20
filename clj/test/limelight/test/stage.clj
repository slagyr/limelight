(ns limelight.test.stage
  (:use
    [limelight.stage]
    [lazytest.describe :only (describe testing it with given)]
    [lazytest.context :only (fn-context)]))

(describe "Stage"
  (testing "lineage"
    (it "implements the API" (isa? limelight.stage.Stage limelight.model.api.StageProxy)))
  (testing "creation"
    (given [stage (new-stage :theater "Bill" (hash-map :options nil))]
      (it "makes a peer" (= limelight.ui.model.FramedStage (type @(.peer stage))))
      (it "peer has stage" (= stage (.getProxy @(.peer stage))))
      (it "peer is named" (= "Bill" (.getName @(.peer stage))))
      (it "has theater" (= :theater (.theater stage))))))


