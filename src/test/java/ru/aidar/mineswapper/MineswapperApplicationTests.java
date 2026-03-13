package ru.aidar.mineswapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MineswapperApplicationTests {

    @Autowired
    private MineswapperApplication application;

    @DisplayName("Should load application context")
    @Test
    void contextLoads() {
        assertThat(application).isNotNull();
    }

}
