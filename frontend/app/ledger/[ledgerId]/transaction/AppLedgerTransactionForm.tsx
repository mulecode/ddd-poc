"use client"

import {AppFieldValidators, AppValidate} from "@/app/data/Validators";
import React, {useEffect, useState} from "react";
import AppFormSelect from "@/app/components/AppFormSelect";
import AppFormInput from "@/app/components/AppFormInput";
import AppActionMenu from "@/app/components/AppActionMenu";
import AppButton from "@/app/components/AppButton";
import Image from "next/image";
import AppDataView from "@/app/components/AppDataView";

export interface LedgerTransactionFormData {
    referenceId: string;
    transactionType: ("DEBIT" | "CREDIT") | string;
    amount: number;
}

interface Props {
    state: "NEW" | "EDIT";
    data: LedgerTransactionFormData | null;
    loading: boolean;
    onSave: (data: LedgerTransactionFormData) => void;
    onCancel: () => void;
}

const LedgerTransactionFormDataValidator: AppFieldValidators = {
    referenceId: {
        required: true,
        min: 5,
        max: 50,
        format: "alphanumeric"
    },
    transactionType: {
        required: true
    },
    amount: {
        required: true,
        format: "positive-numbers"
    }
}

const AppLedgerTransactionForm: React.FC<Props> = ({
                                                       state,
                                                       data,
                                                       loading,
                                                       onSave,
                                                       onCancel
                                                   }: Props) => {

    const [formData, setFormData] = useState<LedgerTransactionFormData>(
        data || {
            referenceId: "",
            transactionType: "DEBIT",
            amount: 0,
        }
    );
    const [errors, setErrors] = useState<any>({});
    const [touched, setTouched] = useState<boolean>(false);

    useEffect(() => {
        if (touched) {
            const validationErrors = AppValidate(formData, LedgerTransactionFormDataValidator);
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

                    <AppFormInput title="Reference ID" description="Unique reference ID value"
                                  type="text" required={true} value={formData.referenceId}
                                  errors={errors.referenceId}
                                  onChange={(value) => handleInputChange("referenceId", value)}
                    />

                    <AppFormSelect title="Transaction Type" description="Either Debit or Credit"
                                   itemSelected={formData.transactionType}
                                   items={[
                                       {id: "DEBIT", name: "Debit"},
                                       {id: "CREDIT", name: "Credit"},
                                   ]}
                                   onChange={(value) => handleInputChange("transactionType", value)}
                    />

                    <AppFormInput title="Amount" description="Value of the transaction"
                                  type="text" required={true} value={formData.amount}
                                  errors={errors.amount}
                                  onChange={(value) => handleInputChange("amount", value)}
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
