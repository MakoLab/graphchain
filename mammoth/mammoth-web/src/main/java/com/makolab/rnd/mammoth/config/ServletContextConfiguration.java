package com.makolab.rnd.mammoth.config;

import com.makolab.rnd.mammoth.controller.RestApi;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import java.nio.charset.Charset;
import java.util.List;

/**
 *
 * @author Rafał Trójczak (Rafal.Trojczak@makolab.net)
 */
@Configuration
@EnableWebMvc
@ComponentScan(
    basePackageClasses = {
      RestApi.class
    },
    useDefaultFilters = false,
    includeFilters = @ComponentScan.Filter(Controller.class)
)
@PropertySource("classpath:properties/repository.properties")
public class ServletContextConfiguration extends WebMvcConfigurerAdapter {

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
    converters.add(stringConverter);
  }
}
