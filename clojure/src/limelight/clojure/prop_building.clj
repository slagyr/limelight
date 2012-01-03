;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.clojure.prop-building
  (:use
    [limelight.clojure.core]
    [limelight.clojure.prop :only (new-prop)]
    [limelight.clojure.util :only (read-src ->options)]))

(declare to-prop)
(declare *context*)

(defn to-props [coll]
  (cond
    (or (not (coll? coll)) (empty? coll)) ()
    (keyword? (first coll)) (list (to-prop coll))
    (coll? (first coll)) (reduce #(into %1 (to-props %2)) [] coll)
    :else (throw (Exception. (str "Don't know how to create props from:" coll)))))

(defn- to-prop [data]
  (let [name (name (first data))
        options (if (map? (second data)) (second data) nil)
        child-data (if options (rest (rest data)) (rest data))
        options (assoc options :name name)
        prop (new-prop (limelight.util.Opts. options))]
    (when (seq child-data)
      (add prop (to-props child-data)))
    prop))

(defn build-props [root src path & context]
  (binding [*ns* (the-ns 'limelight.clojure.prop-building)
            *context* (->options context)]
    (let [prop-data (read-src path (str "[" src "]"))
          props (to-props prop-data)]
      (add root props)
      root)))

(defn- extract-root-path []
  (if-let [path (:root-path *context*)]
    path
    (throw (limelight.LimelightException. "Can't install props without a :root-path"))))

(defn install [path & context]
  (let [fs (limelight.Context/fs)
        root-path (extract-root-path)
        include-path (.pathTo fs root-path path)
        src (.readTextFile fs include-path)
        extra-context (->options context)
        new-context (merge *context* extra-context)]
    (binding [*context* new-context]
      (read-src include-path (str "(list " src ")")))))