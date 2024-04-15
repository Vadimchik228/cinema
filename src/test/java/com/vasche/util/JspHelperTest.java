package com.vasche.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class JspHelperTest {

    @Test
    void get() {
        final String path = "login";

        final String actualResult = JspHelper.get(path);

        Assertions.assertThat(actualResult)
                .isEqualTo("\\WEB-INF\\jsp\\login.jsp");
    }
}
