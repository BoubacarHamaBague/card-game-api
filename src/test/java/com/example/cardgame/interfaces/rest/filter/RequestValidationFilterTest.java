package com.example.cardgame.interfaces.rest.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class RequestValidationFilterTest {

    private RequestValidationFilter filter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockFilterChain filterChain;

    @BeforeEach
    void setUp() {
        filter = new RequestValidationFilter();
        response = new MockHttpServletResponse();
        filterChain = new MockFilterChain();
    }

    @Test
    void givenValidUriWithoutDoubleSlash_whenFilter_thenContinueChain() throws Exception {
        // Arrange
        request = new MockHttpServletRequest("GET", "/api/games");

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertThat(response.getStatus()).isEqualTo(200); // Default status when chain continues
    }

    @Test
    void givenUriWithDoubleSlash_whenFilter_thenReturn400() throws Exception {
        // Arrange
        request = new MockHttpServletRequest("GET", "/api//games");

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getContentType()).isEqualTo("application/json");
        assertThat(response.getContentAsString()).contains("Invalid URL format");
    }

    @Test
    void givenUriWithMultipleDoubleSlashes_whenFilter_thenReturn400() throws Exception {
        // Arrange
        request = new MockHttpServletRequest("POST", "/api//games//decks");

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    void givenUriWithDoubleSlashInMiddle_whenFilter_thenReturn400() throws Exception {
        // Arrange
        request = new MockHttpServletRequest("GET", "/api/games//123");

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertThat(response.getStatus()).isEqualTo(400);
    }
}
