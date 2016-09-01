package io.octa.cluster.etcdclient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.octa.cluster.etcdclient.commands.members.EtcdMembersCreateCommand;
import io.octa.cluster.etcdclient.commands.members.EtcdMembersLeaderCommand;
import io.octa.cluster.etcdclient.commands.members.EtcdMembersListCommand;
import io.octa.cluster.etcdclient.model.EtcdMember;
import io.octa.cluster.etcdclient.model.EtcdResponse;

public class EtcdMembersAsyncApi {
    private EtcdClient client;

    EtcdMembersAsyncApi(EtcdClient client) {
        this.client = client;
    }

    public CompletableFuture<EtcdResponse<List<EtcdMember>>> getMembers() {
        return client.execute(new EtcdMembersListCommand());
    }

    public CompletableFuture<EtcdResponse<EtcdMember>> getLeader() {
        return client.execute(new EtcdMembersLeaderCommand());
    }

    public CompletableFuture<EtcdResponse<EtcdMember>> addMember(String... peerUrls) {
        return client.execute(new EtcdMembersCreateCommand().withPeerUrls(peerUrls));
    }

    public CompletableFuture<EtcdResponse<EtcdMember>> addMember(Iterable<String> peerUrls) {
        return client.execute(new EtcdMembersCreateCommand().withPeerUrls(peerUrls));
    }
}
