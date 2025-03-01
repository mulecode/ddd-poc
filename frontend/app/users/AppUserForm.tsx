"use client"

import React, {useEffect, useState} from "react";
import AppActionMenu from "@/app/components/AppActionMenu";
import AppButton from "@/app/components/AppButton";
import Image from "next/image";
import AppFormSelect from "@/app/components/AppFormSelect";
import AppFormInput from "@/app/components/AppFormInput";
import {AppFieldValidators, AppValidate} from "@/app/data/Validators";

export interface UserFormData {
    id?: string;
    name: string;
    email: string;
    status?: string;
}

interface Props {
    state: "NEW" | "EDIT";
    data: UserFormData | null;
    loading: boolean;
    onSave: (data: UserFormData) => void;
    onCancel: () => void;
}

const userStatuses = [
    {id: "ACTIVE", name: "Active"},
    {id: "INACTIVE", name: "Inactive"},
];

const UserFormDataValidator: AppFieldValidators = {
    name: {
        required: true,
        min: 5,
        max: 50
    },
    email: {
        required: true,
        min: 5,
        max: 50,
        format: "email"
    }
}

const AppUserForm: React.FC<Props> = ({
                                          state,
                                          data,
                                          loading,
                                          onSave,
                                          onCancel
                                      }: Props) => {

    const [formData, setFormData] = useState<UserFormData>(
        data || {
            id: "",
            name: "",
            email: "",
            status: "ACTIVE"
        }
    );
    const [errors, setErrors] = useState<any>({});
    const [touched, setTouched] = useState<boolean>(false);

    useEffect(() => {
        if (touched) {
            const validationErrors = AppValidate(formData, UserFormDataValidator);
            setErrors(validationErrors);
        }
    }, [formData, touched]);

    const onSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        const payload = {...formData};
        if (state === "NEW") {
            const {id, ...createRequest} = payload;
            onSave(createRequest);
        } else {
            const {id, ...updateRequest} = payload;
            onSave(updateRequest);
        }
    }

    return (
        <div>
            <form onSubmit={onSubmit}>
                <div className="mt-6 grid grid-cols-1 gap-x-6 gap-y-6 sm:grid-cols-6 mb-4 pb-4">

                    <AppFormInput title="Name" description="Complete name"
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

                    <AppFormInput title="Email" description="Email address"
                                  type="email" required={true} value={formData.email}
                                  errors={errors.email}
                                  onChange={(value) => {
                                      setTouched(true);
                                      setFormData((prev: any) => prev ? {
                                          ...prev,
                                          email: value
                                      } : prev);
                                  }}
                    />

                    <AppFormSelect title="Status" description="User status"
                                   itemSelected={formData.status || 'ACTIVE'} items={userStatuses}
                                   onChange={(value) => {
                                       setTouched(true);
                                       setFormData(prev => prev ? {
                                           ...prev,
                                           status: value
                                       } : prev)
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

export default AppUserForm;
