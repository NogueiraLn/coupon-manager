package com.coupon.manager.controller;

import com.coupon.manager.dto.CouponDTO;
import com.coupon.manager.service.CouponService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CouponController.class)
class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CouponService service;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldReturnCouponWhenFindById() throws Exception {
        UUID id = UUID.randomUUID();

        CouponDTO dto = new CouponDTO();
        dto.setId(id);
        dto.setCode("ABC123");
        dto.setDescription("Test Coupon");
        dto.setExpirationDate("2029-12-31T23:59:59Z");
        dto.setDiscountValue(0.8);
        dto.setPublished(true);
        dto.setRedeemed(false);

        when(service.findById(id)).thenReturn(dto);

        mockMvc.perform(get("/coupon/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.code").value("ABC123"))
                .andExpect(jsonPath("$.description").value("Test Coupon"))
                .andExpect(jsonPath("$.discountValue").value(0.8));
    }

    @Test
    void shouldInsertCouponAndReturnCreated() throws Exception {
        UUID id = UUID.randomUUID();

        CouponDTO request = new CouponDTO();
        request.setCode("ABC123");
        request.setDescription("Test Coupon");
        request.setExpirationDate("2026-12-31T23:59:59Z");
        request.setDiscountValue(0.8);

        CouponDTO response = new CouponDTO();
        response.setId(id);
        response.setCode("ABC123");
        response.setDescription("Test Coupon");
        response.setExpirationDate("2026-12-31T23:59:59Z");
        response.setDiscountValue(0.8);

        when(service.insert(any(CouponDTO.class))).thenReturn(response);

        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.code").value("ABC123"));
    }

    @Test
    void shouldReturnBadRequestWhenInsertWithInvalidData() throws Exception {
        CouponDTO request = new CouponDTO();
        request.setCode("");
        request.setDescription("");
        request.setExpirationDate("");
        request.setDiscountValue(0);

        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteCouponSuccessfully() throws Exception {
        UUID id = UUID.randomUUID();

        doNothing().when(service).delete(id);

        mockMvc.perform(delete("/coupon/{id}", id))
                .andExpect(status().isNoContent());
    }
}
