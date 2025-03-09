"use client";

import AppTitle from "@/app/components/AppTitle";
import React, {useState} from "react";
import {useRouter} from "next/navigation";
import AppLedgerCreateForm, {LedgeCreateFormData} from "@/app/ledger/AppLedgerCreateForm";


export default function LedgerCreatePage() {
    const router = useRouter();
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);
    const [formData, setFormData] = useState<LedgeCreateFormData | null>(null);

    const handleSubmit = async (payload: LedgeCreateFormData) => {
        setFormData(payload);
        setLoading(true);
        setError(null);
        setSuccess(null);
        try {
            const response = await fetch(`http://localhost:8080/app/ledger/accounts`, {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify(payload),
            });
            const responseData = await response.json();
            if (response.ok) {
                setSuccess("Ledger account created successfully!");
                setTimeout(() => router.push("/ledger"), 1500);
            } else {
                setError(responseData.message || "Failed to create ledger account");
                setLoading(false);
            }
        } catch (err: any) {
            setError(err.message);
            setLoading(false);
        }
    }

    return (
        <div>
            <AppTitle
                title="Ledger"
                subTitle="Create new ledger account"
                backgroundImage="/dashboard/ledger.png"
            />
            <main className="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8 bg-white mt-6">
                <AppLedgerCreateForm state="NEW"
                                     data={null}
                                     loading={loading}
                                     onSave={handleSubmit}
                                     onCancel={() => router.push("/ledger")}
                />
            </main>
        </div>
    )
}
