package tech.zerofiltre.testing.calcul.service;

 class BatchCalculatorServiceImplBuilder {
    private CalculatorService calculatorService;

     BatchCalculatorServiceImplBuilder setCalculatorService(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
        return this;
    }

     BatchCalculatorServiceImpl createBatchCalculatorServiceImpl() {
        if(calculatorService == null) throw new NullPointerException("CalculatorService is null");
        return new BatchCalculatorServiceImpl(calculatorService);
    }
}