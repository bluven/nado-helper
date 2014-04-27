(ns nado-helper.core
 [:require [clj-redis.client :as redis]
           [nado-helper.display :as display]]
 [:import com.bethecoder.ascii_table.ASCIITable]
 (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(def ^:dynamic *db* (redis/init))

(def ^:dynamic *uid* 1)

(defn init
    [ & {:keys [url uid ] :or {url "redis://192.168.0.50:6380" uid *uid* }}]
    (def ^:dynamic *db* (redis/init {:url url}))
    (def ^:dynamic *uid* uid)
    (println url *uid*))

(defn init-job [] (init :url "redis://192.168.0.50:6380" :uid 10006))

(defn print-dict [d]
    (dorun
      (map #(println % (.get d %))
       (keys d))))


(defn print-user
    ([] (print-user *uid*))
    ( [id] (print-dict (redis/hgetall *db*  (format "nadouser:%s:userinfo" id)))))

(defn get-all [key]
    (if (redis/exists *db* key)
      (redis/hgetall *db* key)))

(defn print-by-key [key]
    (println (format "----- %s -----"key))
    (print-dict (get-all key))
    (println "------------------------"))

(declare print-slots)
(defn show-item
    ([pid] (show-item *uid* pid))
    ([uid pid]
        (print-by-key (format "nadouser:%s:item:%s:iteminfo" uid pid))
        (print-slots 1)
        (print-slots 2)))

(defn show-items []
    (let [
            keys-format (format "nadouser:%s:item:*" *uid*)
            keys (redis/keys *db* keys-format)
        ]
        (dorun
            (map print-by-key keys))))

(def slots-keys {1 "nadouser:%s:propslots" 2 "nadouser:%s:runeslots"})

(defn print-slots
 [item-type]
 (let [key (format (slots-keys item-type) *uid*)
        length (redis/llen *db* key)
    ]
    (println "------" key "-----------")
    (loop [i 0]
        (when (< i length)
            (println i (redis/lindex *db* key i))
            (recur (inc i))))))

(defn show-items-and-slots []
    (show-items)
    (print-slots 1)
    (print-slots 2))


(defn show-field [key field]
    (println  field (redis/hget *db* key field)))

(defn show-fund
    ([] (show-fund *uid*))
    ([uid]
        (let [user-key (format "nadouser:%s:userinfo" uid)]
         (dorun (map #(show-field user-key %) ["Money", "LingShi"])))))

(defn fill-fund []
  (let [user-key (format "nadouser:%s:userinfo" *uid*)
        num "100000"]
    (when (redis/exists *db* user-key)
    (redis/hset *db* user-key "Money" num) 
    (redis/hset *db* user-key "LingShi" num))))

(defn reload-self []
   (require 'nado-helper.core :reload))


(defn get-values [key fields]
    (apply redis/hmget (concat [*db* key] fields)))

(defn show-items-in-table []
    (let [
            keys-format (format "nadouser:%s:item:*" *uid*)
            keys (redis/keys *db* keys-format)
            headers (redis/hkeys *db* (first keys))
            values (map #(get-values % headers) keys)
        ]
        (display/print-table headers values)))

