(ns triangle-mtb.api
  (:use [compojure.core :only [defroutes GET]])
  (:require [ring.adapter.jetty :as ring]
            [cheshire.core :as json]))

(defn json-response [data & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/json"}
   :body (json/generate-string data)})

(defroutes application
  (GET "/trail-status.json" [] (json-response [{:trail/name "Lake Crabtree"
                                               :trail/open? true
                                               :trail/last-updated (java.util.Date. 0)}])))

(defn start [port]
  (ring/run-jetty #'application {:port (or port 8080) :join? false}))

(defn -main []
  (let [port (Integer. (System/getenv "PORT"))]
        (start port)))