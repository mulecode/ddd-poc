"use client"
import React from "react";

interface AppButtonProps {
    type?: "button" | "submit" | "reset";
    disabled?: boolean;
    // title: string;
    onClick?: () => void;
    hoverText?: string;
    variant?: "primary" | "secondary" | "danger" | "success";
    children: React.ReactNode;
}

const buttonStyles: Record<string, string> = {
    primary: "bg-blue-900 hover:bg-blue-700 text-white",
    secondary: "text-gray-800 hover:bg-gray-300 hover:text-blue-900",
    danger: "bg-red-700 hover:bg-red-600 text-white",
    success: "bg-green-700 hover:bg-green-600 text-white",
};

const AppButton: React.FC<AppButtonProps> = ({
                                                 type = "button",
                                                 disabled = false,
                                                 onClick,
                                                 hoverText,
                                                 variant = "secondary",
                                                 children
                                             }) => {
    return (
        <button
            type={type}
            disabled={disabled}
            className={`px-4 py-2 rounded flex items-center gap-4 ${buttonStyles[variant]}`}
            onClick={onClick}
            title={hoverText || ""}
        >
            {children}
        </button>
    );
};

export default AppButton;
