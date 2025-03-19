
const { createApp } = Vue;

createApp({
    data() {
        return {
            currentInput: '0',
            firstOperand: null,
            selectedOperator: null,
            error: null
        }
    },
    methods: {
        // Gestion numérique
        appendNumber(num) {
            const numStr = num.toString();
            if (this.selectedOperator && this.currentInput === '0') {
                this.currentInput = numStr;
            } else {
                this.currentInput = this.currentInput === '0' ? numStr : this.currentInput + numStr;
            }
        },

        appendDecimal() {
            if (!this.currentInput.includes('.')) {
                this.currentInput += this.currentInput ? '.' : '0.';
            }
        },

        // Opérations unaires (x², √)
        handleUnaryOperation(symbol) {
            if (!this.currentInput) return;

            this.sendToBackend({
                leftArgument: parseFloat(this.currentInput),
                calculationType: symbol
            });
        },

        // Opérations binaires (+, -, ×, etc.)
        setBinaryOperator(symbol) {
            if (!this.currentInput || this.currentInput === '0') return;

            this.firstOperand = parseFloat(this.currentInput);
            this.selectedOperator = symbol;
            this.currentInput = '';
        },

        // Calcul final
        async calculate() {
            if (!this.selectedOperator || !this.firstOperand) return;

            const payload = {
                type: this.selectedOperator, // Doit envoyer "MODULO" ou "%" selon le backend
                leftArgument: this.firstOperand,
                rightArgument: parseFloat(this.currentInput)
            };

            await this.sendToBackend(payload);

            this.selectedOperator = null;

        },

        // Communication avec le backend
        async sendToBackend(payload) {
            try {
                const { data } = await axios.post('/calculator', payload);
                this.currentInput = data.solution.toString();
                this.firstOperand = data.solution;
                this.error = null;
            } catch (error) {
                this.error = error.response?.data || 'Erreur de calcul';
                this.currentInput = '0';
            }
        },

        // Réinitialisation
        clear() {
            this.currentInput = '0';
            this.firstOperand = null;
            this.selectedOperator = null;
            this.error = null;
        }
    }
}).mount('#app');
