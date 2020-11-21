package home.balda.cryptosign;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

@SpringBootApplication
public class CryptoSignApplication {

    public static void main(String[] args) {
        SpringApplication.run(CryptoSignApplication.class, args);
    }

    @Bean
    public HttpMessageConverters additionalConverters() {
        Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss.SSSZ").create();
        GsonHttpMessageConverter converter = new GsonHttpMessageConverter();
        converter.setGson(gson);
        return new HttpMessageConverters(converter);
    }

}
