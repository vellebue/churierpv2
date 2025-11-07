package org.bastanchu.churierpv2.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping("/")
class LoginController {

    @GetMapping("/login")
    fun login(): String {
        return "loginForm"
    }

    @GetMapping("/public/login.css", produces = ["text/css"])
    @ResponseBody
    fun  loginCss() : ByteArray {
        val classLoader = Thread.currentThread().contextClassLoader
        val stream = classLoader.getResourceAsStream("static/public/login.css")
        val byteArray = stream.readAllBytes()
        return byteArray
    }
}