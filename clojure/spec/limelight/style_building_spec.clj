(ns limelight.style-building-spec
  (:use
    [speclj.core]
    [limelight.style-building]))

(describe "Style building"

  (it "build no styles with empty string"
    (let [styles (build-styles {} "" "styles.clj")]
      (should= 0 (count styles))))

  (it "builds a new style"
    (let [styles (build-styles {} "(style 'hello {:background-color \"black\"})" "styles.clj")]
      (should= "hello" (first (keys styles)))
      (should= "#000000ff" (.getBackgroundColor (first (vals styles))))))

  (it "builds multiple styles"
    (let [styles (build-styles {} "(style 'one {:x 1 :y 2})(style 'two {:x 3 :y 4})" "styles.clj")]
      (should= 2 (count styles))
      (should= "1" (.getX (styles "one")))
      (should= "3" (.getX (styles "two")))))

  (it "allows overriding of styles"
    (let [styles (build-styles {} "(style 'one {:x 1 :y 2}) (style 'one {:x 0})" "styles.clj")]
      (should= "0" (.getX (styles "one")))
      (should= "2" (.getY (styles "one")))))

  (it "with symbols as values"
    (let [styles (build-styles {} "(style 'one {:text-color :blue})" "styles.clj")]
      (should= "#0000ffff" (.getTextColor (styles "one")))))

  (it "can use any nameable as the first parameter"
    (let [styles (build-styles {} "(style \"one\" {:text-color :blue})
                                   (style :two {:text-color :red})" "styles.clj")]
      (should= "#0000ffff" (.getTextColor (styles "one")))
      (should= "#ff0000ff" (.getTextColor (styles "two")))))

  (it "with curly braces is options"
    (let [styles (build-styles {} "(style :one :text-color :blue)" "styles.clj")]
      (should= "#0000ffff" (.getTextColor (styles "one")))))
  )

(run-specs)

