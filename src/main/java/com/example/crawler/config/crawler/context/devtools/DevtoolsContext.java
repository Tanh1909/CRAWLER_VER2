package com.example.crawler.config.crawler.context.devtools;

import com.example.crawler.data.model.NetworkModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Getter
@Setter
public class DevtoolsContext {

    private final Map<String, NetworkModel> networkRequests = new ConcurrentHashMap<>();

    private final List<NetworkModel> completedRequests = new ArrayList<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void putNetworkRequest(NetworkModel networkModel) {
        log.trace("[DevtoolsContext] putNetworkRequest id: {}", networkModel.getRequestIdString());
        networkRequests.put(networkModel.getRequestIdString(), networkModel);
    }

    public NetworkModel getNetworkRequest(String requestId) {
        return networkRequests.get(requestId);
    }

    public void addCompleteRequest(NetworkModel networkModel) {
        synchronized (completedRequests) {
            log.trace("[DevtoolsContext] addCompleteRequest id: {}", networkModel.getRequestIdString());
            if (networkModel.isComplete()) {
                completedRequests.add(networkModel);
                networkRequests.remove(networkModel.getRequestIdString());
            }
        }
    }

    public List<NetworkModel> getCompletedRequests() {
        synchronized (completedRequests) {
            return new ArrayList<>(completedRequests);
        }
    }

    public List<JsonNode> getCompletedResponse(String path, String method) {
        synchronized (completedRequests) {
            return completedRequests.stream()
                    .filter(networkModel -> networkModel.getMethod().equalsIgnoreCase(method))
                    .filter(networkModel -> networkModel.getUrl().contains(path))
                    .map(networkModel -> {
                        try {
                            return objectMapper.readTree(networkModel.getResponseBody());
                        } catch (JsonProcessingException e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList();
        }
    }

}
