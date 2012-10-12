(ns triangle-mtb.trail-status
  (:require [net.cgrand.enlive-html :as h])
  (:require [clojure.string :as s])
  (:import java.net.URL))

(defn keywordize [str]
  (-> str
      s/lower-case
      (s/replace " " "-")
      keyword))

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

(defn row->last-updated [row]
  (-> row
      (h/select [[:td (h/attr? :nowrap)]])
      (h/select [:span])
      first
      :content
      first))

(defn row->triplet [row]
  ((juxt row->name row->status row->last-updated) row))

(defn fetch []
  (-> "http://www.trianglemtb.com/trailstatus.php"
      URL.
      h/html-resource))

(defn parse [html]
  (let [rows (status->rows html)]
    (map row->triplet rows)))

(defn current-status [] (parse (fetch)))

