(ns limelight.stage-spec
  (:use
    [speclj.core]
    [limelight.stage])
  (:import [limelight.stage Stage]))

(describe "Stage"
  (it "has limelight lineage"
    (should (isa? Stage limelight.model.api.StageProxy)))

  (it "can be created with all the right values"
    (let [stage (new-stage :theater "Bill" {})]
      (should= limelight.ui.model.FramedStage (type @(.peer stage)))
      (should= stage (.getProxy @(.peer stage)))
      (should= "Bill" (.getName @(.peer stage)))
      (should= :theater (.theater stage))))

  (it "uses the options map passed in constructor function"
    (let [stage (new-stage :theater "George" (hash-map :title "Dr. Mario"))]
      (should= "George" (.getName @(.peer stage)))
      (should= "Dr. Mario" (.getTitle @(.peer stage)))))
  )
