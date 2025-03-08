"use client"

import React from "react";
import clsx from "clsx";


interface Props {
    text: string;
    colourConfig?: Map<string, string>;
}

const AppBadge: React.FC<Props> = ({text, colourConfig}: Props) => {

    const colorClasses = {
        "red": "bg-red-100 text-red-700",
        "green": "bg-green-100 text-green-700",
        "blue": "bg-blue-100 text-blue-700",
        "yellow": "bg-yellow-100 text-yellow-700",
        "purple": "bg-purple-100 text-purple-700",
        "gray": "bg-gray-100 text-gray-700"
    };

    const color = colourConfig?.get(text);
    const colorClass = colorClasses[color as keyof typeof colorClasses] || colorClasses["gray"];

    return (
        <span className={clsx(
            "px-1.5 py-0.5 text-xs font-semibold rounded-full",
            colorClass
        )}>
            {text}
        </span>
    )
}

export default AppBadge;
