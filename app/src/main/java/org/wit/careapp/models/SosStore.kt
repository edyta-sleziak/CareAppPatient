package org.wit.careapp.models

interface SosStore {
    fun runSosAlert(sos: SosModel)
}