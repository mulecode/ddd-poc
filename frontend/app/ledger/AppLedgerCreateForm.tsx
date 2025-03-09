"use client"

import {AppFieldValidators, AppValidate} from "@/app/data/Validators";
import React, {useEffect, useState} from "react";
import AppFormSelect from "@/app/components/AppFormSelect";
import AppFormInput from "@/app/components/AppFormInput";
import AppActionMenu from "@/app/components/AppActionMenu";
import AppButton from "@/app/components/AppButton";
import Image from "next/image";
import AppDataView from "@/app/components/AppDataView";

export interface LedgeCreateFormData {
    name: string;
    description: string;
    userId: string;
    type: ("ASSETS" | "LIABILITIES" | "EQUITY" | "REVENUE" | "EXPENSES") | string;
}

interface Props {
    state: "NEW" | "EDIT";
    data: LedgeCreateFormData | null;
    loading: boolean;
    onSave: (data: LedgeCreateFormData) => void;
    onCancel: () => void;
}

const FormDataValidator: AppFieldValidators = {
    name: {
        required: true,
        min: 5,
        max: 50,
        format: "alphanumeric"
    },
    description: {
        required: true,
        min: 5,
        max: 100,
        format: "alphanumeric"
    },
    userId: {
        required: true,
    },
    type: {
        required: true,
    }
}

const AppLedgerTransactionForm: React.FC<Props> = ({
                                                       state,
                                                       data,
                                                       loading,
                                                       onSave,
                                                       onCancel
                                                   }: Props) => {

    const [formData, setFormData] = useState<LedgeCreateFormData>(
        data || {
            name: "",
            description: "",
            userId: "",
            type: "ASSETS",
        }
    );
    const [errors, setErrors] = useState<any>({});
    const [touched, setTouched] = useState<boolean>(false);

    useEffect(() => {
        if (touched) {
            const validationErrors = AppValidate(formData, FormDataValidator);
            setErrors(validationErrors);
        }
    }, [formData, touched]);

    const onSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        onSave(formData);
    }

    const handleInputChange = (field: string, value: any) => {
        setTouched(true);
        setFormData((prev: any) => prev ? {
            ...prev,
            [field]: value
        } : prev);
    };

    return (
        <div>
            <form onSubmit={onSubmit}>
                <AppDataView>

                    <AppFormInput title="Account Name" description="Unique name for the account"
                                  type="text" required={true} value={formData.name}
                                  errors={errors.name}
                                  onChange={(value) => handleInputChange("name", value)}
                    />

                    <AppFormInput title="Description" description="Description of the account"
                                  type="text" required={true} value={formData.description}
                                  errors={errors.description}
                                  onChange={(value) => handleInputChange("description", value)}
                    />

                    <AppFormSelect title="Transaction Type" description="Either Debit or Credit"
                                   itemSelected={formData.type}
                                   items={[
                                       {id: "ASSETS", name: "Assets"},
                                       {id: "LIABILITIES", name: "Liabilities"},
                                       {id: "EQUITY", name: "Equity"},
                                       {id: "REVENUE", name: "Revenue"},
                                       {id: "EXPENSES", name: "Expense"},
                                   ]}
                                   onChange={(value) => handleInputChange("type", value)}
                    />

                    <AppFormInput title="User ID" description="Association with the account"
                                  type="text" required={true} value={formData.userId}
                                  errors={errors.userId}
                                  onChange={(value) => handleInputChange("userId", value)}
                    />
                </AppDataView>
                <AppActionMenu>
                    <AppButton variant="secondary" onClick={onCancel}>
                        Cancel
                    </AppButton>
                    <AppButton variant="primary" type="submit" disabled={loading}>
                        <Image src="/save.svg" alt="View" width={24} height={24}/>
                        {loading ? "Creating..." : "Save"}
                    </AppButton>
                </AppActionMenu>
            </form>
        </div>
    )
}

export default AppLedgerTransactionForm;
