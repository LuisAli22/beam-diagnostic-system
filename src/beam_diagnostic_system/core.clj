(ns beam-diagnostic-system.core
  (:use [beam-diagnostic-system.util.definitions]
         [beam-diagnostic-system.model.diagnoseBeam]
  )
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clojure.string :as string])
)
(defn usage [optionsSummary]
  (->> ["Usage: lein run -- [options]"
        ""
        "Options:"
        optionsSummary
        ""]
       (string/join \newline)))

(defn errorMessage [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (string/join \newline errors)))

(defn exit [status message]
  (println message)
  (System/exit status))
(defn -main [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cliOptions)]
  ;; Manejar errores y ayuda
    (cond
      (:help options) (exit 0 (usage summary))
      (> (count arguments) 1) (exit 1 (usage summary))
      errors (exit 1 (errorMessage errors))
    )
    (diagnoseBeam (inputBeamDataFile arguments))
  )
)
