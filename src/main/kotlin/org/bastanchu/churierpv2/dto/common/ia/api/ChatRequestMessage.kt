package org.bastanchu.churierpv2.dto.common.ia.api

data class ChatRequestMessage(val role: String, val content: String, val images: List<String>? = null)