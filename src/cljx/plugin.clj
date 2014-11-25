(ns cljx.plugin
  (:require [clojure.java.io :as io]))

(def ^:private cljx-version
  (-> (io/resource "META-INF/leiningen/com.taoensso.forks/cljx/project.clj")
       slurp
       read-string
       (nth 2)))

(assert (string? cljx-version)
        (str "Something went wrong, version of cljx is not a string: "
             cljx-version))

(defn middleware
  [project]
  (-> project
      (update-in [:dependencies]
                 (fnil into [])
                 [['com.taoensso.forks/cljx cljx-version]])
      (update-in [:repl-options :nrepl-middleware]
                 (fnil into [])
                 '[cljx.repl-middleware/wrap-cljx cemerick.piggieback/wrap-cljs-repl])))
