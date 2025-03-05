
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
        }
     /*   appendNumber(num) {
            if (this.selectedOperator && this.currentInput === '0') {
                // Nouvel opérande après opérateur : remplace le 0 initial
                this.currentInput = num.toString();
            } else if (this.selectedOperator) {
                // Déjà un chiffre saisi : concaténation normale
                this.currentInput += num.toString();
            } else {
                // Mode saisie normale
                this.currentInput === '0'
                    ? this.currentInput = num.toString()
                    : this.currentInput += num.toString();
            }
        }*/,

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

/*
const { createApp } = Vue;

createApp({
    data() {
        return {
            currentInput: '',
            firstOperand: null,
            selectedOperator: null,
            result: null,
            error: null
        }
    },
    methods: {
        sqrt() {
            if (this.currentInput) {
                const num = parseFloat(this.currentInput);
                this.currentInput = Math.sqrt(num).toString();
            }
        },
        square() {
            if (this.currentInput) {
                const num = parseFloat(this.currentInput);
                this.currentInput = (num * num).toString();
            }
        },
        appendNumber(num) {
            if (this.currentInput === '0') {
                this.currentInput = num.toString();
            } else {
                this.currentInput += num.toString();
            }
        },
        appendDecimal() {
            if (!this.currentInput.includes('.')) {
                this.currentInput += '.';
            }
        },
        clear() {
            this.currentInput = '';
            this.firstOperand = null;
            this.selectedOperator = null;
            this.result = null;
            this.error = null;
        },
        setOperator(operator) {
            if (this.currentInput) {
                this.firstOperand = parseFloat(this.currentInput);
                this.selectedOperator = operator;
                this.currentInput = '';
            }
        },
        async calculate() {
            if (!this.selectedOperator || !this.firstOperand) return;

            const calculation = {
                leftArgument: this.firstOperand,
                calculationType: this.selectedOperator,
                rightArgument: parseFloat(this.currentInput)
            };

            try {
                const response = await axios.post('/calculator', calculation);
                this.result = response.data.solution;
                this.error = null;
                // Reset for next calculation
                this.firstOperand = response.data.solution;
                this.currentInput = '';
            } catch (error) {
                this.error = error.response?.data || 'Erreur de calcul';
                this.result = null;
            }
        }
    }
}).mount('#app');*/
