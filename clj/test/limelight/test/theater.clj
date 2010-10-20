(ns limelight.test.theater
  (:use
    [limelight.theater]
    [lazytest.describe :only (describe testing it with given)]
    [lazytest.context :only (fn-context)]))

(describe "Theater"
  (testing "lineage"
    (it (isa? limelight.theater.Theater limelight.model.api.TheaterProxy)))
  (testing "construction"
    (given [theater (limelight.theater.Theater. :peer :production)]
      (it "sets the peer" (= :peer (.peer theater)))
      (it "sets the production" (= :production (.production theater))))))


