package io.octa.cluster.etcdclient.commands.members;

import java.net.URI;
import java.util.function.Consumer;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;

import io.octa.cluster.etcdclient.exceptions.MalformedEtcdResponseException;
import io.octa.cluster.etcdclient.model.EtcdMember;
import io.octa.cluster.etcdclient.model.EtcdResponse;

public class EtcdMembersLeaderCommand extends EtcdMembersCommand<EtcdResponse<EtcdMember>> {
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
        emitter.accept("/leader");
    }

    @Override
    protected RequestBuilder createRequestBuilder(URI uri) {
        return RequestBuilder.get(uri);
    }
}
