(ns music-player-backend.router)

(defn handler [req]
  (println (:uri req))
  {:status 200
   :headers {"content-type" "text/plain"}
   :body "works"})
