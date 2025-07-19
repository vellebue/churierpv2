package org.bastanchu.churierpv2.dto.administration.societies

import org.bastanchu.churierpv2.view.common.annotations.Field
import org.bastanchu.churierpv2.view.common.annotations.FormField

data class SocietiesFilterDto(@Field("societies.filter.name.key")
                              @FormField(groupId = 0, indexInGroup = 0)
                              var name : String? = null,
                              @Field("societies.filter.socialName.key")
                              @FormField(groupId = 0, indexInGroup = 1)
                              var socialName: String? = null) {
}