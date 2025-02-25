"use client"
import React from "react";

interface AppActionMenuProps {
    children: React.ReactNode;
}

const AppActionMenu: React.FC<AppActionMenuProps> = ({children}) => {
    return (
        <div className="flex items-center justify-end gap-x-2 mt-2 p-2">
            <hr/>
            {children}
        </div>
    );
};

export default AppActionMenu;
