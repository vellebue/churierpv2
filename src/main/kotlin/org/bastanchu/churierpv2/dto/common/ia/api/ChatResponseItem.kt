package org.bastanchu.churierpv2.dto.common.ia.api

import com.fasterxml.jackson.annotation.JsonProperty

data class ChatResponseItem(var model: String? = "", @JsonProperty("created_at") var createdAt: String? = "", var role: String? = "", var message: ChatResponseMessageItem? = null, var done: Boolean? = true)

data class ChatResponseMessageItem(var role: String? = "", var content: String? = "")