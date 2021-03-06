package com.github.bottomlessarchive.urlcollector.validator.service;

import com.github.bottomlessarchive.urlcollector.validator.configuration.ValidationConfigurationProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class URLValidatorTest {

    @Mock
    private ValidationConfigurationProperties validationConfigurationProperties;

    @InjectMocks
    private URLValidator urlValidator;

    @Test
    void validatingEmptyStringShouldReturnFalse() {
        boolean result = urlValidator.validateUrl("");

        assertThat(result).isFalse();
    }

    @Test
    void validatingBadURLShouldReturnFalse() {
        boolean result = urlValidator.validateUrl("http://badurl--2");

        assertThat(result).isFalse();
    }

    @Test
    void validatingGoodURLThatHasBadExtensionShouldReturnFalse() {
        when(validationConfigurationProperties.getTypes())
                .thenReturn(List.of("pdf"));

        boolean result = urlValidator.validateUrl("http://example.com/something.abc");

        assertThat(result).isFalse();
    }

    @Test
    void validatingGoodURLThatDoesntStartWithHttpOrHttps() {
        boolean result = urlValidator.validateUrl("ftp://example.com/something.abc");

        assertThat(result).isFalse();
    }

    @Test
    void validatingGoodURLThatStartWithHttpAndHasGoodExtensionShouldReturnTrue() {
        when(validationConfigurationProperties.getTypes())
                .thenReturn(List.of("pdf"));

        boolean result = urlValidator.validateUrl("http://example.com/something.pdf");

        assertThat(result).isTrue();
    }

    @Test
    void validatingGoodURLThatStartWithHttpsAndHasGoodExtensionShouldReturnTrue() {
        when(validationConfigurationProperties.getTypes())
                .thenReturn(List.of("pdf"));

        boolean result = urlValidator.validateUrl("https://example.com/something.pdf");

        assertThat(result).isTrue();
    }

    @Test
    void validatingGoodURLThatHasGoodExtensionShouldReturnTrue() {
        when(validationConfigurationProperties.getTypes())
                .thenReturn(List.of("pdf"));

        boolean result = urlValidator.validateUrl("http://example.com/something.pdf");

        assertThat(result).isTrue();
    }
}