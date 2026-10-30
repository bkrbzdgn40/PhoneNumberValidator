package com.odev.telefon_servisi.controller;

import com.odev.telefon_servisi.repository.RegistrationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PhoneController.class)
class PhoneControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private RegistrationRepository registrationRepository;

    @Test
    void validatesTheDocumentedNumberField() throws Exception {
        mvc.perform(post("/api/phone/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"number\":\"123321\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.number").value("123321"))
            .andExpect(jsonPath("$.isValid").value(true))
            .andExpect(jsonPath("$.rules.hasNonZeroDigit").value(true))
            .andExpect(jsonPath("$.rules.sumFirstEqualsLast").value(true))
            .andExpect(jsonPath("$.rules.sumOddEqualsEven").value(true));
    }

    @Test
    void rejectsAnInvalidNumberFormat() throws Exception {
        mvc.perform(post("/api/phone/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"number\":\"12345\"}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.isValid").value(false));
    }

    @Test
    void rejectsMissingNumberField() throws Exception {
        mvc.perform(post("/api/phone/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"phone\":\"123321\"}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.isValid").value(false));
    }
}
