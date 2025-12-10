(ns music-player-backend.server)

(defn respond
  "Template for HTTP responses"
  [body & {:keys [status headers] :or {status 200
                                       headers {"content-type" "text/plain"}}}]
  {:status status
   :headers headers
   :body body})
