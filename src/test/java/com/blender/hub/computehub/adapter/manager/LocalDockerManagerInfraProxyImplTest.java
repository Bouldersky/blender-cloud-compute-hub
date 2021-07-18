package com.blender.hub.computehub.adapter.manager;

import com.blender.hub.computehub.configuration.services.ManagerDockerContainerConfig;
import com.blender.hub.computehub.core.manager.entity.Hostname;
import com.blender.hub.computehub.core.manager.entity.Manager;
import com.blender.hub.computehub.core.manager.port.adapter.ManagerInfraProxy;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerCmd;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocalDockerManagerInfraProxyImplTest {
    private static final String IMAGE_NAME = "flamencoManager:1";
    private static final int FLAMENCO_PORT = 8000;
    private static final String CONTAINER_ID = "container-id";
    private static final int CONTAINER_PORT = 45678;

    @Mock
    DockerClient dockerClient;

    @Mock(answer = Answers.RETURNS_SELF)
    CreateContainerCmd createContainerCmd;

    @Mock(answer = Answers.RETURNS_SELF)
    InspectContainerCmd inspectContainerCmd;

    Manager manager;
    ManagerInfraProxy infraProxy;

    @BeforeEach
    void setUp() {
        manager = Manager.builder().id(UUID.randomUUID().toString()).build();

        // infraProxy setup
        ManagerDockerContainerConfig config = new ManagerDockerContainerConfig();
        config.setImageName(IMAGE_NAME);
        config.setApiPort(FLAMENCO_PORT);
        infraProxy = new LocalDockerManagerInfraProxyImpl(dockerClient, config);

        // dockerClient mock setup
        when(dockerClient.createContainerCmd(anyString()))
                .thenReturn(createContainerCmd);
        CreateContainerResponse createContainerResponse = new CreateContainerResponse();
        createContainerResponse.setId(CONTAINER_ID);
        when(createContainerCmd.exec()).thenReturn(createContainerResponse);

        InspectContainerResponse inspectResponse = mock(InspectContainerResponse.class);
        NetworkSettings networkSettings = mock(NetworkSettings.class);
        when(dockerClient.inspectContainerCmd(CONTAINER_ID)).thenReturn(inspectContainerCmd);
        when(inspectContainerCmd.exec()).thenReturn(inspectResponse);
        when(inspectResponse.getNetworkSettings()).thenReturn(networkSettings);
        Ports ports = new Ports();
        ports.add(new PortBinding(Ports.Binding.bindPort(CONTAINER_PORT), ExposedPort.tcp(FLAMENCO_PORT)));
        when(networkSettings.getPorts()).thenReturn(ports);
    }

    @Test
    void containerIsCreated() {
        infraProxy.createInfraFor(manager);
        verify(dockerClient, times(1)).createContainerCmd(IMAGE_NAME);
        verify(createContainerCmd, times(1)).withName(manager.getId());
        verify(createContainerCmd, times(1)).withExposedPorts(ExposedPort.tcp(FLAMENCO_PORT));
        verify(createContainerCmd, times(1)).exec();
    }

    @Test
    void containerHostnameIsReturned() {
        infraProxy.createInfraFor(manager);
        verify(dockerClient, times(1)).inspectContainerCmd(CONTAINER_ID);

        Hostname returnedHostname = infraProxy.createInfraFor(manager);
        assertEquals(new Hostname("localhost:" + CONTAINER_PORT), returnedHostname);
    }
}