package com.example.demo

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.ListBucketsResponse

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class Back2ApplicationTests {

    @Autowired
    private lateinit var mvc: MockMvc

    // 1. S3Client와 S3Service를 가짜(Mock) 객체로 등록하여 에러를 방지합니다.
    @MockitoBean
    private lateinit var s3Client: S3Client

    @MockitoBean
    private lateinit var s3Service: S3Service

    @Test
    @DisplayName("GET /buckets")
    fun t1() {
        // 2. Controller에서 s3Client.listBuckets()를 호출할 때 NullPointerException이 나지 않도록 빈 버킷 리스트를 반환하게 셋팅합니다.
        val fakeResponse = ListBucketsResponse.builder().buckets(emptyList()).build()
        given(s3Client.listBuckets()).willReturn(fakeResponse)

        val resultActions = mvc
            .perform(
                get("/buckets")
            )
            .andDo(MockMvcResultHandlers.print())

        // 기존 코드 유지
        resultActions
            .andExpect(status().isOk())
    }
}