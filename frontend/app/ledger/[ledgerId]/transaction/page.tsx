"use client";

import {useParams, useRouter} from "next/navigation";
import React, {useEffect, useState} from "react";
import AppTitle from "@/app/components/AppTitle";
import AppBadge from "@/app/components/AppBadge";
import AppActionMenu from "@/app/components/AppActionMenu";
import AppButton from "@/app/components/AppButton";
import Image from "next/image";
import AppLedgerTransactionForm, {
    LedgerTransactionFormData
} from "@/app/ledger/[ledgerId]/transaction/AppLedgerTransactionForm";
import {UserFormData} from "@/app/users/AppUserForm";

interface LedgerModel {
    id: string;
    name: string;
    description: string;
    status: string;
    type: string;
    balance: number;
}

export default function LedgerTransactionPage() {

    const router = useRouter();
    const {ledgerId} = useParams();
    const [ledgerModel, setLedgerModel] = useState<LedgerModel | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);

    useEffect(() => {
        if (!ledgerId) return;

        fetch(`http://localhost:8080/app/ledger/accounts/${ledgerId}`)
            .then((response) => {
                if (!response.ok) {
                    throw new Error(`Ledger not found (ID: ${ledgerId})`);
                }
                return response.json();
            })
            .then((data) => {
                setLedgerModel(data);
                setLoading(false);
            })
            .catch((error) => {
                setError(error.message);
                setLoading(false);
            });
    }, [ledgerId]);

    const handleSubmit = async (payload: LedgerTransactionFormData) => {
        setLoading(true);
        setError(null);
        setSuccess(null);
        try {
            const response = await fetch(`http://localhost:8080/app/ledger/accounts/${ledgerId}/transactions`, {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify(payload),
            });
            const responseData = await response.json();
            if (response.ok) {
                setSuccess("Ledger updated successfully!");
                setTimeout(() => router.push(`/ledger/${ledgerId}`), 1500);
            } else {
                setError(responseData.message || "Failed to update ledger");
                setLoading(false);
            }
        } catch (error: any) {
            setError(error.message || "An unexpected error occurred");
            setLoading(false);
        }
    }

    return (
        <div>
            <AppTitle
                title="Ledger"
                subTitle="List of all ledger accounts"
                backgroundImage="/dashboard/ledger.png"
            />
            <main className="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8 bg-white mt-6">
                {success && <p className="text-green-500 bg-green-800 p-3 rounded mb-4">{success}</p>}
                {error && <p className="text-red-500">{error}</p>}

                {/* Loading State */}
                {loading && (
                    <p className="text-gray-400">Loading...</p>
                )}

                {!loading && ledgerModel && (
                    <div>
                        <h1 className="text-2xl font-bold text-gray-800">{ledgerModel.name}</h1>
                        <p className="text-gray-600">{ledgerModel.description}</p>
                        <div className="mt-4 grid grid-cols-2 gap-x-4 gap-y-2 items-center w-fit text-left">
                            <span className="text-gray-700">Status:</span>
                            <AppBadge text={ledgerModel.status}
                                      colourConfig={new Map([
                                          ["INACTIVE", "red"],
                                          ["ACTIVE", "green"],
                                      ])}
                            />

                            <span className="text-gray-700">Type:</span>
                            <AppBadge text={ledgerModel.type}
                                      colourConfig={new Map([
                                          ["ASSETS", "blue"],
                                          ["LIABILITIES", "yellow"],
                                          ["EQUITY", "purple"],
                                          ["REVENUE", "green"],
                                          ["EXPENSES", "red"],
                                      ])}
                            />
                            <span className="text-gray-700">Balance:</span>
                            <span className="text-lg font-semibold text-gray-900">
                                {ledgerModel.balance.toLocaleString()}
                            </span>

                        </div>
                        {(!loading) && (
                            <AppLedgerTransactionForm
                                state="NEW"
                                loading={loading}
                                data={null}
                                onSave={(data) => handleSubmit(data)}
                                onCancel={() => router.push(`/ledger/${ledgerId}`)}
                            />
                        )}
                    </div>
                )}
            </main>
        </div>
    );
}
