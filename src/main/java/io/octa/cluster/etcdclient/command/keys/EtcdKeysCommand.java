package io.octa.cluster.etcdclient.command.keys;

import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;

import io.octa.cluster.etcdclient.command.EtcdCommand;
import io.octa.cluster.etcdclient.exceptions.EtcdKeyNotFoundException;
import io.octa.cluster.etcdclient.exceptions.EtcdRemoteException;
import io.octa.cluster.etcdclient.exceptions.MalformedEtcdResponseException;
import io.octa.cluster.etcdclient.model.EtcdKeysResponse;
import io.octa.cluster.etcdclient.model.EtcdNode;
import io.octa.cluster.etcdclient.model.EventAction;

public abstract class EtcdKeysCommand extends EtcdCommand<EtcdKeysResponse> {
    protected final static String API_PATH = "/v2/keys";

    private final static int NODES_MAX_DEPTH = 10_000;
    private final static DateTimeFormatter expirationDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.nX");

    private String prefix = null;

    @Override
    public void handleError(Throwable cause) {
        if (cause instanceof EtcdRemoteException) {
            EtcdRemoteException err = (EtcdRemoteException) cause;
            if (err.getEtcdErrorCode() == 100) {
                throw new EtcdKeyNotFoundException(err, "Key not found: " + err.getEtcdErrorCause());
            }
        }
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    @Override
    public EtcdKeysResponse readResponse(HttpResponse httpResponse) {
        EtcdKeysResponse resp = new EtcdKeysResponse();

        readHeaders(httpResponse, resp);

        try {
            JsonNode node = jsonMapper.readTree(httpResponse.getEntity().getContent());

            JsonNode actionNode = node.get("action");
            if (actionNode == null) {
                throw new MalformedEtcdResponseException("`action` parameter not found.");
            }

            if (actionNode.isNull()) {
                throw new MalformedEtcdResponseException("`action` parameter cannot be null.");
            }

            if (!actionNode.isTextual()) {
                throw new MalformedEtcdResponseException("`action` parameter must be string.");
            }

            resp.setAction(EventAction.fromEtcdActionText(actionNode.asText()));

            JsonNode refreshNode = node.get("refresh");
            if ((refreshNode != null) && !refreshNode.isNull()) {
                if (refreshNode.isBoolean()) {
                    resp.setRefresh(refreshNode.asBoolean());
                } else {
                    throw new MalformedEtcdResponseException("`refresh` parameter must be boolean.");
                }
            }

            resp.setPayload(readEtcdNode(node.get("node"), NODES_MAX_DEPTH, 1));
            resp.setPrevNode(readEtcdNode(node.get("prevNode"), NODES_MAX_DEPTH, 1));

        } catch (Exception e) {
            throw new MalformedEtcdResponseException("unable to read etcd response: " + e.getMessage(), e);
        }

        return resp;
    }

    public void readHeaders(HttpResponse httpResponse, EtcdKeysResponse resp) {
        resp.setClusterId(readClusterId(httpResponse));

        Header[] etcdIndexHdrs = httpResponse.getHeaders("X-Etcd-Index");
        Header[] raftIndexHdrs = httpResponse.getHeaders("X-Raft-Index");
        Header[] raftTermHdrs = httpResponse.getHeaders("X-Raft-Term");

        if ((etcdIndexHdrs == null) || (etcdIndexHdrs.length == 0)) {
            throw new MalformedEtcdResponseException("`X-Etcd-Index` header not found.");

        } else if (etcdIndexHdrs.length != 1) {
            throw new MalformedEtcdResponseException("`multiple X-Etcd-Index` header was found.");
        }

        if ((raftIndexHdrs == null) || (raftIndexHdrs.length == 0)) {
            throw new MalformedEtcdResponseException("`X-Raft-Index` header not found.");

        } else if (raftIndexHdrs.length != 1) {
            throw new MalformedEtcdResponseException("`multiple X-Raft-Index` header was found.");
        }

        if ((raftTermHdrs == null) || (raftTermHdrs.length == 0)) {
            throw new MalformedEtcdResponseException("`X-Raft-Term` header not found.");

        } else if (raftTermHdrs.length != 1) {
            throw new MalformedEtcdResponseException("`multiple X-Raft-Term` header was found.");
        }

        try {
            resp.setRaftIndex(new BigInteger(raftIndexHdrs[0].getValue()));

        } catch (NumberFormatException e) {
            throw new MalformedEtcdResponseException("unable to parse `X-Raft-Index` header('" + raftIndexHdrs[0].getValue() + "'): " + e.getMessage(), e);
        }

        try {
            resp.setRaftTerm(new BigInteger(raftTermHdrs[0].getValue()));

        } catch (NumberFormatException e) {
            throw new MalformedEtcdResponseException("unable to parse `X-Raft-Term` header('" + raftTermHdrs[0].getValue() + "'): " + e.getMessage(), e);
        }

        try {
            resp.setEtcdIndex(new BigInteger(etcdIndexHdrs[0].getValue()));

        } catch (NumberFormatException e) {
            throw new MalformedEtcdResponseException("unable to parse `X-Etcd-Index` header('" + etcdIndexHdrs[0].getValue() + "'): " + e.getMessage(), e);
        }
    }

    private EtcdNode readEtcdNode(JsonNode json, int maxDepth, int depth) {
        if (depth == maxDepth) {
            throw new MalformedEtcdResponseException("maximum recursion depth(" + maxDepth + ") exceeded while reading an etcd node from a response.");
        }

        if ((json == null) || json.isNull()) {
            return null;
        }

        EtcdNode node = new EtcdNode();

        JsonNode keyNode = json.get("key");
        if (keyNode != null) {
            if (keyNode.isNull()) {
                throw new MalformedEtcdResponseException("`key` parameter cannot be null.");
            }

            if (!keyNode.isTextual()) {
                throw new MalformedEtcdResponseException("`key` parameter must be string.");
            }

            node.setKey(keyNode.asText());
        }

        JsonNode valueNode = json.get("value");
        if ((valueNode != null) && !valueNode.isNull()) {
            if (!valueNode.isTextual()) {
                throw new MalformedEtcdResponseException("`value` parameter must be string.");
            }

            node.setValue(valueNode.asText());
        }

        JsonNode dirNode = json.get("dir");
        if (dirNode != null) {
            if (dirNode.isNull()) {
                throw new MalformedEtcdResponseException("`dir` parameter cannot be null.");
            }

            if (!dirNode.isBoolean()) {
                throw new MalformedEtcdResponseException("`dir` parameter must be boolean.");
            }

            node.setDir(dirNode.asBoolean());
        } else {
            node.setDir(false);
        }

        JsonNode expirationNode = json.get("expiration");
        if ((expirationNode != null) && !expirationNode.isNull()) {
            if (!expirationNode.isTextual()) {
                throw new MalformedEtcdResponseException("`expiration` parameter must be string.");
            }

            try {
                node.setExpiration(OffsetDateTime.parse(expirationNode.asText(), expirationDateFormatter));

            } catch (DateTimeParseException e) {
                throw new MalformedEtcdResponseException("unable to parse `expiration` parameter('" + expirationNode.asText() + "'): " + e.getMessage(), e);
            }
        }

        JsonNode ttlNode = json.get("ttl");
        if ((ttlNode != null) && !ttlNode.isNull()) {
            if (ttlNode.isNumber()) {
                node.setTtlInSeconds(ttlNode.asLong());

            } else {
                throw new MalformedEtcdResponseException("`ttl` parameter must be integer.");
            }
        }

        node.setNodes(new ArrayList<>());
        JsonNode nodesNode = json.get("nodes");
        if ((nodesNode != null) && !nodesNode.isNull()) {
            if (nodesNode.isArray()) {
                for (int i = 0; i < nodesNode.size(); i++) {
                    node.getNodes()
                            .add(readEtcdNode(nodesNode.get(i), maxDepth, depth + 1));
                }
            } else {
                throw new MalformedEtcdResponseException("`nodes` parameter must be array.");
            }
        }

        JsonNode modIndexNode = json.get("modifiedIndex");
        if (modIndexNode != null) {
            if (modIndexNode.isNull()) {
                throw new MalformedEtcdResponseException("`modifiedIndex` parameter cannot be null.");
            }

            if (!modIndexNode.isNumber()) {
                throw new MalformedEtcdResponseException("`modifiedIndex` parameter must be integer.");
            }

            try {
                node.setModifiedIndex(new BigInteger(modIndexNode.asText()));

            } catch (NumberFormatException e) {
                throw new MalformedEtcdResponseException("unable to parse `modifiedIndex` parameter('" + modIndexNode.asText() + "'): " + e.getMessage(), e);
            }
        }

        JsonNode createdIndexNode = json.get("createdIndex");
        if (createdIndexNode != null) {
            if (createdIndexNode.isNull()) {
                throw new MalformedEtcdResponseException("`createdIndex` parameter cannot be null.");
            }

            if (!createdIndexNode.isNumber()) {
                throw new MalformedEtcdResponseException("`createdIndex` parameter must be integer.");
            }

            try {
                node.setCreatedIndex(new BigInteger(createdIndexNode.asText()));

            } catch (NumberFormatException e) {
                throw new MalformedEtcdResponseException("unable to parse `createdIndex` parameter('" + createdIndexNode.asText() + "'): " + e.getMessage(), e);
            }
        }

        return node;
    }
}
