package io.octa.cluster.etcdclient.commands.members;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.octa.cluster.etcdclient.exceptions.MalformedEtcdResponseException;
import io.octa.cluster.etcdclient.model.EtcdMember;
import io.octa.cluster.etcdclient.model.EtcdResponse;

public class EtcdMembersCreateCommand extends EtcdMembersCommand<EtcdResponse<EtcdMember>> {
    private ArrayList<String> peerUrls = new ArrayList<>();

    public List<String> getPeerUrls() {
        return Collections.unmodifiableList(peerUrls);
    }

    public EtcdMembersCreateCommand withPeerUrls(String... peerUrls) {
        this.peerUrls.addAll(Arrays.asList(peerUrls));

        return this;
    }

    public EtcdMembersCreateCommand withPeerUrls(Iterable<String> peerUrls) {
        peerUrls.forEach(this.peerUrls::add);

        return this;
    }

    @Override
    protected String buildReqBody() {
        HashMap<String, Object> req = new HashMap<>();

        req.put("PeerURLs", peerUrls);

        try {
            return jsonMapper.writeValueAsString(req);

        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public EtcdResponse<EtcdMember> readResponse(HttpResponse httpResponse) {
        EtcdResponse<EtcdMember> resp = new EtcdResponse<>();

        try {
            resp.setClusterId(readClusterId(httpResponse));

            resp.setPayload(readMember(jsonMapper.readTree(httpResponse.getEntity().getContent())));

        } catch (Exception e) {
            throw new MalformedEtcdResponseException("unable to read etcd response: " + e.getMessage(), e);
        }

        return resp;
    }

    @Override
    protected void buildUriPathElements(Consumer<String> emitter) {
        emitter.accept(API_PATH);
    }

    @Override
    protected RequestBuilder createRequestBuilder(URI uri) {
        return RequestBuilder.post(uri).addHeader("Content-Type", "application/json");
    }
}
