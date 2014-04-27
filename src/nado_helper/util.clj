(ns nado-helper.util
 (:gen-class))

(def to-string (memfn toString))

(defn to-string-array [l] (into-array string (map to-string l)))

(defn to-string-2d [d] (map to-string-array d))



