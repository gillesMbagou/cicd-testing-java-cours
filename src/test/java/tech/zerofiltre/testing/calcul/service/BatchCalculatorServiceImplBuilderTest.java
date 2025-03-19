package tech.zerofiltre.testing.calcul.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
 class BatchCalculatorServiceImplBuilderTest {

    @Mock
    private CalculatorService calculatorServiceMock;

    @InjectMocks
    private BatchCalculatorServiceImplBuilder builder;

    @Test
     void testSetCalculatorService_ReturnsSelfForChaining() {
        // When
        BatchCalculatorServiceImplBuilder result = builder.setCalculatorService(calculatorServiceMock);
        
        // Then
        assertSame(builder, result, "Should return self for method chaining");
    }

    @Test
     void testCreateBatchCalculatorServiceImpl_InjectsDependency() throws Exception {
        // Given
        builder.setCalculatorService(calculatorServiceMock);
        
        // When
        BatchCalculatorServiceImpl service = builder.createBatchCalculatorServiceImpl();
        
        // Then
        assertNotNull(service, "Created service should not be null");
        
        // Use reflection to verify private field injection
        var calculatorServiceField = BatchCalculatorServiceImpl.class
                .getDeclaredField("calculatorService");
        calculatorServiceField.setAccessible(true);
        
        assertSame(calculatorServiceMock, calculatorServiceField.get(service),
                "Should inject the configured calculator service");
    }

    @Test
     void testCreateBatchCalculatorServiceImpl_ThrowsWhenServiceNotSet() {
         // Given: un builder vierge SANS service configuré
        BatchCalculatorServiceImplBuilder emptyBuilder = new BatchCalculatorServiceImplBuilder();

        // When & Then
        assertThrows(NullPointerException.class, emptyBuilder::createBatchCalculatorServiceImpl, "Should throw NPE when calculator service is not set");
    }
}