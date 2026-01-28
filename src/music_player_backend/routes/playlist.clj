(ns music-player-backend.routes.playlist
  (:require [music-player-backend.playlist :as playlist]
            [music-player-backend.server :as server]
            [music-player-backend.json :as json]
            [music-player-backend.db :as db]))

(defn route
  "Route to handle playlist requests"
  [req]
  (let [userid (db/get-userid (:username (:user-data req)))
        data (assoc (json/json-from-request req) :userid userid)]

    (if userid
      (case [(:uri req) (:request-method req)]
        ["/playlist" :post] (playlist/add-playlist data)
        ["/playlist" :put] (playlist/update-playlist data)
        ["/playlist" :delete] (playlist/delete-playlist data)

        ["/playlist/songs" :put] (playlist/update-playlist-list data)

        (server/respond "Unknown request" :status 405))

      (server/respond "User not found" :status 404))))