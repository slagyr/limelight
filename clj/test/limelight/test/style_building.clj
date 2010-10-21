(ns limelight.test.style-building
  (:use
    [limelight.style-building]
    [lazytest.describe :only (describe testing it with given before using)]
    [lazytest.context :only (fn-context)]))

(describe "Style building"
  (testing "with no styles"
    (given [styles (build-styles {} "")]
      (it "has no styles" (= 0 (count styles)))))

  (testing "with a new style"
    (given [styles (build-styles {} "(style 'hello {:background-color :black})")]
      (it "has the right name"
        (= "hello" (first (keys styles))))
      (it "has the right attributes"
        (= "#00000000" (.getBackgroundColor (first (vals styles)))))))

  (testing "with two styles"
    (given [styles (build-styles {} "(style 'one {:x 1 :y 2})(style 'two {:x 3 :y 4})")]
      (it "has 2 styles" (= 2 (count styles)))
      (it "one: x = 1" (= "1" (.getX (styles "one"))))
      (it "two: x = 3" (= "3" (.getX (styles "two"))))))

  (testing "overriding styles"
    (given [styles (build-styles {} "(style 'one {:x 1 :y 2}) (style 'one {:x 0})")]
      (it "last one wins"
        (= "0" (.getX (styles "one"))))
      (it "keeps other attributes"
        (= "2" (.getY (styles "one")))))))


