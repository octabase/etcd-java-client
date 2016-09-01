package io.octa.cluster.etcdclient.commands.members;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;

import com.fasterxml.jackson.databind.JsonNode;

import io.octa.cluster.etcdclient.exceptions.MalformedEtcdResponseException;
import io.octa.cluster.etcdclient.model.EtcdMember;
import io.octa.cluster.etcdclient.model.EtcdResponse;

public class EtcdMembersListCommand extends EtcdMembersCommand<EtcdResponse<List<EtcdMember>>> {
    @Override
    public EtcdResponse<List<EtcdMember>> readResponse(HttpResponse httpResponse) {
        EtcdResponse<List<EtcdMember>> resp = new EtcdResponse<>();

        try {
            resp.setClusterId(readClusterId(httpResponse));
            resp.setPayload(new ArrayList<>());

            JsonNode node = jsonMapper.readTree(httpResponse.getEntity().getContent());

            JsonNode membersNode = node.get("members");
            if (membersNode == null) {
                throw new MalformedEtcdResponseException("`members` parameter not found.");
            }

            if (membersNode.isNull()) {
                throw new MalformedEtcdResponseException("`members` parameter cannot be null.");
            }

            if (!membersNode.isArray()) {
                throw new MalformedEtcdResponseException("`members` parameter must be an array.");
            }

            membersNode.elements().forEachRemaining(n -> resp.getPayload().add(readMember(n)));

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
        return RequestBuilder.get(uri);
    }
}
