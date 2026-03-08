package org.bastanchu.churierpv2.service.impl.accountancy

import org.bastanchu.churierpv2.dto.common.ia.api.ChatRequest
import org.bastanchu.churierpv2.dto.common.ia.api.ChatRequestMessage
import org.bastanchu.churierpv2.dto.common.ia.api.ChatRequestResponseFormat
import org.bastanchu.churierpv2.dto.common.ia.api.ChatResponseItem
import org.bastanchu.churierpv2.service.PromptDirectoryService
import org.bastanchu.churierpv2.service.accountancy.IncomingInvoicesIAService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClient
import java.io.InputStream
import java.util.Base64

@Service
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
open class IncomingInvoicesIAServiceImpl(@Autowired val ollamaWebClient: WebClient,
                                         @Autowired val promptDirectoryService: PromptDirectoryService) : IncomingInvoicesIAService {

    val serviceUri = "/api/chat"
    val invoicePromptId = "receivingInvoicePrompt"

    override fun parseInvoice(inputStream: InputStream): String {
        /*
        val chatRequestMessage = ChatRequestMessage(role = "user", content = "Describe articles, num units and prices on this invoice", listOf(base64Image))
        val chatRequest = ChatRequest(model = "qwen2.5vl:7b", listOf(chatRequestMessage))
         */
        val chatRequest = buldChatRequest(inputStream)
        val response =  ollamaWebClient
                            .post()
                            .uri(serviceUri)
                            .bodyValue(chatRequest)
                            .retrieve()
                            //.bodyToMono(String::class.java)
            .bodyToFlux<ChatResponseItem>(ChatResponseItem::class.java).map { it.message?.content ?: "" }
            .collectList()
            .block()         //.block()
        return response.joinToString(separator = "")
    }

    private fun buldChatRequest(inputStream: InputStream) : ChatRequest {
        val invoicePrompt = promptDirectoryService.getPrompt(invoicePromptId)
        val base64Image = fromInputStreamToBase64(inputStream)
        val messages = ArrayList<ChatRequestMessage>()
        if (invoicePrompt?.systemPrompt != null) {
            messages.add(ChatRequestMessage(role = "system", content = invoicePrompt.systemPrompt!!))
        }
        if (invoicePrompt?.userPrompt != null) {
            messages.add(ChatRequestMessage(role = "system", content = invoicePrompt.userPrompt!!, listOf(base64Image)))
        }
        val chatRequestResponseFormat = ChatRequestResponseFormat(type = "json_schema", jsonSchema = invoicePrompt?.jsonResponseSchema)
        val chatRequest = ChatRequest(model = invoicePrompt?.model ?: "", messages = messages, chatRequestResponseFormat)
        return chatRequest
    }

    private fun fromInputStreamToBase64(stream: InputStream): String {
        val bytes = stream.readAllBytes()
        val base64Result = Base64.getEncoder().encodeToString(bytes)
        return base64Result
    }
}