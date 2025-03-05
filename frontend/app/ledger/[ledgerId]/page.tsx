"use client";

import {useParams, useRouter} from "next/navigation";
import {useEffect, useState} from "react";
import AppTitle from "@/app/components/AppTitle";
import clsx from "clsx";

interface LedgerModel {
    id: string;
    name: string;
    description: string;
    status: string;
    type: string;
    balance: number;
}

export default function LedgerViewPage() {
    const router = useRouter();
    const {ledgerId} = useParams();
    const [ledgerModel, setLedgerModel] = useState<LedgerModel | null>(null);
    const [ledgerHistorySize, setLedgerHistorySize] = useState<number>(5);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (!ledgerId) return;

        fetch(`http://localhost:8080/app/ledger/accounts/${ledgerId}?historySize=${ledgerHistorySize}`)
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
                        <h1 className="text-2xl font-bold text-gray-800">{ledgerModel.name}</h1>
                        <p className="text-gray-600">{ledgerModel.description}</p>

                        <div className="mt-4 grid grid-cols-2 gap-x-4 gap-y-2 items-center w-fit text-left">
                            <span className="text-gray-700">Status:</span>
                            <span className={clsx(
                                "px-1.5 py-0.5 text-xs font-semibold rounded-full",
                                ledgerModel.status === "INACTIVE" && "bg-red-100 text-red-700",
                                ledgerModel.status === "ACTIVE" && "bg-green-100 text-green-700",
                                !["ACTIVE", "INACTIVE"].includes(ledgerModel.status) && "bg-gray-100 text-gray-700"
                            )}
                            >{ledgerModel.status}</span>

                            <span className="text-gray-700">Type:</span>
                            <span className={clsx(
                                "px-1.5 py-0.5 text-xs font-semibold rounded-full",
                                ledgerModel.type === "ASSETS" && "bg-blue-100 text-blue-700",
                                ledgerModel.type === "LIABILITIES" && "bg-yellow-100 text-yellow-700",
                                ledgerModel.type === "EQUITY" && "bg-purple-100 text-purple-700",
                                ledgerModel.type === "REVENUE" && "bg-green-100 text-green-700",
                                ledgerModel.type === "EXPENSES" && "bg-red-100 text-red-700",
                                !["ASSETS", "LIABILITIES", "EQUITY", "REVENUE", "EXPENSES"].includes(ledgerModel.type) && "bg-gray-100 text-gray-700"
                            )}>{ledgerModel.type}</span>

                            <span className="text-gray-700">Balance:</span>
                            <span className="text-lg font-semibold text-gray-900">
                                £{ledgerModel.balance.toLocaleString()}
                            </span>
                        </div>
                    </div>
                )}
            </main>
        </div>
    )
}
