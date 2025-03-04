"use client"

import React from "react";

interface Props {
    currentPage: number;
    pageSize: number;
    totalElements: number;
}

const AppTableSummary: React.FC<Props> = ({currentPage, pageSize, totalElements}: Props) => {
    return (
        <div className="flex-1 w-full min-w-[300px] p-4 ">
            <p className="text-sm text-gray-500">
                Showing
                <span className="font-medium"> {currentPage * pageSize + 1} </span>
                to
                <span
                    className="font-medium"> {Math.min((currentPage + 1) * pageSize, totalElements)} </span>
                of
                <span className="font-medium"> {totalElements} </span> results
            </p>
        </div>
    )
}

export default AppTableSummary;
