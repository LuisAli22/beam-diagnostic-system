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
  [(or
    (Altura (< verdadera minima))
    (Seccion (< minima limite))
    (Seccion (< verdadera minima))
    (Corte (< (/ capacidad solicitacion) 1.0))
    (Flexion (< (/ capacidad solicitacion) 1.0)))]
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
  [Condition (= tiempo LENTO)]
  [Condition (= costo MUYCARO)]
  [Condition (= espacio POCO)]
  [Condition (= carga POCO)]
  [Condition (= adquisicion MUYDIFICIL)]
  =>
  (insert! (->Refuerzo FIBRACARBONO))
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
  )
)
