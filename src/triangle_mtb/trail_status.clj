(ns triangle-mtb.trail-status
  (:require [net.cgrand.enlive-html :as h])
  (:require [clojure.string :as s])
  (:import java.net.URL))

(defn fetch-raw-trail-status []
  (-> "http://www.trianglemtb.com/trailstatus.php"
      URL.
      h/html-resource))

(defn status->header [trail-status]
  (->>
   (-> trail-status
       (h/select [:tr])
       first
       (h/select [:td :b]))
   (map (comp first :content))))

(defn status->rows [trail-status]
  (-> trail-status
      (h/select [:tr])
      rest))

(defn row->name [row]
  (-> row
      (h/select [:a])
      first
      :content
      first))

(defn row->status [row]
  (-> row
      (h/select [[:td (h/attr? :title)]])
      (h/select [:span])
      first
      :content
      first
      s/trim
      s/lower-case
      keyword))

(defn row->updated-at [row]
  (-> row
      (h/select [[:td (h/attr? :nowrap)]])
      (h/select [:span])
      first
      :content
      first))

(defn row->triplet [row]
  ((juxt row->name row->status row->updated-at) row))

(defn fetch-current []
  (let [status (fetch-raw-trail-status)
        header (conj (status->header status) "Name")
        rows   (status->rows status)]
      (map #(zipmap header (row->triplet %)) rows)))



