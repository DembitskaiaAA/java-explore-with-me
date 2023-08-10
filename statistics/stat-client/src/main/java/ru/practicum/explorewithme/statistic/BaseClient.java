package ru.practicum.explorewithme.statistic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.practicum.explorewithme.EndpointHitDto;
import ru.practicum.explorewithme.ViewStatsDto;
import ru.practicum.explorewithme.exceptions.StatisticException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseClient {
    protected final RestTemplate restTemplate;
    protected final String serverUrl;

    public BaseClient(RestTemplate restTemplate, String serverUrl) {
        this.restTemplate = restTemplate;
        this.serverUrl = serverUrl;
    }

    public void postStat(HttpServletRequest request, String app) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        EndpointHitDto endpointHitDto = new EndpointHitDto();
        endpointHitDto.setApp(app);
        endpointHitDto.setUri(request.getRequestURI());
        endpointHitDto.setIp(request.getRemoteAddr());
        endpointHitDto.setTimestamp(LocalDateTime.now());

        HttpEntity<EndpointHitDto> requestEntity = new HttpEntity<>(endpointHitDto, headers);
        restTemplate.exchange(serverUrl + "/hit", HttpMethod.POST, requestEntity, EndpointHitDto.class);
    }

    public void postStat(String uri, HttpServletRequest request, String app) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        EndpointHitDto endpointHitDto = new EndpointHitDto();
        endpointHitDto.setApp(app);
        endpointHitDto.setUri(uri);
        endpointHitDto.setIp(request.getRemoteAddr());
        endpointHitDto.setTimestamp(LocalDateTime.now());

        HttpEntity<EndpointHitDto> requestEntity = new HttpEntity<>(endpointHitDto, headers);
        restTemplate.exchange(serverUrl + "/hit", HttpMethod.POST, requestEntity, EndpointHitDto.class);
    }

    public List<ViewStatsDto> getStat(String start, String end, List<String> uris, Boolean unique) {

        Map<String, Object> parameters = new HashMap<>();
        String[] uriForRequest = uris.toArray(new String[0]);

        parameters.put("start", start);
        parameters.put("end", end);
        parameters.put("uris", uriForRequest);
        parameters.put("unique", unique);

        ResponseEntity<String> response = restTemplate.getForEntity(
                serverUrl + "/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                String.class, parameters);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return Arrays.asList(objectMapper.readValue(response.getBody(), ViewStatsDto[].class));
        } catch (JsonProcessingException exception) {
            throw new StatisticException(String.format("Json processing error: %s", exception.getMessage()));
        }
    }

}