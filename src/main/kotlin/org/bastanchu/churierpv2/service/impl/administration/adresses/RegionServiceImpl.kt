package org.bastanchu.churierpv2.service.impl.administration.adresses

import org.bastanchu.churierpv2.dao.administration.addresses.RegionsDao
import org.bastanchu.churierpv2.service.administration.adresses.RegionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.text.Collator

@Service
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
open class RegionServiceImpl(@param:Autowired val regionsDao: RegionsDao,
                             @param:Autowired val messages: MessageSource): RegionService {

    override fun retrieveRegionsMap(): Map<String, Map<String, String>> {
        val regions = regionsDao.listAll()
        val sorter = Collator.getInstance(LocaleContextHolder.getLocale())
        val sortedRegions = regions.sortedWith { region, region1 ->
            sorter.compare(messages.getMessage(region.key, null, LocaleContextHolder.getLocale()),
                messages.getMessage(region1.key, null, LocaleContextHolder.getLocale()))
        } //.sortedBy { messages.getMessage(it.key!!, null, LocaleContextHolder.getLocale()) }
        val map = HashMap<String, Map<String, String>>()
        for (region in sortedRegions) {
            val regionMap = map[region.countryId] as HashMap<String, String>?
            if (regionMap != null) {
                regionMap[region.regionId!!] = "${region.regionId} - ${messages.getMessage(region.key!!, null, LocaleContextHolder.getLocale())}"
            } else {
                val regionMap = linkedMapOf<String, String>()
                map[region.countryId!!] = regionMap
                regionMap[region.regionId!!] = "${region.regionId} - ${messages.getMessage(region.key!!, null, LocaleContextHolder.getLocale())}"
            }
        }
        return map
    }

}