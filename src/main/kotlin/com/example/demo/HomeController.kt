package com.example.demo

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import software.amazon.awssdk.services.s3.S3Client

@RestController
class HomeController(
    private val s3Service: S3Service,
    private val s3Client: S3Client
) {

    @Value("\${custom.secretWord}")
    private val secretWord: String = ""

    @GetMapping
    fun main(): String {
        return "Hi, $secretWord"
    }

    @GetMapping("/buckets")
    fun buckets(): List<String> {
        return s3Client.listBuckets().buckets().map { it.name() }
    }

}