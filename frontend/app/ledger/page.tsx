"use client"
import {useEffect, useState} from "react";
import {useRouter} from "next/navigation";
import AppTitle from "@/app/components/AppTitle";
import AppButton from "@/app/components/AppButton";
import AppTableNav from "@/app/components/AppTableNav";
import Image from "next/image";
import Link from "next/link";
import AppFilterBar from "@/app/components/AppFilterBar";
import {AppSelectItem} from "@/app/components/AppFormSelect";

interface Ledger {
    id: string;
    name: string;
    description: string;
    type: string;
}

interface LedgerApiResponse {
    ledgerAccounts: Ledger[];
    page: number;
    totalPages: number;
    size: number;
    totalElements: number;
}

export default function LedgerPage() {
    const [ledger, setLedger] = useState<Ledger[]>([]);
    const [currentPage, setCurrentPage] = useState<number>(0);
    const [pageSize, setPageSize] = useState<number>(5);
    const [totalPages, setTotalPages] = useState<number>(1);
    const [totalElements, setTotalElements] = useState<number>(0);
    const [queryStrings, setQueryStrings] = useState<string>("");

    const router = useRouter();

    useEffect(() => {
        console.log(`Fetching ledgers... ${currentPage} ${pageSize} ${queryStrings}`);
        fetch(`http://localhost:8080/app/ledger/accounts?page=${currentPage}&size=${pageSize}&${queryStrings}`)
            .then((response) => response.json())
            .then((data: LedgerApiResponse) => {
                setLedger(data.ledgerAccounts);
                setTotalPages(data.totalPages);
                setTotalElements(data.totalElements);
            })
            .catch((error) => console.error("Error fetching users:", error));
    }, [currentPage, pageSize, queryStrings]);


    const filters = [
        {id: "id", name: "Identification"},
        {id: "name", name: "Name"},
        {id: "type", name: "Type"},
    ];

    const handleSearch = (selectedFilters: AppSelectItem[]) => {
        console.log("Filters submitted:", selectedFilters);
        setCurrentPage(0);
        setQueryStrings(selectedFilters.map(f => `${f.id}=${f.name}`).join("&"));
    };

    return (
        <div>
            <AppTitle
                title="Ledger"
                subTitle="List of all ledge accounts"
                backgroundImage="/dashboard/ledger.png"
                actionButton={
                    <AppButton
                        variant="primary"
                        onClick={() => router.push("/ledger/new")}
                    >
                        <Image src="/plus.svg" alt="View" width={24} height={24}/>
                        <span>Create New User</span>
                    </AppButton>
                }
            />
            <main className="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8 bg-white mt-6">
                <AppFilterBar filters={filters}
                              defaultFilter="name"
                              onSearch={handleSearch}/>
                <table className="min-w-full border border-gray-500 bg-white">
                    <thead className="bg-gray-500 text-white">
                    <tr>
                        <th className="py-2 px-4 border-b border-gray-500 text-left w-2/8">ID</th>
                        <th className="py-2 px-4 border-b border-gray-500 text-left w-2/8">Name</th>
                        <th className="py-2 px-4 border-b border-gray-500 text-left w-2/8">Description</th>
                        <th className="py-2 px-4 border-b border-gray-500 text-left w-2/8">Type</th>
                    </tr>
                    </thead>
                    <tbody>
                    {ledger.map((item) => (
                        <tr key={item.id} className="hover:bg-gray-300">
                            <td className="py-2 px-4 border-b border-gray-500 w-2/8">
                                <Link href={`/ledger/${item.id}`}>
                                    {item.id}
                                </Link>
                            </td>
                            <td className="py-2 px-4 border-b border-gray-500 w-2/8">
                                {item.name}
                            </td>
                            <td className="py-2 px-4 border-b border-gray-500 w-2/8">
                                {item.description}
                            </td>
                            <td className="py-2 px-4 border-b border-gray-500 w-2/8">
                                {item.type}
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
                {/* Tables footer */}
                <div className="flex flex-wrap gap-4 w-full text-white">
                    {/* Pagination Summary */}
                    <div className="flex-1 w-full min-w-[300px] p-4 ">
                        <p className="text-sm text-gray-500">
                            Showing
                            <span className="font-medium"> {currentPage * pageSize + 1} </span>
                            to
                            <span
                                className="font-medium"> {Math.min((currentPage + 1) * pageSize, totalElements)} </span>
                            of
                            <span className="font-medium"> {totalElements} </span> results
                        </p>
                    </div>

                    {/* Pagination Controls in Second Column */}
                    <div className="flex-1 w-full min-w-[300px] p-4 flex justify-end">
                        <AppTableNav
                            currentPage={currentPage}
                            totalPages={totalPages}
                            previousPageAction={() => setCurrentPage((prev) => Math.max(prev - 1, 0))}
                            nextPageAction={() => setCurrentPage((prev) => Math.min(prev + 1, totalPages - 1))}
                            indexPageAction={(index) => setCurrentPage(index)}
                        />
                    </div>
                </div>
            </main>
        </div>
    );
}

