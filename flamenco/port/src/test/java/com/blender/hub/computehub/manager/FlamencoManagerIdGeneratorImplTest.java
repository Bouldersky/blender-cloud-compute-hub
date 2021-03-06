package com.blender.hub.computehub.manager;

import com.blender.hub.computehub.port.manager.ManagerIdGeneratorImpl;
import com.blender.hub.computehub.usecase.manager.port.driven.ManagerIdGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

class FlamencoManagerIdGeneratorImplTest {

    @Test
    void generateIsUnique() {
        ManagerIdGenerator generator = new ManagerIdGeneratorImpl();

        List<String> ids = Stream.generate(generator::generate)
                .limit(1000)
                .sorted()
                .collect(Collectors.toList());

        for (int i = 0; i < ids.size() - 1; i++) {
            assertNotEquals(ids.get(i), ids.get(i+1), "ids must be unique");
        }
    }
}