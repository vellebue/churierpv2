package org.bastanchu.churierpv2.dto.common.ia.api

import com.fasterxml.jackson.annotation.JsonProperty

data class ChatRequest(val model: String, val messages: List<ChatRequestMessage>,
                       @JsonProperty("response_format") val responseFormat: ChatRequestResponseFormat? = null)