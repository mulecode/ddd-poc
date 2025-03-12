"use client"

import AppButton from "@/app/components/AppButton";
import Image from "next/image";
import AppTitle from "@/app/components/AppTitle";
import {useRouter} from "next/navigation";
import {useEffect, useState} from "react";
import AppTableData from "@/app/components/AppTableData";
import AppFilterBar from "@/app/components/AppFilterBar";
import {AppSelectItem} from "@/app/components/AppFormSelect";
import AppTableFooter from "@/app/components/AppTableFooter";


export interface Product {
    id: string;
    code: string;
    manufacturer: string;
    supplier: string;
    brand: string;
    name: string;
    description: string;
    category: string;
    subCategory: string;
    originCountryCode: string;
    status: string;
    variations?: ProductVariation[];
}

export interface ProductVariation {
    id: string;
    upcCode: string;
    name: string;
    description: string;
    specifications?: ProductSpecification[];
    status: string;
}

export interface ProductSpecification {
    specName: string;
    specValue: string;
    specUnit: string;
}

interface ProductsResponse {
    products: Product[];
    page: number;
    totalPages: number;
    size: number;
    totalElements: number;
}

export default function ProductsPage() {
    const router = useRouter();

    const [products, setProducts] = useState<Product[]>([]);
    const [currentPage, setCurrentPage] = useState<number>(0);
    const [pageSize, setPageSize] = useState<number>(5);
    const [totalPages, setTotalPages] = useState<number>(1);
    const [totalElements, setTotalElements] = useState<number>(0);
    const [queryStrings, setQueryStrings] = useState<string>("");

    useEffect(() => {
        console.log(`Fetching products... ${currentPage} ${pageSize} ${queryStrings}`);
        fetch(`/backend/products?page=${currentPage}&size=${pageSize}&${queryStrings}`)
            .then((response) => response.json())
            .then((data: ProductsResponse) => {
                setProducts(data.products);
                setTotalPages(data.totalPages);
                setTotalElements(data.totalElements);
            })
            .catch((error) => console.error("Error fetching users:", error));
    }, [currentPage, pageSize, queryStrings]);

    const filters = [
        {id: "id", name: "Identification"},
        {id: "manufacturer", name: "Manufacturer"},
        {id: "supplier", name: "Supplier"},
        {id: "code", name: "Code"},
        {id: "brand", name: "Brand"},
        {id: "name", name: "Name"},
        {id: "description", name: "Description"},
        {id: "category", name: "Category"},
        {id: "subCategory", name: "Sub Category"},
        {id: "originCountryCode", name: "Country of Origin"},
        {id: "status", name: "Status"},
    ];

    const handleSearch = (selectedFilters: AppSelectItem[]) => {
        console.log("Filters submitted:", selectedFilters);
        setCurrentPage(0);
        setQueryStrings(selectedFilters.map(f => `${f.id}=${f.name}`).join("&"));
    };

    return (
        <div>
            <AppTitle
                title="Product Catalog"
                subTitle="List of all products"
                backgroundImage="/dashboard/products.png"
                actionButton={
                    <AppButton
                        variant="primary"
                        onClick={() => router.push("/products/new")}
                    >
                        <Image src="/plus.svg" alt="View" width={24} height={24}/>
                        <span>Add New Product</span>
                    </AppButton>
                }
            />
            <main className="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8 bg-white mt-6">
                <AppFilterBar filters={filters}
                              defaultFilter="name"
                              onSearch={handleSearch}
                />
                <AppTableData
                    headers={["ID", "Code", "Brand", "Name", "Status"]}
                    columnWidths={["w-2/10", "w-2/10", "w-2/10", "w-2/10", "w-2/10"]}
                    columnCss={["text-xs", "text-xs", "", "", ""]}
                    data={products}
                    linkBaseUrl="/products"
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
    )
}
