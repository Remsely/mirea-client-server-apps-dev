package edu.mirea.remsely.csad.practice8.races.client;

import edu.mirea.remsely.csad.practice8.commons.drivers.dto.DriverDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "driver-service")
public interface DriverClient {
    @GetMapping("/api/drivers/{id}")
    DriverDto getDriverById(@PathVariable Long id);
}
