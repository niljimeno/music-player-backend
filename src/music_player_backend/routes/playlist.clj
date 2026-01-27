(ns music-player-backend.routes.playlist
  (:require [music-player-backend.playlist :as playlist]
            [music-player-backend.server :as server]
            [music-player-backend.json :as json]
            [music-player-backend.db :as db]))

(defn route
  "Route to add a new playlist"
  [req]
  (let [userid (db/get-userid (:username (:user-data req)))
        data (assoc (json/json-from-request req) :userid userid)] 

    (if userid
      (case (:request-method req)
        :post (playlist/add-playlist data)
        :put (playlist/update-playlist data)
        :delete (playlist/delete-playlist data)
        (server/respond "Unknown request" :status 405))

      (server/respond "User not found" :status 404))))