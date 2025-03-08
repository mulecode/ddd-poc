"use client"
import React from "react";
import clsx from "clsx";

interface AppActionMenuProps {
    alignDirection?: "left" | "right";
    children: React.ReactNode;
}

const AppActionMenu: React.FC<AppActionMenuProps> = ({
                                                         alignDirection,
                                                         children
                                                     }) => {
    const justifyClass = alignDirection === "left" ? "justify-start" : "justify-end";
    return (
        <div className={clsx(
            "flex items-center gap-x-2 mt-2 p-2",
            justifyClass
        )}>
            {children}
        </div>
    );
};

export default AppActionMenu;
