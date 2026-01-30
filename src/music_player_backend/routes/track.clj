(ns music-player-backend.routes.track
  (:require [music-player-backend.playlist :as playlist]
            [music-player-backend.server :as server]
            [music-player-backend.json :as json]
            [music-player-backend.db :as db]))

(defn route
  "Route to handle track requests in playlists"
  [req]
  (let [userid (db/get-userid (:username (:user-data req)))
        data (assoc (json/json-from-request req) :userid userid)] 

    (if userid
      (case (:request-method req)
        :post (playlist/add-track data)
        :put (playlist/update-track data)
        :delete (playlist/delete-track data)
        (server/respond "Unknown request" :status 405))

      (server/respond "User not found" :status 404))))