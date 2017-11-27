package com.makolab.rnd.mammoth.config;

import com.makolab.rnd.mammoth.ComponentScanIndicator;
import com.makolab.rnd.mammoth.service.hashing.CircleDotHashingService;
import com.makolab.rnd.mammoth.service.hashing.HashingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Rafał Trójczak (Rafal.Trojczak@makolab.net)
 */
@Configuration
@ComponentScan(
    basePackageClasses = {
      ComponentScanIndicator.class
    },
    excludeFilters = @ComponentScan.Filter(Controller.class)
)
public class RootContextConfiguration {

  @Primary
  @Bean
  public HashingService hashingService() {
    return new CircleDotHashingService();
  }
}