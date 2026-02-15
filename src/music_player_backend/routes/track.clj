(ns music-player-backend.routes.track
  (:require [music-player-backend.track :as track]
            [music-player-backend.server :as server]
            [music-player-backend.json :as json]
            [music-player-backend.db :as db]))

(defn route
  "Route to handle track requests"
  [req]
  (let [userid (db/get-userid (:username (:user-data req)))
        data (assoc (json/json-from-request req) :userid userid)]

    (if userid
      (case (:request-method req)
        :post (track/add-track data)
        :put (track/update-track data)
        :delete (track/delete-track data)
        (server/respond "Unknown request" :status 405))

      (server/respond "User not found" :status 404))))
