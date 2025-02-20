// frontend/app/components/Button.tsx
"use client";
import Link from "next/link";
import React from "react";

interface ButtonProps {
    name: string;
    href: string;
}

const Button: React.FC<ButtonProps> = ({name, href}) => {
    return (
        <Link href={href} className="button">
            {name}
        </Link>
    );
};

export default Button;
