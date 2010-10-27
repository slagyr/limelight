(ns limelight.stage-test
  (:use
    [limelight.stage]
    [lazytest.describe :only (describe testing it with given)]
    [lazytest.context :only (fn-context)])
  (:import [limelight.stage Stage]))

(describe "Stage"
  (testing "lineage"
    (it "implements the API" (isa? Stage limelight.model.api.StageProxy)))
  (testing "creation"
    (given [stage (new-stage :theater "Bill" (hash-map :options nil))]
      (it "makes a peer" (= limelight.ui.model.FramedStage (type @(.peer stage))))
      (it "peer has stage" (= stage (.getProxy @(.peer stage))))
      (it "peer is named" (= "Bill" (.getName @(.peer stage))))
      (it "has theater" (= :theater (.theater stage))))
    (given [stage (new-stage :theater "George" (hash-map :title "Dr. Mario"))]
      (it "peer is named" (= "George" (.getName @(.peer stage))))
      (it "applies options" (= "Dr. Mario" (.getTitle @(.peer stage)))))))
