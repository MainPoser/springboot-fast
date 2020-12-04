package cn.com.datu;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author tianyao
 */
@SpringBootApplication
@Slf4j
public class PipOpenApiApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(PipOpenApiApplication.class, args);
            log.info("PipOpenApiApplication start success");
        } catch (Exception e) {
            log.error("PipOpenApiApplication start failed:<{}>", ExceptionUtils.getFullStackTrace(e));
        }

    }

}
