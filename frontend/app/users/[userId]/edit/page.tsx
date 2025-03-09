"use client";
import React, {useEffect, useState} from "react";
import {useParams, useRouter} from "next/navigation";
import AppTitle from "@/app/components/AppTitle";
import AppButton from "@/app/components/AppButton";
import AppUserForm, {UserFormData} from "@/app/users/AppUserForm";

interface UserEditRequest {
    id: string;
    name: string;
    email: string;
    status: string;
}


export default function UserEditPage() {
    const router = useRouter();
    const {userId} = useParams();
    const [formData, setFormData] = useState<UserFormData | null>(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);

    // Fetch User Data
    useEffect(() => {
        console.log("User ID:", userId);
        if (!userId) return;
        setLoading(true);
        fetch(`http://localhost:8080/app/users/${userId}`)
            .then((response) => {
                if (!response.ok) {
                    throw new Error("User not found");
                }
                return response.json();
            })
            .then((data) => {
                setFormData(data);
                setLoading(false);
            })
            .catch((err) => {
                setError(err.message);
                setLoading(false);
            });
    }, [userId]);

    const handleSubmit = async (payload: UserFormData) => {
        setFormData(payload);
        setLoading(true);
        setError(null);
        setSuccess(null);
        try {
            const response = await fetch(`http://localhost:8080/app/users/${userId}`, {
                method: "PUT",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify(payload),
            });
            const responseData = await response.json();
            if (response.ok) {
                setSuccess("User updated successfully!");
                setTimeout(() => router.push("/users"), 1500);
            } else {
                setError(responseData.message || "Failed to update user");
            }
        } catch (err: any) {
            setError(err.message || "An unexpected error occurred");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div>
            <AppTitle
                title="Users Management"
                subTitle="Modify User Details"
                backgroundImage="/dashboard/users.png"
            />
            <main className="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8 bg-white mt-6">
                {/* Success/Error Messages */}
                {success && <p className="text-green-500 bg-green-800 p-3 rounded mb-4">{success}</p>}
                {error && <p className="text-red-500 bg-red-800 p-3 rounded mb-4">{error}</p>}

                {/* Loading State */}
                {loading && (
                    <p className="text-gray-400">Loading user data...</p>
                )}

                {(!loading && !formData) && (
                    <div>
                        <AppButton variant="primary" onClick={() => router.push("/users")}>
                            Go Back
                        </AppButton>
                    </div>
                )}

                {(!loading && formData && !success) && (
                    <AppUserForm
                        state="EDIT"
                        loading={loading}
                        data={formData}
                        onSave={(data) => handleSubmit(data)}
                        onCancel={() => router.push("/users")}
                    />
                )}
            </main>
        </div>
    );
}
