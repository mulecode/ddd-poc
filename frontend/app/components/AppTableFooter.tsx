"use client"

import React from "react";
import AppTableSummary from "@/app/components/AppTableSummary";
import AppTableNav from "@/app/components/AppTableNav";

interface Props {
    currentPage: number;
    pageSize: number;
    totalElements: number;
    totalPages: number;
    onPageChange: (page: number) => void;
}

const AppTableFooter: React.FC<Props> = ({
                                             currentPage,
                                             pageSize,
                                             totalElements,
                                             totalPages,
                                             onPageChange
                                         }: Props) => {
    return (
        <div className="flex flex-wrap gap-4 w-full text-white">
            <AppTableSummary
                currentPage={currentPage}
                pageSize={pageSize}
                totalElements={totalElements}
            />
            <div className="flex-1 w-full min-w-[300px] p-4 flex justify-end">
                <AppTableNav
                    currentPage={currentPage}
                    totalPages={totalPages}
                    setCurrentPage={onPageChange}
                />
            </div>
        </div>
    )
}

export default AppTableFooter;
