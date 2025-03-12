"use client"
import {useEffect, useState} from "react";
import {useRouter} from "next/navigation";
import AppTitle from "@/app/components/AppTitle";
import AppButton from "@/app/components/AppButton";
import Image from "next/image";
import AppFilterBar from "@/app/components/AppFilterBar";
import {AppSelectItem} from "@/app/components/AppFormSelect";
import AppTableData from "@/app/components/AppTableData";
import AppTableFooter from "@/app/components/AppTableFooter";

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
        fetch(`/backend/ledger/accounts?page=${currentPage}&size=${pageSize}&${queryStrings}`)
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
                subTitle="List of all ledger accounts"
                backgroundImage="/dashboard/ledger.png"
                actionButton={
                    <AppButton
                        variant="primary"
                        onClick={() => router.push("/ledger/new")}
                    >
                        <Image src="/plus.svg" alt="View" width={24} height={24}/>
                        <span>Create New Ledger</span>
                    </AppButton>
                }
            />
            <main className="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8 bg-white mt-6">
                <AppFilterBar filters={filters}
                              defaultFilter="name"
                              onSearch={handleSearch}
                />
                <AppTableData
                    headers={["ID", "Name", "Description", "Type"]}
                    columnWidths={["w-2/8", "w-2/8", "w-2/8", "w-2/8"]}
                    columnCss={["text-xs", "", "", ""]}
                    data={ledger}
                    linkBaseUrl="/ledger"
                />
                <AppTableFooter
                    currentPage={currentPage}
                    totalPages={totalPages}
                    pageSize={pageSize}
                    totalElements={totalElements}
                    onPageChange={(page) => setCurrentPage(page)}
                />
            </main>
        </div>
    );
}

