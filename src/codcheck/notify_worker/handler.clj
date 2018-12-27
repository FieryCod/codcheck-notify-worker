(ns codcheck.notify-worker.handler
  (:require
   [langohr basic consumers channel]
   [tentacles.core :as tentacles]
   [taoensso.timbre :as timbre]
   [clj-http.client :as client]
   [cheshire.core :as cheshire]
   [clojure.string :as string]
   [codcheck.envs :refer [envs]]
   [codcheck github auth]
   [codcheck.notify-worker file]))

(defmacro propagate-on-error
  [body]
  `(try ~body
        (catch Exception err#
          (.printStackTrace err#)
          (timbre/info "Removing worker...")
          (System/exit -1))))

(defn construct-message
  [lint-result]
  (clojure.pprint/pprint lint-result)
  (str "**Linter "  (string/capitalize (:linter-type lint-result))  " respond with error messages:** \n-----\n"
       (string/join "\n"
                    (map #(str "At `" (:path %) ":" (:line %) "`\n\n" (:explanation %) "\n") (:messages lint-result)))))


(defn on-checked-pr
  [_ _ ^bytes payload]
  (propagate-on-error
   (let [event (read-string (slurp payload))
         commit-sha (-> event :pull_request :head :sha)
         username (-> event :pull_request :head :user :login)
         repo-name (-> event :pull_request :head :repo :name)
         repo-url (-> event :pull_request :head :repo :git_url)
         repo-identifier (clojure.string/join "/" [username repo-name commit-sha])
         project-path (codcheck.notify-worker.file/get-absolute-project-path)
         signed-token (codcheck.auth/gh-sign-token)
         installation-id (-> event :installation :id)
         token (codcheck.auth/installation-request->token
                (codcheck.auth/gh-installation-token-request installation-id signed-token))]

     (codcheck.github/comment-on-pr token username repo-name (:number event) (construct-message (:lint-result event))))))
