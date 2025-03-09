"use client"

import React from "react";

interface Props {
    children: React.ReactNode;
}

const AppDataView: React.FC<Props> = ({children}) => {
    return (
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-x-8 gap-y-4">
            {children}
        </div>
    );
}

export default AppDataView;
