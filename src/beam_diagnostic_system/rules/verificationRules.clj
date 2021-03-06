(ns beam-diagnostic-system.rules.verificationRules
  (:use [beam-diagnostic-system.util.definitions]
        [beam-diagnostic-system.util.formulas])
  (:require [clara.rules :refer :all])
)

(defrecord Altura [verdadera minima])
(defrecord Seccion [verdadera minima limite])
(defrecord Corte [capacidad solicitacion])
(defrecord Flexion [capacidad solicitacion])
(defrecord Verification [name])
(defrecord InitialDiagnoseResult [resultMessage])
(defrecord Condition [tiempo costo espacio carga adquisicion])
(defrecord Refuerzo [nombre])
(defrecord VigaBienDimensionada [valor])
(defrecord FinalDiagnoseResult [resultMessage])
(defrule RVerificaAltura
  "Se debe verificar si la viga cumple con la altura mínima"
  [Altura (>= verdadera minima)]
  =>
  (insert! (->Verification
            OKHEIGHT))
)
(defrule RVerifLimiteSeccion
  [Seccion (>= minima limite)]
  =>
  (insert! (->Verification
            OKSECTIONLIMIT))
)
(defrule RVerifSeccion
  [Seccion (>= verdadera minima)]
  =>
  (insert! (->Verification
            OKSECTION))
)
(defrule RVerifCorte
  [Corte (>= (/ capacidad solicitacion) 1.0)]
  =>
  (insert! (->Verification
            OKSHEAR))
)
(defrule RVerifFlexion
  [Flexion (>= (/ capacidad solicitacion) 1.0)]
  =>
  (insert! (->Verification
            OKBINDING))
)
(defrule RVerificaReqDis
  [Altura (>= verdadera minima)]
  [Seccion (>= minima limite)]
  [Seccion (>= verdadera minima)]
  [Corte (>= (/ capacidad solicitacion) 1.0)]
  [Flexion (>= (/ capacidad solicitacion) 1.0)]
  =>
  (insert! (->VigaBienDimensionada true)
            (->InitialDiagnoseResult OKBEAM)
            (->Refuerzo NONEEDREINFORCEMENT ))
)
(defrule RNoVerificaReqDis
  [:or
    [Altura (< verdadera minima)]
    [Seccion (< minima limite)]
    [Seccion (< verdadera minima)]
    [Corte (< (/ capacidad solicitacion) 1.0)]
    [Flexion (< (/ capacidad solicitacion) 1.0)]]
  =>
  (insert!  (->VigaBienDimensionada false)
            (->InitialDiagnoseResult NOTOKBEAM))
)
(defrule RRefPerfil
  [VigaBienDimensionada (= valor false)]
  [Condition (= tiempo MUYRAPIDO)]
  [Condition (= costo CARO)]
  [Condition (= espacio MUCHO)]
  [Condition (= carga MUCHO)]
  [Condition (= adquisicion MUYDIFICIL)]
  =>
  (insert! (->Refuerzo PERFIL))
)
(defrule RRefPlanchaAcero
  [VigaBienDimensionada (= valor false)]
  [Condition (= tiempo MUYRAPIDO)]
  [Condition (= costo CARO)]
  [Condition (= espacio SUFICIENTE)]
  [Condition (= carga MODERADO)]
  [Condition (= adquisicion DIFICIL)]
  =>
  (insert! (->Refuerzo PLANCHAACERO))
)
(defrule RRefVigaReticulada
  [VigaBienDimensionada (= valor false)]
  [Condition (= tiempo RAPIDO)]
  [Condition (= costo MUYCARO)]
  [Condition (= espacio MUCHO)]
  [Condition (= carga SUFICIENTE)]
  [Condition (= adquisicion DIFICIL)]
  =>
  (insert! (->Refuerzo VIGARETICULADA))
)
(defrule RRefEnchapeHormigon
  [VigaBienDimensionada (= valor false)]
  [Condition (= tiempo MUYLENTO)]
  [Condition (= costo MUYBARATO)]
  [Condition (= espacio SUFICIENTE)]
  [Condition (= carga POCO)]
  [Condition (= adquisicion FACIL)]
  =>
  (insert! (->Refuerzo ENCHAPEHORMIGON))
)
(defrule RRefDobleViga
  [VigaBienDimensionada (= valor false)]
  [Condition (= tiempo LENTO)]
  [Condition (= costo BARATO)]
  [Condition (= espacio MUCHO)]
  [Condition (= carga MUCHO)]
  [Condition (= adquisicion MUYFACIL)]
  =>
  (insert! (->Refuerzo DUPLICARVIGA))
)
(defrule RRefFibra
  [VigaBienDimensionada (= valor false)]
  [Condition (= tiempo MUYRAPIDO)]
  [Condition (= costo MUYCARO)]
  [Condition (= espacio POCO)]
  [Condition (= carga POCO)]
  [Condition (= adquisicion MUYDIFICIL)]
  =>
  (insert! (->Refuerzo FIBRACARBONO))
)
(defrule RefInviable
  [VigaBienDimensionada (= valor false)]
  [:not [Condition (= tiempo MUYRAPIDO) (= costo CARO) (= espacio MUCHO) (= carga MUCHO) (= adquisicion MUYDIFICIL)]]
  [:not [Condition (= tiempo MUYRAPIDO) (= costo CARO) (= espacio SUFICIENTE) (= carga MODERADO) (= adquisicion DIFICIL)]]
  [:not [Condition (= tiempo RAPIDO) (= costo MUYCARO) (= espacio MUCHO) (= carga SUFICIENTE) (= adquisicion DIFICIL)]]
  [:not [Condition (= tiempo MUYLENTO) (= costo MUYBARATO) (= espacio SUFICIENTE) (= carga POCO) (= adquisicion FACIL)]]
  [:not [Condition (= tiempo LENTO) (= costo BARATO) (= espacio MUCHO) (= carga MUCHO) (= adquisicion MUYFACIL)]]
  [:not [Condition (= tiempo MUYRAPIDO) (= costo MUYCARO) (= espacio POCO) (= carga POCO) (= adquisicion MUYDIFICIL)]]
  => (insert! (->Refuerzo UNFEASIBLEREINFORCEMENT))
)

(defrule RVerificaReqDisConRefPerfilOVigaRet
  [:or [Refuerzo (= nombre PERFIL)] [Refuerzo (= nombre VIGARETICULADA)]]
  [Seccion (>= (* verdadera 10) minima)]
  [Corte (>= (/ capacidad (* solicitacion 0.5)) 1.0)]
  [Flexion (>= (/ capacidad (* solicitacion 0.5)) 1.0)]
  =>
  (insert! (->FinalDiagnoseResult OKFINALBEAM))
)
(defrule RVerificaReqDisConRefPlanchaAceroOEnchapeHormigon
  [:or [Refuerzo (= nombre PLANCHAACERO)] [Refuerzo (= nombre ENCHAPEHORMIGON)]]
  [Seccion (>= (* verdadera 1.5) minima)]
  [Corte (>= (/ capacidad (* solicitacion 0.75)) 1.0)]
  [Flexion (>= (/ capacidad (* solicitacion 0.75)) 1.0)]
  =>
  (insert! (->FinalDiagnoseResult OKFINALBEAM))
)
(defrule RVerificaReqDisConRefDobleViga
  [Refuerzo (= nombre DUPLICARVIGA)]
  [Seccion (>= (* verdadera 2) minima)]
  [Corte (>= (/ capacidad (* solicitacion 0.5)) 1.0)]
  [Flexion (>= (/ capacidad (* solicitacion 0.5)) 1.0)]
  =>
  (insert! (->FinalDiagnoseResult OKFINALBEAM))
)
(defrule RVerificaReqDisConRefFibra
  [Refuerzo (= nombre DUPLICARVIGA)]
  [Seccion (>= (* verdadera 1.2) minima)]
  [Corte (>= (/ capacidad (* solicitacion 0.9)) 1.0)]
  [Flexion (>= (/ capacidad (* solicitacion 0.9)) 1.0)]
  =>
  (insert! (->FinalDiagnoseResult OKFINALBEAM))
)
(defrule RNoVerificaReqDisConRefPerfilOVigaRet
  [:or [Refuerzo (= nombre PERFIL)] [Refuerzo (= nombre VIGARETICULADA)]]
  [:or  [Seccion (< (* verdadera 10) minima)]
        [Corte (< (/ capacidad (* solicitacion 0.5)) 1.0)]
        [Flexion (< (/ capacidad (* solicitacion 0.5)) 1.0)]]
  =>
  (insert! (->FinalDiagnoseResult MISMATCHREINFORCEMENT))
)
(defrule RNoVerificaReqDisConRefPlanchaAceroOEnchapeHormigon
  [:or  [Refuerzo (= nombre PLANCHAACERO)] [Refuerzo (= nombre ENCHAPEHORMIGON)]]
  [:or  [Seccion (< (* verdadera 1.5) minima)]
        [Corte (< (/ capacidad (* solicitacion 0.75)) 1.0)]
        [Flexion (< (/ capacidad (* solicitacion 0.75)) 1.0)]]
  =>
  (insert! (->FinalDiagnoseResult MISMATCHREINFORCEMENT))
)
(defrule RNoVerificaReqDisConRefDobleViga
  [Refuerzo (= nombre DUPLICARVIGA)]
  [:or  [Seccion (< (* verdadera 2) minima)]
        [Corte (< (/ capacidad (* solicitacion 0.5)) 1.0)]
        [Flexion (< (/ capacidad (* solicitacion 0.5)) 1.0)]]
  =>
  (insert! (->FinalDiagnoseResult MISMATCHREINFORCEMENT))
)
(defrule RNoVerificaReqDisConRefFibra
  [Refuerzo (= nombre DUPLICARVIGA)]
  [:or  [Seccion (< (* verdadera 1.2) minima)]
        [Corte (< (/ capacidad (* solicitacion 0.9)) 1.0)]
        [Flexion (< (/ capacidad (* solicitacion 0.9)) 1.0)]]
  =>
  (insert! (->FinalDiagnoseResult MISMATCHREINFORCEMENT))
)

(defquery checkValidation
  "Busco los resultados de las verificaciones realizadas"
  []
  [?verificationItem <- Verification]
)
(defquery checkInitialResult
  "Busca el resultado del análisis inicial"
  []
  [?resultItem <- InitialDiagnoseResult]
)
(defquery checkFinalResult
  "Busca el resultado del análisis final"
  []
  [?resultItem <- FinalDiagnoseResult]
)
(defquery checkSelection
  "Busco los resultados de las verificaciones realizadas"
  []
  [?reinforcementItem <- Refuerzo]
)
(defn printVerifications!
  "Imprime el resultado de las verificaciones realllizadas en esta sesión"
  [session]
  (println "Verificaciones")
  (doseq [result (query session checkValidation)]
    (println "\t"
            (get-in result [:?verificationItem :name])))
  session
)
(defn printOriginalBeamDiagnose!
  "Imprime el diagnóstico de la viga original en esta sesión"
  [session]
  (doseq [result (query session checkInitialResult)]
    (println "Resultado del análisis: "
            (get-in result [:?resultItem :resultMessage])))
  session
)
(defn printReinforcementSelection!
  "Imprime el resultado del refuerzo seleccionado en esta sesión"
  [session]
  (doseq [result (query session checkSelection)]
    (println "Refuerzo: "
            (get-in result [:?reinforcementItem :nombre])))
  session
)
(defn printFinalDiagnose!
  "Imprime el diagnóstico final"
  [session]
  (doseq [result (query session checkFinalResult)]
    (println "Resultado del análisis de viga con refuerzo: "
            (get-in result [:?resultItem :resultMessage])))
  session
)
(defn startDiagnose [beamDataMap conditionDataMap]
  (def alturaUtil
    (calcularAlturaUtil (beamDataMap height)
                (beamDataMap longitudinalBarDiameter)
                (beamDataMap stirrupBarDiameter)
                (beamDataMap concreteCover))
  )
  (def alturaMinima
    (calcularAlturaMinima (beamDataMap supportPointAmount)
                          (beamDataMap beamLength))
  )
  (def limiteSeccion
    (calcularLimiteSecciónMinima  alturaUtil
                                  (beamDataMap steelQuality)
                                  (beamDataMap width))
  )
  (def seccionMinima
    (calcularSeccionMinima  alturaUtil
                            (beamDataMap steelQuality)
                            (beamDataMap concreteQuality)
                            (beamDataMap width))
  )
  (def capacidadCorte
    (calcularCapacidadCorte (beamDataMap nominalShearTension))
  )
  (def capacidadFlexion
    (calcularCapacidadFlexion (beamDataMap nominalBendingMoment))
  )
  (-> (mk-session 'beam-diagnostic-system.rules.verificationRules)
      (insert (->Altura (beamDataMap height)
                        alturaMinima)
              (->Seccion  (beamDataMap steelSection)
                          seccionMinima
                          limiteSeccion)
              (->Corte  capacidadCorte
                        (beamDataMap lastShearTension))
              (->Flexion  capacidadFlexion
                          (beamDataMap lastBendingMoment))
              (->Condition  (conditionDataMap TIME)
                            (conditionDataMap COST)
                            (conditionDataMap SPACE)
                            (conditionDataMap LOAD)
                            (conditionDataMap PURCHASE))
    )
    (fire-rules)
    (printVerifications!)
    (printOriginalBeamDiagnose!)
    (printReinforcementSelection!)
    (printFinalDiagnose!)
  )
)
