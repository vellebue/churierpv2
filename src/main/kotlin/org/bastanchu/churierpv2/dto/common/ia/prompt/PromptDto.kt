package org.bastanchu.churierpv2.dto.common.ia.prompt

data class PromptDto(
    var id: String? = null,
    var description: String? = null,
    var model: String? = null,
    var systemPrompt: String? = null,
    var userPrompt: String? = null,
    var jsonResponseSchema: Map<String, Any?>? = null,
    var temperature: Double? = null
)