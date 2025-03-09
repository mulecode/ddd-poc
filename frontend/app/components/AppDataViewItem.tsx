"use client"

import React from "react";

interface Props {
    title: string;
    children: React.ReactNode;
}

const AppDataViewItem: React.FC<Props> = ({title, children}) => {
    return (
        <div className="flex flex-col">
            <span className="text-gray-800 text-sm">{title}</span>
            <span className="font-medium">
                {children}
            </span>
        </div>
    );
}

export default AppDataViewItem;
