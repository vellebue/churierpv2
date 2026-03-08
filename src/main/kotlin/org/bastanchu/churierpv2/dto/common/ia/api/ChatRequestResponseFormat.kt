package org.bastanchu.churierpv2.dto.common.ia.api

import com.fasterxml.jackson.annotation.JsonProperty

data class ChatRequestResponseFormat(val type: String = "", @JsonProperty("json_schema") val jsonSchema: Map<String, Any?>? = null)