package org.bastanchu.churierpv2.service.administration.adresses

interface RegionService {

    fun retrieveRegionsMap(): Map<String, Map<String, String>>

}