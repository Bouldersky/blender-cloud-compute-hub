package com.blender.hub.computehub.entity.manager;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Hostname {
    String scheme;
    String hostname;
    int port;
}
