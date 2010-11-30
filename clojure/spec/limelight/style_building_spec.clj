(ns limelight.style-building-spec
  (:use
    [speclj.core]
    [limelight.style-building]))

(describe "Style building"

  (it "build no styles with empty string"
    (let [styles (build-styles {} "")]
      (should= 0 (count styles))))

  (it "builds a new style"
    (let [styles (build-styles {} "(style 'hello {:background-color \"black\"})")]
      (should= "hello" (first (keys styles)))
      (should= "#000000ff" (.getBackgroundColor (first (vals styles))))))

  (it "builds multiple styles"
    (let [styles (build-styles {} "(style 'one {:x 1 :y 2})(style 'two {:x 3 :y 4})")]
      (should= 2 (count styles))
      (should= "1" (.getX (styles "one")))
      (should= "3" (.getX (styles "two")))))

  (it "allows overriding of styles"
    (let [styles (build-styles {} "(style 'one {:x 1 :y 2}) (style 'one {:x 0})")]
      (should= "0" (.getX (styles "one")))
      (should= "2" (.getY (styles "one")))))

  (it "with symbols as values"
    (let [styles (build-styles {} "(style 'one {:text-color :blue})")]
      (should= "#0000ffff" (.getTextColor (styles "one")))))
  )

