package com.example.demo;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Component
@RestController
@RequestMapping("api")
public class WebApiController {
	
    // 1分ごとに実行
    @Scheduled(fixedRate = 60000)
    public void myTask() {
        System.out.println("定期的なタスクが実行されました。");
        // ここにタスクの実装を追加する
    }

    @RequestMapping(value = "hello", produces = MediaType.TEXT_PLAIN_VALUE)
    public String hello() {
        return "SpringBoot!";
    }

    /**
     * "http://localhost:8082/api/weather/tokyo" でアクセス。
     * @return 東京の天気情報
     */
    @RequestMapping(value = "weather/tokyo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getTokyoWeather() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            final String endpoint = "https://api.open-meteo.com/v1/forecast";
            final String parameters = "?latitude=35.6785&longitude=139.6823&hourly=temperature_2m";

            final String url = endpoint + parameters;

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String json = response.getBody();
                return ResponseEntity.ok(json);
            } else {
                // ステータスコードとレスポンスボディをログに出力
                System.out.println("Error: " + response.getStatusCode() + " - " + response.getBody());
                return ResponseEntity.status(response.getStatusCode()).body("Error: Unable to fetch weather data");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}


