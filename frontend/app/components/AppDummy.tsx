"use client"

import React from "react";

interface AppDummyData {
    name: string;
}

interface Props {
    data: AppDummyData | null;
}

const AppDummy: React.FC<Props> = ({data}: Props) => {
    return (
        <div></div>
    )
}
