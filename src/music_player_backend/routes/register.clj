(ns music-player-backend.routes.register
  (:require [music-player-backend.server :as server]
            [music-player-backend.json :as json]
            [music-player-backend.auth :as auth]))

(defn route
  "Route to register a new user"
  [req]
  (let [data (json/json-from-request req)
        result (auth/register-user data)]

    (case result
      :already-exist (server/respond "User already exists" :status 409)
      :user-error (server/respond "Server error" :status 400)
      (server/respond result :status 201))
    ))
