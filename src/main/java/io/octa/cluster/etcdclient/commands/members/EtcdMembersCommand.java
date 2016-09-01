package io.octa.cluster.etcdclient.commands.members;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;

import io.octa.cluster.etcdclient.command.EtcdCommand;
import io.octa.cluster.etcdclient.exceptions.MalformedEtcdResponseException;
import io.octa.cluster.etcdclient.model.EtcdId;
import io.octa.cluster.etcdclient.model.EtcdMember;

public abstract class EtcdMembersCommand<T> extends EtcdCommand<T> {
    protected final static String API_PATH = "/v2/members";

    public EtcdMember readMember(JsonNode memberNode) {
        try {
            JsonNode idNode = memberNode.get("id");
            JsonNode nameNode = memberNode.get("name");
            JsonNode peerUrlsNode = memberNode.get("peerURLs");
            JsonNode clientUrlsNode = memberNode.get("clientURLs");

            EtcdMember member = new EtcdMember();
            member.setPeerURLs(new ArrayList<>());
            member.setClientURLs(new ArrayList<>());

            if ((idNode != null) && !idNode.isNull() && idNode.isTextual()) {
                member.setId(new EtcdId(idNode.asText()));

            } else {
                throw new MalformedEtcdResponseException("`idNode` parameter missing or malformed.");
            }

            if ((nameNode != null) && !nameNode.isNull() && nameNode.isTextual()) {
                member.setName(nameNode.asText());
            }

            if ((peerUrlsNode != null) && !peerUrlsNode.isNull() && peerUrlsNode.isArray()) {
                peerUrlsNode.elements().forEachRemaining(urlNode -> {
                    if ((urlNode != null) && !urlNode.isNull() && urlNode.isTextual()) {
                        member.getPeerURLs().add(urlNode.asText());

                    } else {
                        throw new MalformedEtcdResponseException("`peerUrls[i]` parameter missing or malformed.");
                    }
                });
            }

            if ((clientUrlsNode != null) && !clientUrlsNode.isNull() && clientUrlsNode.isArray()) {
                clientUrlsNode.elements().forEachRemaining(urlNode -> {
                    if ((urlNode != null) && !urlNode.isNull() && urlNode.isTextual()) {
                        member.getClientURLs().add(urlNode.asText());

                    } else {
                        throw new MalformedEtcdResponseException("`peerUrls[i]` parameter missing or malformed.");
                    }
                });
            }

            return member;

        } catch (Exception e) {
            throw new MalformedEtcdResponseException("unable to read etcd member: " + e.getMessage(), e);
        }
    }
}
