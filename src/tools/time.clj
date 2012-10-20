(ns tools.time)

(def triangle-mtb-time-format (java.text.SimpleDateFormat. "yy/MM/dd hh:mm a"))
(defn current-year [] (.. java.util.Calendar (getInstance) (get java.util.Calendar/YEAR)))

(defn time-string->inst [time-string]
  (let [string-with-year (str (current-year) "/" time-string)]
    (.parse triangle-mtb-time-format string-with-year)))