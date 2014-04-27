(ns nado-helper.display
 [:import com.bethecoder.ascii_table.ASCIITable]
 (:gen-class))

(def to-string (memfn toString))

(defn to-strings [data] (map to-string data))

(defn to-2d-string [data]
  (map to-strings data))

(defn to-string-array [data]
   (into-array String (to-strings data)))

(defn to-2d-string-array [data]
    (into-array (map to-string-array data)))

(defn print-table [headers, data]
    (let [headers (to-string-array headers)
          data (to-2d-string-array data)
          table (ASCIITable/getInstance)]
        (.printTable table headers data)
    ))


