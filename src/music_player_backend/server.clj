(ns music-player-backend.server)

(defn respond [body & {:keys [status] :or {status 200}}]
  {:status (or status 200)
   :headers {"content-type" "text/plain"}
   :body body})
