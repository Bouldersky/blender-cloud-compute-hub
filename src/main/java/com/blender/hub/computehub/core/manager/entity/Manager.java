package com.blender.hub.computehub.core.manager.entity;

import com.blender.hub.computehub.core.hmac.entity.HmacSecret;
import lombok.Builder;
import lombok.Data;
import org.joda.time.DateTime;

@Builder(toBuilder = true)
@Data
public class Manager {
    private final String id;
    private Hostname hostname;
    private HmacSecret hmacSecret;
    private ManagerState state;
    private final DateTime createdTs;

    public ManagerState getState() {
        return state;
    }

    public void setState(ManagerState state) {
        this.state = state;
    }
}
