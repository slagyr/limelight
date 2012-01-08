;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.clojure.prop-building
  (:use
    [limelight.clojure.core]
    [limelight.clojure.prop :only (new-prop)]
    [limelight.clojure.util :only (read-src ->options)]))

(declare to-prop)
(declare *context*)

(defn- named? [x]
  (or
    (instance? clojure.lang.Named x)
    (string? x)))

(defn to-props [coll]
  (cond
    (or (not (coll? coll)) (empty? coll)) ()
    (named? (first coll)) (list (to-prop coll))
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

(defn- src->data [src]
  (if (string? src)
    (let [src-file (or (:source-file *context*) "[CODE]")]
      (read-src src-file (str "[" src "]")))
    (eval src)))

(defn- establish-root-path [root context]
  (if-let [prod (production root)]
    (if (nil? (:root-path context))
      (assoc context :root-path (path prod))
      context)
    context))

(defn build-props [root src & context]
  (let [context (->options context)
        context (establish-root-path root context)
        context (assoc context :root root)]
    (binding [*ns* (the-ns 'limelight.clojure.prop-building)
              *context* context]
      (let [prop-data (src->data src)
            props (to-props prop-data)]
        (add root props)
        root))))

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