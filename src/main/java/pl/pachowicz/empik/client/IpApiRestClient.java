package pl.pachowicz.empik.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface IpApiRestClient {

    @GetExchange("/{ip}")
    IpInfoDto getCountryByIp(@PathVariable("ip") String ip);
}
