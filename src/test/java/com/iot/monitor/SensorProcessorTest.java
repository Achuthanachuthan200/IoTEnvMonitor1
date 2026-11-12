package com.iot.monitor;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SensorProcessorTest {
    @Test
    public void testClassification() {
        assertEquals("Normal", SensorProcessor.classifyStatus(25.0, 50.0, 1013.0));
        assertEquals("Warning", SensorProcessor.classifyStatus(34.0, 75.0, 1010.0));
        assertEquals("Critical", SensorProcessor.classifyStatus(41.0, 90.0, 980.0));
    }
}