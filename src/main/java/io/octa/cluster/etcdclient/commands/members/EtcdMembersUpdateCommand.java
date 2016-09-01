package io.octa.cluster.etcdclient.commands.members;

import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;

import io.octa.cluster.etcdclient.model.EtcdId;

// TODO
public class EtcdMembersUpdateCommand extends EtcdMembersCommand {
    private EtcdId memberId;
    private ArrayList<String> peerUrls = new ArrayList<>();

    public EtcdId getMemberId() {
        return memberId;
    }

    public List<String> getPeerUrls() {
        return Collections.unmodifiableList(peerUrls);
    }

    public EtcdMembersUpdateCommand withPeerUrls(String... peerUrls) {
        return withPeerUrls(Arrays.asList(peerUrls));
    }

    public EtcdMembersUpdateCommand withPeerUrls(List<String> peerUrls) {
        this.peerUrls.addAll(peerUrls);

        return this;
    }

    public EtcdMembersUpdateCommand withMemberId(String memberId) {
        return withMemberId(new EtcdId(memberId));
    }

    public EtcdMembersUpdateCommand withMemberId(BigInteger memberId) {
        return withMemberId(new EtcdId(memberId));
    }

    public EtcdMembersUpdateCommand withMemberId(EtcdId memberId) {
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
