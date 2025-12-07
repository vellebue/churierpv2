package org.bastanchu.churierpv2.dao.administration.addresses

import org.bastanchu.churierpv2.dao.BaseValueObjectDao
import org.bastanchu.churierpv2.dto.administration.adresses.RegionDto
import org.bastanchu.churierpv2.entity.administration.adresses.Region
import org.bastanchu.churierpv2.entity.administration.adresses.RegionPk

interface RegionsDao: BaseValueObjectDao<RegionPk, Region, RegionDto> {
}