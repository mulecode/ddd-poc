"use client"

import {AppFieldValidators, AppValidate} from "@/app/data/Validators";
import React, {useEffect, useState} from "react";
import AppUserForm, {UserFormData} from "@/app/users/AppUserForm";
import AppFormSelect from "@/app/components/AppFormSelect";
import AppFormInput from "@/app/components/AppFormInput";
import AppActionMenu from "@/app/components/AppActionMenu";
import AppButton from "@/app/components/AppButton";
import Image from "next/image";

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

    return (
        <div>
            <form onSubmit={onSubmit}>
                <div className="mt-6 grid grid-cols-1 gap-x-6 gap-y-6 sm:grid-cols-6 mb-4 pb-4">

                    <AppFormInput title="Reference ID" description="Unique reference ID value"
                                  type="text" required={true} value={formData.referenceId}
                                  errors={errors.referenceId}
                                  onChange={(value) => {
                                      setTouched(true);
                                      setFormData((prev: any) => prev ? {
                                          ...prev,
                                          referenceId: value
                                      } : prev);
                                  }}
                    />

                    <AppFormSelect title="Transaction Type" description="Either Debit or Credit"
                                   itemSelected={formData.transactionType}
                                   items={[
                                       {id: "DEBIT", name: "Debit"},
                                       {id: "CREDIT", name: "Credit"},
                                   ]}
                                   onChange={(value) => {
                                       setTouched(true);
                                       setFormData(prev => prev ? {
                                           ...prev,
                                           transactionType: value
                                       } : prev)
                                   }}
                    />

                    <AppFormInput title="Amount" description="Value of the transaction"
                                  type="text" required={true} value={formData.amount}
                                  errors={errors.amount}
                                  onChange={(value) => {
                                      setTouched(true);
                                      setFormData((prev: any) => prev ? {
                                          ...prev,
                                          amount: value
                                      } : prev);
                                  }}
                    />
                </div>
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
