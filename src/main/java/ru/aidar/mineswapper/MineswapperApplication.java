package ru.aidar.mineswapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = MinesweeperScanMarker.class)
public class MineswapperApplication {

    public static void main(String[] args) {
        SpringApplication.run(MineswapperApplication.class, args);
    }

}
