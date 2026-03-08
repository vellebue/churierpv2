package org.bastanchu.churierpv2.service

import org.bastanchu.churierpv2.dto.common.ia.prompt.PromptDto
import org.bastanchu.churierpv2.dto.common.ia.prompt.PromptListDto
import org.springframework.stereotype.Component
import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.TypeDescription
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import org.yaml.snakeyaml.nodes.Tag

@Component
class PromptDirectoryService {

    lateinit var promptList: List<PromptDto>

    init {
        val stream = this.javaClass.classLoader.getResourceAsStream("prompts.yaml")
        //val listType = TypeDescription(List::class.java, Tag.SEQ)
        //listType.putListPropertyType("dummy", PromptDto::class.java)
        val constructor = Constructor(PromptListDto::class.java, LoaderOptions())
        //constructor.addTypeDescription(listType)
        val yaml = Yaml(constructor)
        val promptList = yaml.load(stream) as PromptListDto
        this.promptList = promptList.promptList!!
    }

    fun getPrompt(promptId: String): PromptDto? {
        return promptList.filter { it.id == promptId } .firstOrNull()
    }

}