"use client";

import {useParams, useRouter} from "next/navigation";
import React, {useEffect, useState} from "react";
import AppTitle from "@/app/components/AppTitle";
import AppBadge from "@/app/components/AppBadge";
import AppButton from "@/app/components/AppButton";
import AppActionMenu from "@/app/components/AppActionMenu";

interface LedgerHistoryModel {
    id: string;
    date: string;
    amount: number;
    referenceId: string;
    transactionType: string;
    balanceAfter: number;
    signature: string;
    previousSignature: string;
    verificationStatus: string;
}

interface LedgerModel {
    id: string;
    name: string;
    description: string;
    status: string;
    type: string;
    balance: number;
    history: LedgerHistoryModel[];
}

export default function LedgerViewPage() {
    const router = useRouter();
    const {ledgerId} = useParams();
    const [ledgerModel, setLedgerModel] = useState<LedgerModel | null>(null);
    const [ledgerHistorySize, setLedgerHistorySize] = useState<number>(10);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (!ledgerId) return;

        fetch(`/backend/ledger/accounts/${ledgerId}?historySize=${ledgerHistorySize}`)
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
    }, [ledgerId, ledgerHistorySize]);

    return (
        <div>
            <AppTitle
                title="Ledger"
                subTitle="List of all ledger accounts"
                backgroundImage="/dashboard/ledger.png"
            />
            <main className="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8 bg-white mt-6">
                {loading && <p className="text-gray-400">Loading ledger data...</p>}
                {error && <p className="text-red-500">{error}</p>}

                {ledgerModel && (
                    <div>
                        <div className="flex justify-between items-center">
                            <div>
                                <h1 className="text-2xl font-bold text-gray-800">{ledgerModel.name}</h1>
                                <p className="text-gray-600">{ledgerModel.description}</p>
                            </div>
                            <div>
                                <AppButton variant="primary"
                                           onClick={() => router.push(`/ledger/${ledgerId}/transaction`)}>
                                    Add Transaction
                                </AppButton>
                            </div>
                        </div>
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

                        <h2 className="text-1xl font-bold text-gray-800 pt-5">History</h2>
                        <h3 className="text-xs text-gray-400 pb-3">Last {ledgerHistorySize} records</h3>

                        <div className="overflow-x-auto">
                            <table className="min-w-full bg-white">
                                <thead className="text-gray-600 border-b-2 border-gray-400">
                                <tr>
                                    <th className="py-2 px-4 text-left text-gray-800 text-sm font-semibold">
                                        Date
                                    </th>
                                    <th className="py-2 px-4 text-left text-gray-800 text-sm font-semibold">
                                        Transaction Type
                                    </th>
                                    <th className="py-2 px-4 text-left text-gray-800 text-sm font-semibold">
                                        Reference ID
                                    </th>
                                    <th className="py-2 px-4 text-left text-gray-800 text-sm font-semibold">
                                        Amount
                                    </th>
                                    <th className="py-2 px-4 text-left text-gray-800 text-sm font-semibold">
                                        Balance After
                                    </th>
                                </tr>
                                </thead>
                                <tbody>
                                {ledgerModel.history.map((history) => (
                                    <tr key={history.id} className="hover:bg-gray-200 border-b-1 border-gray-300">
                                        <td className="py-2 px-4 text-gray-600 text-sm">
                                            {new Intl.DateTimeFormat('en-GB', {
                                                year: 'numeric',
                                                month: 'long',
                                                day: 'numeric',
                                                hour: 'numeric',
                                                minute: 'numeric',
                                            }).format(new Date(history.date))}
                                        </td>

                                        <td className="py-2 px-4 text-gray-600 text-sm">
                                            <AppBadge text={history.transactionType}
                                                      colourConfig={new Map([
                                                          ["CREDIT", "blue"],
                                                          ["DEBIT", "green"],
                                                      ])}
                                            />
                                        </td>

                                        <td className="py-2 px-4 text-gray-600 text-sm">
                                            {history.referenceId}
                                        </td>

                                        <td className="py-2 px-4 text-gray-600 text-sm font-bold">
                                            {history.amount.toLocaleString()}
                                        </td>

                                        <td className="py-2 px-4 text-gray-600 text-sm">
                                            {history.balanceAfter.toLocaleString()}
                                        </td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>

                            <AppActionMenu alignDirection="left">
                                <AppButton variant="primary" onClick={() => router.push("/ledger")}>
                                    Back
                                </AppButton>
                            </AppActionMenu>

                        </div>
                    </div>
                )}
            </main>

        </div>
    )
}
