package com.blender.hub.computehub.entrypoint.manager;

import com.blender.hub.computehub.core.hmac.entity.HmacSecret;
import com.blender.hub.computehub.core.hmac.usecase.CreateHmacSecretImpl;
import com.blender.hub.computehub.entrypoint.manager.requests.CreateLinkingHmacKeyRequest;
import com.blender.hub.computehub.entrypoint.manager.response.CreateLinkingHmacKeyResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ManagerLinkingProvider {
    CreateHmacSecretImpl createHmacSecret;

    CreateLinkingHmacKeyResponse createLinkingHmac(CreateLinkingHmacKeyRequest request) {
        HmacSecret secret = createHmacSecret.newLinkTimeHmacSecret(request.getKey());
        log.debug("New linking hmac secret created with id: {}", secret.id);
        return CreateLinkingHmacKeyResponse.builder().identifier(secret.id).build();
    }
}