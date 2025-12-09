(ns music-player-backend.router)

(defn respond [body & {:keys [status] :or {status 200}}]
  {:status (or status 200)
   :headers {"content-type" "text/plain"}
   :body body})

(defn handler [req]
  (let [uri (:uri req)]
    (println "route is" uri)
    (cond
     (= uri "/") (respond "hello main!")
     (= uri "/route") (respond "example route")
     :else (respond "what??" :status 404))))
