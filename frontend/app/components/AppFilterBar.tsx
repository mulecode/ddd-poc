"use client"

import React, {useState} from "react";
import Image from "next/image";
import AppButton from "@/app/components/AppButton";
import AppFormInput from "@/app/components/AppFormInput";
import AppFormSelect, {AppSelectItem} from "@/app/components/AppFormSelect";
import {Button} from "@headlessui/react";

interface AppFilterBarProps {
    filters: AppSelectItem[];
    defaultFilter?: string;
    onSearch: (selectedFilters: AppSelectItem[]) => void;
}

const AppFilterBar: React.FC<AppFilterBarProps> = ({filters, defaultFilter, onSearch}) => {
    const [selectedFilter, setSelectedFilter] = useState<string>(
        defaultFilter && filters.some(f => f.id === defaultFilter) ? defaultFilter : filters[0]?.id || ""
    );
    const [inputValue, setInputValue] = useState<string>("");
    const [appliedFilters, setAppliedFilters] = useState<AppSelectItem[]>([]);

    const handleAddFilter = () => {
        if (!selectedFilter || !inputValue.trim()) return;

        const newFilter: AppSelectItem = {id: selectedFilter, name: inputValue.trim()};

        setAppliedFilters([...appliedFilters, newFilter]);
        setInputValue("");
    };

    const handleRemoveFilter = (index: number) => {
        setAppliedFilters(appliedFilters.filter((_, i) => i !== index));
    };

    const handleClearFilters = () => {
        setAppliedFilters([]);
    };

    const handleSearch = () => {
        onSearch(appliedFilters);
    };

    return (
        <div className="p-3 border border-gray-300 w-full">
            <div className="flex w-full flex-auto gap-5 mb-3 items-center">

                <AppFormSelect placeholder="Attribute"
                               itemSelected={selectedFilter}
                               items={filters}
                               onChange={(e) => setSelectedFilter(e)}
                />

                <AppFormInput placeholder="Filter"
                              className="flex-grow min-w-0"
                              type="text" required={true} value={inputValue}
                              onChange={(e) => setInputValue(e)}
                              onKeyDown={(e) => e.key === "Enter" && handleAddFilter()}
                />

                <AppButton variant="secondary" type="button" onClick={handleAddFilter}>
                    <Image src="/plus-black.svg" alt="View" width={24} height={24}/>
                </AppButton>

                <AppButton variant="primary" type="button" onClick={handleSearch}>
                    <Image src="/search.svg" alt="View" width={24} height={24}/>
                    <span>Search</span>
                </AppButton>
            </div>

            <div className="flex flex-wrap gap-5 mb-3">
                {appliedFilters.map((filter, index) => (
                    <div
                        key={index}
                        className="bg-gray-500 hover:bg-gray-600 transition text-white text-sm px-3 py-1 rounded-full flex items-center gap-2 cursor-default"
                    >
                        <span>{filter.id}: {filter.name}</span>
                        <Button
                            className="text-white text-xs cursor-pointer hover:text-red-400 transition"
                            onClick={() => handleRemoveFilter(index)}
                        >
                            ✕
                        </Button>
                    </div>
                ))}
            </div>

            {appliedFilters.length >= 2 && (
                <div className="text-center mt-2">
                    <button
                        onClick={handleClearFilters}
                        className="text-xs text-gray-500 hover:text-red-600 transition cursor-pointer"
                    >
                        Clear all filters
                    </button>
                </div>
            )}
        </div>
    );
};

export default AppFilterBar;
