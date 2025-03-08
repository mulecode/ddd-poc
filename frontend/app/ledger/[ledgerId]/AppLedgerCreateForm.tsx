"use client"

import {AppFieldValidators, AppValidate} from "@/app/data/Validators";
import React, {useEffect, useState} from "react";
import AppUserForm, {UserFormData} from "@/app/users/AppUserForm";
import AppFormSelect from "@/app/components/AppFormSelect";
import AppFormInput from "@/app/components/AppFormInput";
import AppActionMenu from "@/app/components/AppActionMenu";
import AppButton from "@/app/components/AppButton";
import Image from "next/image";

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

    return (
        <div>
            <form onSubmit={onSubmit}>
                <div className="mt-6 grid grid-cols-1 gap-x-6 gap-y-6 sm:grid-cols-6 mb-4 pb-4">

                    <AppFormInput title="Account Name" description="Unique name for the account"
                                  type="text" required={true} value={formData.name}
                                  errors={errors.name}
                                  onChange={(value) => {
                                      setTouched(true);
                                      setFormData((prev: any) => prev ? {
                                          ...prev,
                                          name: value
                                      } : prev);
                                  }}
                    />

                    <AppFormInput title="Description" description="Description of the account"
                                  type="text" required={true} value={formData.description}
                                  errors={errors.description}
                                  onChange={(value) => {
                                      setTouched(true);
                                      setFormData((prev: any) => prev ? {
                                          ...prev,
                                          description: value
                                      } : prev);
                                  }}
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
                                   onChange={(value) => {
                                       setTouched(true);
                                       setFormData(prev => prev ? {
                                           ...prev,
                                           type: value
                                       } : prev)
                                   }}
                    />

                    <AppFormInput title="User ID" description="Association with the account"
                                  type="text" required={true} value={formData.userId}
                                  errors={errors.userId}
                                  onChange={(value) => {
                                      setTouched(true);
                                      setFormData((prev: any) => prev ? {
                                          ...prev,
                                          userId: value
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
