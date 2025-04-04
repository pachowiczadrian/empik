package pl.pachowicz.empik.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
class RestClientConfiguration {

    @Bean
    IpApiRestClient ipApiClient() {
        RestClient restClient = RestClient.builder().baseUrl("https://api.country.is/").build();
        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return factory.createClient(IpApiRestClient.class);
    }

}
