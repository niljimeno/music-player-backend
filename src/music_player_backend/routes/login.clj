(ns music-player-backend.routes.login
  (:require [music-player-backend.server :as server]
            [music-player-backend.json :as json]
            [music-player-backend.auth :as auth]))

(defn route
  "Route to log-in session"
  [req]
  (let [data (json/value-from-request :data req) 
        result (auth/login-user data)]

    (case result
      :user-login (server/respond "Login successful")
      :incorrect-password (server/respond "Incorrect password" :status 401)
      :not-found (server/respond "User not found" :status 404)
      :user-error (server/respond "Server error" :status 400))
    ))
