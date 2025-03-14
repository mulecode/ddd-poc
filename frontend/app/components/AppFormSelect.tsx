"use client"

import React, {useMemo, useState} from "react";
import {
    Combobox,
    ComboboxButton,
    ComboboxInput,
    ComboboxOption,
    ComboboxOptions, Description,
    Field,
    Label
} from "@headlessui/react";
import clsx from "clsx";
import {ChevronDownIcon} from "@heroicons/react/24/solid";
import {CheckIcon} from "@heroicons/react/16/solid";
import {placeholder} from "@babel/types";

interface AppSelectProps {
    title?: string;
    description?: string;
    placeholder?: string;
    itemSelected: string;
    items: AppSelectItem[];
    className?: string;
    errors?: string[];
    onChange?: (value: string) => void;
}

export interface AppSelectItem {
    id: string;
    name: string;
}

const AppFormSelect: React.FC<AppSelectProps> = ({
                                                     title,
                                                     description,
                                                     placeholder,
                                                     itemSelected,
                                                     items,
                                                     className,
                                                     errors,
                                                     onChange
                                                 }) => {
    const [query, setQuery] = useState('');
    const [selected, setSelected] = useState<AppSelectItem>(
        items.find(item => item.id === itemSelected) || {id: '', name: ''}
    );

    const filteredOptions = useMemo(() => {
        const lowerQuery = query.toLowerCase();
        return items.filter(item => !query || item.name.toLowerCase().includes(lowerQuery));
    }, [query, items]);

    const handleChange = (value: { id: string; name: string }) => {
        setSelected(value);
        onChange?.(value.id);
    };

    return (
        <Field className={clsx(
            className
        )}>
            <Label className="text-sm/6 font-medium">{title}</Label>
            <Description className="text-xs text-gray-500">{description}</Description>
            <Combobox value={selected}
                      name={placeholder}
                      onChange={handleChange}
                      onClose={() => setQuery('')}
            >
                <div className="relative">
                    <ComboboxInput
                        className={clsx(
                            'block w-full p-1 outline-none',
                            'border-b-2 border-gray-500',
                            'focus:border-blue-500',
                            'bg-gray-200',
                            (errors && errors.length > 0) ? 'border-red-500 focus:border-red-500' : ''
                        )}
                        displayValue={(item: AppSelectItem) => item?.name || ''}
                        onChange={(e) => setQuery(e.target.value)}
                    />
                    <ComboboxButton className="group absolute inset-y-0 right-0 px-2.5">
                        <ChevronDownIcon className={clsx(
                            'size-4 fill-gray-500',
                            'group-data-[hover]:fill-blue-500'
                        )}/>
                    </ComboboxButton>
                </div>
                {errors && errors.length > 0 && (
                    <div className="text-red-500 text-xs">
                        {errors.map((err, index) => (
                            <div key={index}>{err}</div>
                        ))}
                    </div>
                )}

                <ComboboxOptions
                    anchor="bottom"
                    transition
                    className={clsx(
                        'absolute border-1 border-gray-300 bg-white',
                        'absolute w-[var(--input-width)] p-1 [--anchor-gap:var(--spacing-1)] empty:invisible',
                    )}
                >
                    {filteredOptions.map((item) => (
                        <ComboboxOption
                            key={item.id}
                            value={item}
                            className={clsx(
                                "group flex items-center gap-2 p-2",
                                "hover:bg-gray-300"
                            )}
                        >
                            <CheckIcon className="invisible size-4 fill-blue-500 group-data-[selected]:visible"/>
                            <div className="text-sm-6">{item.name}</div>
                        </ComboboxOption>
                    ))}
                </ComboboxOptions>
            </Combobox>
        </Field>
    );
};

export default AppFormSelect;
