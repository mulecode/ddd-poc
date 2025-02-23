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
        } else if (validator.min !== undefined && value.length < validator.min) {
            fieldErrors.push(`Minimum length is ${validator.min}.`);
        } else if (validator.max !== undefined && value.length > validator.max) {
            fieldErrors.push(`Maximum length is ${validator.max}.`);
        } else if (validator.regex && !validator.regex.test(value)) {
            fieldErrors.push("Invalid format.");
        } else if (validator.format === "email" && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) {
            fieldErrors.push("Invalid email format.");
        }

        if (fieldErrors.length > 0) {
            errors[field] = fieldErrors;
        }
    }
    return errors;
}
