package ru.practicum.explorewithme.statistic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.explorewithme.ViewStatsDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class StatisticClient extends BaseClient {

    @Autowired
    public StatisticClient(@Value("${stats-client}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build(),
                serverUrl);
    }

    public void postStat(HttpServletRequest request, String app) {
        super.postStat(request, app);
    }

    public void postStat(String uri, HttpServletRequest request, String app) {
        super.postStat(uri, request, app);
    }

    public List<ViewStatsDto> getStat(String start, String end, List<String> uris, Boolean unique) {
        return super.getStat(start, end, uris, unique);
    }
}