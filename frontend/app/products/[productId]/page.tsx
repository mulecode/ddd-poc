"use client"

import {useParams, useRouter} from "next/navigation";
import {useEffect, useState} from "react";
import {Product} from "@/app/products/page";
import AppTitle from "@/app/components/AppTitle";
import AppDataView from "@/app/components/AppDataView";
import AppDataViewItem from "@/app/components/AppDataViewItem";
import AppBadge from "@/app/components/AppBadge";
import {badgerStatusConfig} from "@/app/data/BadgerConfig";
import AppActionMenu from "@/app/components/AppActionMenu";
import AppButton from "@/app/components/AppButton";

export default function ProductViewPage() {

    const router = useRouter();
    const {productId} = useParams();
    const [data, setData] = useState<Product | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (!productId) return;

        fetch(`http://localhost:8080/app/products/${productId}`)
            .then((response) => {
                if (!response.ok) {
                    throw new Error(`Product not found (ID: ${productId})`);
                }
                return response.json();
            })
            .then((data) => {
                setData(data);
                setLoading(false);
            })
            .catch((error) => {
                setError(error.message);
                setLoading(false);
            });
    }, [productId]);

    return (
        <div>
            <AppTitle
                title="Product Management"
                subTitle="Product Details"
                backgroundImage="/dashboard/products.png"
            />
            <main className="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8 bg-white mt-6">
                {loading && <p className="text-gray-400">Loading user data...</p>}
                {error && <p className="text-red-500">{error}</p>}
                {data && (
                    <AppDataView>
                        <AppDataViewItem title="Product ID">
                            {data.id}
                        </AppDataViewItem>
                        <AppDataViewItem title="Product Code">
                            {data.code}
                        </AppDataViewItem>
                        <AppDataViewItem title="Manufacturer">
                            {data.manufacturer}
                        </AppDataViewItem>
                        <AppDataViewItem title="Supplier">
                            {data.supplier}
                        </AppDataViewItem>
                        <AppDataViewItem title="Brand">
                            {data.brand}
                        </AppDataViewItem>
                        <AppDataViewItem title="Name">
                            {data.name}
                        </AppDataViewItem>
                        <AppDataViewItem title="Description">
                            {data.description}
                        </AppDataViewItem>
                        <AppDataViewItem title="Category">
                            {data.category}
                        </AppDataViewItem>
                        <AppDataViewItem title="Sub Category">
                            {data.subCategory}
                        </AppDataViewItem>
                        <AppDataViewItem title="Made In">
                            {data.originCountryCode}
                        </AppDataViewItem>
                        <AppDataViewItem title="Status">
                            <AppBadge text={data.status}
                                      colourConfig={badgerStatusConfig}
                            />
                        </AppDataViewItem>
                    </AppDataView>
                )}
                <AppActionMenu>
                    <AppButton variant="secondary" onClick={() => router.push("/products")}>
                        Cancel
                    </AppButton>
                    <AppButton variant="primary" onClick={() => router.push(`/products/${productId}/edit`)}>
                        Edit
                    </AppButton>
                </AppActionMenu>
            </main>
        </div>
    )
}

