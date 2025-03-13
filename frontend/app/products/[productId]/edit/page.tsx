"use client"

import AppTitle from "@/app/components/AppTitle";
import AppButton from "@/app/components/AppButton";
import React, {useEffect, useState} from "react";
import {useParams, useRouter} from "next/navigation";
import {Product} from "@/app/products/page";
import AppProductForm, {ProductFormData} from "@/app/products/AppProductForm";

export default function ProductEditPage() {
    const router = useRouter();
    const {productId} = useParams();
    const [formData, setFormData] = useState<Product | null>(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);

    useEffect(() => {
        if (!productId) return;
        setLoading(true);
        fetch(`/backend/products/${productId}?withVariations=false`)
            .then((response) => {
                if (!response.ok) {
                    throw new Error(`Product not found (ID: ${productId})`);
                }
                return response.json();
            })
            .then((data) => {
                setLoading(false);
                setFormData(data);
            })
            .catch((error) => {
                setLoading(false);
                setError(error.message);
            });
    }, [productId]);

    const handleSubmit = async (payload: ProductFormData) => {
        setLoading(true);
        setError(null);
        setSuccess(null);
        try {
            const response = await fetch(`/backend/products/${productId}`, {
                method: "PUT",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify(payload),
            });
            const responseData = await response.json();
            if (response.ok) {
                setSuccess("Product altered successfully!");
                setTimeout(() => router.push("/products"), 1500);
            } else {
                setError(responseData.message || "Failed to modify product");
            }
        } catch (err: any) {
            setError(err.message || "An unexpected error occurred");
        } finally {
            setLoading(false);
        }
    }

    return (
        <div>
            <AppTitle
                title="Product Management"
                subTitle="Modify Product Details"
                backgroundImage="/dashboard/products.png"
            />
            <main className="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8 bg-white mt-6">
                {/* Success/Error Messages */}
                {success && <p className="text-green-500 bg-green-800 p-3 rounded mb-4">{success}</p>}
                {error && <p className="text-red-500 bg-red-800 p-3 rounded mb-4">{error}</p>}

                {/* Loading State */}
                {loading && (
                    <p className="text-gray-400">Loading product data...</p>
                )}

                {(!loading && !formData) && (
                    <div>
                        <AppButton variant="primary" onClick={() => router.push("/products")}>
                            Go Back
                        </AppButton>
                    </div>
                )}

                {(!loading && formData && !success) && (
                    <AppProductForm
                        state="EDIT"
                        loading={loading}
                        data={formData}
                        onSave={(data) => handleSubmit(data)}
                        onCancel={() => router.push("/products")}
                    />
                )}
            </main>
        </div>
    )
}
