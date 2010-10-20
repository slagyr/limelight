(ns limelight.test.theater
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
      (it "sets the production" (= :production (.production theater))))))


