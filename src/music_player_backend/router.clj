(ns music-player-backend.router
  (:require [music-player-backend.middleware :as middleware]
            [music-player-backend.routes.song :as route-song]
            [music-player-backend.routes.login :as route-login]
            [music-player-backend.routes.search :as route-search]
            [music-player-backend.routes.register :as route-register]
            [music-player-backend.routes.not-found :as route-not-found]))

(defn handler
  "Handle http requests and send them to the correct route function"
  [req]
  (let [uri (:uri req)]
    (case uri
      "/login" (route-login/route req)
      "/song" (route-song/route req)
      ;; "/song" (middleware/check-token route-song/route req)
      "/search" (middleware/check-token route-search/route req)
      "/register" (middleware/check-master-key route-register/route req)
      (route-not-found/route))))
