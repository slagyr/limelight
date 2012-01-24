;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.clojure.style-building-spec
  (:use
    [speclj.core]
    [limelight.clojure.spec-helper]
    [limelight.clojure.style-building]))

(describe "Style building"

  (it "build no styles with empty string"
    (let [styles (build-styles {} "" "styles.clj" *ns*)]
      (should= 0 (count styles))))

  (it "builds a new style"
    (let [styles (build-styles {} "['hello {:background-color \"black\"}]" "styles.clj" *ns* *ns*)]
      (should= "hello" (first (keys styles)))
      (should= "#000000ff" (.getBackgroundColor (first (vals styles))))))

  (it "builds multiple styles"
    (let [styles (build-styles {} "['one {:x 1 :y 2}] ['two {:x 3 :y 4}]" "styles.clj" *ns*)]
      (should= 2 (count styles))
      (should= "1" (.getX (styles "one")))
      (should= "3" (.getX (styles "two")))))

  (it "allows overriding of styles"
    (let [styles (build-styles {} "['one {:x 1 :y 2}] ['one {:x 0}]" "styles.clj" *ns*)]
      (should= "0" (.getX (styles "one")))
      (should= "2" (.getY (styles "one")))))

  (it "with symbols as values"
    (let [styles (build-styles {} "['one {:text-color :blue}]" "styles.clj" *ns*)]
      (should= "#0000ffff" (.getTextColor (styles "one")))))

  (it "can use any nameable as the first parameter"
    (let [styles (build-styles {} "[\"one\" {:text-color :blue}]
                                   [:two {:text-color :red}]" "styles.clj" *ns*)]
      (should= "#0000ffff" (.getTextColor (styles "one")))
      (should= "#ff0000ff" (.getTextColor (styles "two")))))

  (it "with curly braces is options"
    (let [styles (build-styles {} "[:one :text-color :blue]" "styles.clj" *ns*)]
      (should= "#0000ffff" (.getTextColor (styles "one")))))

  (it "allows styles to extend other styles"
    (let [styles (build-styles {} "[:one :width 100] [:two :extends :one :height 200]" "styles.clj" *ns*)
          one (styles "one")
          two (styles "two")]
      (should= 2 (count styles))
      (should= true (.hasExtension two one))
      (should= "200" (.getHeight two))
      (should= "100" (.getWidth two))))

  (it "errors when extending missing style"
    (try
      (build-styles {} "[:two :extends :missing :height 200]" "styles.clj" *ns*)
      (should-fail "Exception expected")
      (catch limelight.LimelightException e
        (println "e: " e)
        (should= true (.contains (.getMessage e) "Can't extend missing style: 'missing'")))))

  (it "allows styles to extend multiple styles"
    (let [src "[:one :width 100] [:two :height 200] [:three :extends [:one :two]]"
          styles (build-styles {} src "styles.clj" *ns*)
          one (styles "one")
          two (styles "two")
          three (styles "three")]
      (should= 3 (count styles))
      (should= true (.hasExtension three one))
      (should= true (.hasExtension three two))
      (should= "200" (.getHeight three))
      (should= "100" (.getWidth three))))

  (it "extends styles form the extensable-styles map"
    (let [extendable (build-styles {} "[:one :width 100]", "extensions.clj" *ns*)
          styles (build-styles {} "[:two :extends [:one] :height 200]" "styles.clj" *ns* extendable)
          one (extendable "one")
          two (styles "two")]
      (should= 1 (count styles))
      (should= true (.hasExtension two one))
      (should= "200" (.getHeight two))
      (should= "100" (.getWidth two))))

  (it "allows hover styles"
    (let [styles (build-styles {} "[:root :width 100 :hover {:width 50}]", "styles.clj" *ns*)]
      (should= 2 (count styles))
      (should= "100" (.getWidth (styles "root")))
      (should= "50" (.getWidth (styles "root.hover")))))
  )


(run-specs)