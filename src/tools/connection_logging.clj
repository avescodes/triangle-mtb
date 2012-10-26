(ns tools.connection-logging
  (import java.net.InetAddress))

(defn local-host [] (. InetAddress getLocalHost))

(defn log-ip-addresses []
  (let [localhost (. InetAddress getLocalHost)
        canonical-name (.getCanonicalHostName localhost)]
    (println (str "My IP: "(.getHostAddress localhost)))
    (println (str "My Canonical Hostname: " canonical-name))
    (doseq [addresses (. InetAddress getAllByName canonical-name)]
      (println (str "I'm also: " (.getHostAddress addresses))))))
