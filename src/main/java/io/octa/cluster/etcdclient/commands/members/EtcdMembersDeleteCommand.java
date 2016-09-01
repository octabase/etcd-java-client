package io.octa.cluster.etcdclient.commands.members;

import java.math.BigInteger;
import java.net.URI;
import java.util.function.Consumer;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;

import io.octa.cluster.etcdclient.model.EtcdId;
// TODO
public class EtcdMembersDeleteCommand extends EtcdMembersCommand {
    private EtcdId memberId;

    public EtcdId getMemberId() {
        return memberId;
    }

    public EtcdMembersDeleteCommand withMemberId(String memberId) {
        return withMemberId(new EtcdId(memberId));
    }

    public EtcdMembersDeleteCommand withMemberId(BigInteger memberId) {
        return withMemberId(new EtcdId(memberId));
    }

    public EtcdMembersDeleteCommand withMemberId(EtcdId memberId) {
        this.memberId = memberId;

        return this;
    }

    @Override
    public Object readResponse(HttpResponse httpResponse) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void buildUriPathElements(Consumer emitter) {
        // TODO Auto-generated method stub

    }

    @Override
    protected RequestBuilder createRequestBuilder(URI uri) {
        // TODO Auto-generated method stub
        return null;
    }
}
