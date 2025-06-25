package com.example.crawler.config.crawler.context.devtools;

import com.example.crawler.data.model.NetworkModel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Getter
@Setter
public class DevtoolsContext {

    private final Map<String, NetworkModel> networkRequests = new ConcurrentHashMap<>();

    private final List<NetworkModel> completedRequests = new ArrayList<>();

    public void putNetworkRequest(NetworkModel networkModel) {
        log.debug("[DevtoolsContext] putNetworkRequest id: {}", networkModel.getRequestIdString());
        networkRequests.put(networkModel.getRequestIdString(), networkModel);
    }

    public NetworkModel getNetworkRequest(String requestId) {
        return networkRequests.get(requestId);
    }

    public void addCompleteRequest(NetworkModel networkModel) {
        synchronized (completedRequests) {
            log.debug("[DevtoolsContext] addCompleteRequest id: {}", networkModel.getRequestIdString());
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


}
