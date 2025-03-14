"use client"


import AppTitle from "@/app/components/AppTitle";
import {useRouter} from "next/navigation";
import {useState} from "react";
import AppProductForm, {ProductFormData} from "@/app/products/AppProductForm";

export default function ProductCreatePage() {
    const router = useRouter();
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);

    const handleSubmit = async (payload: ProductFormData) => {
        setLoading(true);
        setError(null);
        setSuccess(null);
        try {
            const response = await fetch("/backend/products", {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify(payload),
            });
            const responseData = await response.json();
            if (response.ok) {
                setSuccess("Product created successfully!");
                setTimeout(() => router.push("/products"), 1500);
            } else {
                setError(responseData.message || "Failed to create product");
            }
        } catch (err: any) {
            setError(err.message || "An unexpected error occurred");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div>
            <AppTitle title="Product Management"
                      subTitle="Create New product"
                      backgroundImage="/dashboard/products.png"
            />
            <main className="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8 bg-white mt-6">
                {/* Success/Error Messages */}
                {success && <p className="text-green-500 bg-green-800 p-3 rounded mb-4">{success}</p>}
                {error && <p className="text-red-500 bg-red-800 p-3 rounded mb-4">{error}</p>}

                {!loading && !success && (
                    <AppProductForm
                        state="NEW"
                        loading={loading}
                        data={null}
                        onSave={(data) => handleSubmit(data)}
                        onCancel={() => router.push("/products")}
                    />
                )}
            </main>
        </div>
    )
}
