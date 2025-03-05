
    let currentInput = '0';
    let firstOperand = null;
    let selectedOperator = null;

    function updateDisplay() {
    document.getElementById('current-input').textContent = currentInput;
}

    function appendNumber(num) {
    if (currentInput === '0') {
    currentInput = num.toString();
} else {
    currentInput += num.toString();
}
    updateDisplay();
}

    function appendDecimal() {
    if (!currentInput.includes('.')) {
    currentInput += '.';
    updateDisplay();
}
}

    function clearAll() {
    currentInput = '0';
    firstOperand = null;
    selectedOperator = null;
    document.getElementById('result').textContent = '';
    updateDisplay();
}

    // Gestion des opérations
    document.querySelectorAll('[data-operator]').forEach(button => {
    button.addEventListener('click', () => {
        if (selectedOperator !== null) return;

        firstOperand = parseFloat(currentInput);
        selectedOperator = button.dataset.operator;
        document.getElementById('leftArgument').value = firstOperand;
        document.getElementById('calculationType').value = selectedOperator;
        currentInput = '0';
        updateDisplay();
    });
});

    // Soumission du formulaire
    document.querySelector('form').addEventListener('submit', (e) => {
    if (selectedOperator && firstOperand !== null) {
    document.getElementById('rightArgument').value = parseFloat(currentInput);
} else {
    e.preventDefault();
}
});
