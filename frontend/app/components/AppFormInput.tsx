"use client"

import React from "react";
import {Description, Field, Input, Label} from "@headlessui/react";
import clsx from "clsx";


interface AppFormInputProps {
    title: string;
    description: string;
    type: string;
    required?: boolean;
    value: string;
    errors?: string[];
    onChange: (value: string | any) => void;
}

const AppFormInput: React.FC<AppFormInputProps> = ({
                                                       title,
                                                       description,
                                                       type,
                                                       required,
                                                       value,
                                                       errors,
                                                       onChange
                                                   }: AppFormInputProps) => {
    return (
        <Field className="sm:col-span-3">
            <Label className="text-sm-6 font-medium">{title}</Label>
            <Description className="text-sm-6 text-gray-800">{description}</Description>
            <Input name="name" value={value}
                   required={required}
                   type={type}
                   onChange={(e) => onChange(e.target.value)}
                   className={clsx(
                       'block w-full p-1 outline-none',
                       'border-b-2 border-gray-500',
                       'focus:border-blue-500',
                       'bg-gray-200',
                       (errors && errors.length > 0) ? 'border-red-500' : ''
                   )}
            />
            {errors && errors.length > 0 && (
                <div className="text-red-500 text-sm-6">
                    {errors.map((err, index) => (
                        <div key={index}>{err}</div>
                    ))}
                </div>
            )}
        </Field>
    )
}

export default AppFormInput;
