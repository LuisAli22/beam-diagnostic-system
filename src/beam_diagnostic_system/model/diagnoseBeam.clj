(ns beam-diagnostic-system.model.diagnoseBeam
  (:use [beam-diagnostic-system.util.definitions]
    [beam-diagnostic-system.util.excelReader]
        [beam-diagnostic-system.util.formulas]
        [beam-diagnostic-system.rules.verificationRules])
  (:require [clara.rules :refer :all])
)
(defn diagnoseBeam [inputBeamDataFile inputConditionDataFile]
  (def beamDataMap (storeInputToMap inputBeamDataFile))
  (def conditionDataMap (storeInputToMap inputConditionDataFile))
  (startDiagnose beamDataMap conditionDataMap)
)
