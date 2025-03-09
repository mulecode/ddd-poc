export interface AppValidator {
    required?: boolean;
    min?: number;
    max?: number;
    format?: string;
    regex?: RegExp;
}

export interface AppFieldValidators {
    [field: string]: AppValidator;
}

export interface AppFieldValidatorsError {
    [field: string]: string[];
}

export const AppValidate = (
    data: any,
    validators: AppFieldValidators): AppFieldValidatorsError => {
    const errors: AppFieldValidatorsError = {};
    for (const field in validators) {
        const value = data[field];
        const validator = validators[field];
        const fieldErrors: string[] = [];

        if (validator.required && !value) {
            fieldErrors.push("This field is required.");
        }
        if (validator.min !== undefined && typeof value === 'string' && value.length < validator.min) {
            fieldErrors.push(`Minimum length is ${validator.min}.`);
        }
        if (validator.max !== undefined && typeof value === 'string' && value.length > validator.max) {
            fieldErrors.push(`Maximum length is ${validator.max}.`);
        }
        if (validator.regex && typeof value === 'string' && !validator.regex.test(value)) {
            fieldErrors.push("Invalid format.");
        }
        if (validator.format === "email" && typeof value === 'string' && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) {
            fieldErrors.push("Invalid email format.");
        }
        if (validator.format === "text" && typeof value === 'string' && !/^[a-zA-Z ]*$/.test(value)) {
            fieldErrors.push("Only text characters are allowed.");
        }
        if (validator.format === "alphanumeric" && typeof value === 'string' && !/^[a-zA-Z0-9 ]*$/.test(value)) {
            fieldErrors.push("Only text and numbers characters are allowed.");
        }
        if (validator.format === "positive-numbers") {
            if (!/^\d+(\.\d{1,2})?$/.test(value)) {
                fieldErrors.push("Value must be a valid number.");
            } else {
                const numberValue = parseFloat(value);
                if (numberValue <= 0) {
                    fieldErrors.push("Only positive numbers are allowed.");
                }
            }
        }

        if (fieldErrors.length > 0) {
            errors[field] = fieldErrors;
        }
    }
    return errors;
}
