package com.blender.hub.computehub.usecase.hmac.port.driven;

import com.blender.hub.computehub.entity.hmac.HmacSecret;
import com.blender.hub.computehub.usecase.hmac.usecase.HmacResetCommand;

public interface CreateHmacSecret {
    /**
     * Handle secret sent by a flamenco manager during linking
     * @param secretValue value of the secret to be created
     * @return generated sec
     */
    HmacSecret newLinkTimeHmacSecret(String secretValue);

    /**
     * @param oldSecret secret to be replaced
     * @return a freshly generated secret
     */
    HmacSecret refresh(HmacResetCommand hmacResetCommand) throws AuthenticationException;
}
