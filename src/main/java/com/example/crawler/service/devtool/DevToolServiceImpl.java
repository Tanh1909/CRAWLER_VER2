package com.example.crawler.service.devtool;

import com.example.crawler.config.crawler.context.devtools.DevtoolsContext;
import com.example.crawler.config.crawler.context.devtools.DevtoolsContextHolder;
import com.example.crawler.data.model.NetworkModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v114.network.Network;
import org.openqa.selenium.devtools.v114.network.model.Request;
import org.openqa.selenium.devtools.v114.network.model.RequestId;
import org.openqa.selenium.devtools.v114.network.model.Response;
import org.springframework.stereotype.Service;

import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class DevToolServiceImpl implements IDevToolsService {


    @Override
    public void handleDevTools(DevTools devTools) {
        DevtoolsContext context = DevtoolsContextHolder.getContext();

        devTools.addListener(Network.requestWillBeSent(), requestWillBeSent -> {
            RequestId requestId = requestWillBeSent.getRequestId();
            Request request = requestWillBeSent.getRequest();
            log.trace("[RequestWillBeSent: {}] {}", requestId, request.toString());
            String url = request.getUrl();
            String method = request.getMethod();
            Map<String, Object> headers = request.getHeaders().toJson();
            Long timestamp = System.currentTimeMillis();
            NetworkModel networkModel = new NetworkModel()
                    .setRequestId(requestId)
                    .setUrl(url)
                    .setMethod(method)
                    .setHeaders(headers)
                    .setTimestamp(timestamp);
            if (request.getHasPostData().isPresent()) {
                networkModel.setRequestBody(request.getPostData().orElse(null));
            }
            context.putNetworkRequest(networkModel);
        });

        devTools.addListener(Network.responseReceived(), responseReceived -> {
            String requestId = responseReceived.getRequestId().toString();
            Response response = responseReceived.getResponse();
            log.trace("[ResponseReceived: {}] {}", requestId, response.toString());

            NetworkModel networkModel = context.getNetworkRequest(requestId);
            if (networkModel != null) {
                networkModel.setStatusCode(response.getStatus());
                getResponseBody(networkModel, devTools);
            }
        });

        devTools.addListener(Network.loadingFinished(), loadingFinished -> {
            String requestId = loadingFinished.getRequestId().toString();
            log.trace("[LoadingFinished: {}] {}", requestId, loadingFinished.getTimestamp());
            NetworkModel networkModel = context.getNetworkRequest(requestId);
            if (networkModel != null && !networkModel.isComplete()) {
                getResponseBody(networkModel, devTools);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                context.addCompleteRequest(networkModel);
            }

        });
    }

    private void getResponseBody(NetworkModel networkModel, DevTools devTools) {
        try {
            String responseBody = devTools.send(Network.getResponseBody(networkModel.getRequestId())).getBody();
            networkModel.setResponseBody(responseBody);
            networkModel.setComplete(true);
        } catch (Exception e) {
            log.trace("Error when getResponseBody[requestId: {}]: {}:", networkModel.getRequestIdString(), e.getMessage());
            networkModel.setResponseBody("");
            networkModel.setError(true);
        }
    }

}
