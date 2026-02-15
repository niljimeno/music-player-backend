(ns music-player-backend.routes.playlist
  (:require [music-player-backend.playlist :as playlist]
            [music-player-backend.server :as server]
            [music-player-backend.json :as json]
            [music-player-backend.db-mg :as db]))

(defn route
  "Route to handle playlist requests"
  [req]
  (let [user (db/get-user (:username (:user-data req)))
        data (assoc (json/json-from-request req) :user_id (:_id (:user-data req)))]

    (println "user data:" (:user-data req))

    (if user
      (case [(:uri req) (:request-method req)]
        ["/playlist" :post] (playlist/add data)
        ["/playlist" :put] (playlist/edit data)
        ["/playlist" :delete] (playlist/delete data)

        ["/playlist/tracks" :put] (playlist/update-list data)

        (server/respond "Unknown request" :status 405))

      (server/respond "User not found" :status 404))))
