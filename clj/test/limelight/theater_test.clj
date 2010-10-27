(ns limelight.theater-test
  (:use
    [limelight.theater]
    [lazytest.describe :only (describe testing it with given)]
    [lazytest.context :only (fn-context)])
  (:import [limelight.theater Theater]))

(describe "Theater"
  (testing "lineage"
    (it (isa? Theater limelight.model.api.TheaterProxy)))
  (testing "construction"
    (given [theater (Theater. :peer :production)]
      (it "sets the peer" (= :peer (.peer theater)))
      (it "sets the production" (= :production (.production theater)))))
  (testing "building a stage"
    (given [theater (Theater. :peer :production)
            stage   (.buildStage theater "Limelight" {})]
      (it "uses the right type of Stage"
        (isa? (type stage) limelight.model.Stage))
      (it "passes the right name and options along"
        (= "Limelight" (.getName stage))))))


