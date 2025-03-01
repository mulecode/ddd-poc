"use client";
import {useState} from "react";
import {useRouter} from "next/navigation";
import AppTitle from "@/app/components/AppTitle";
import AppUserForm, {UserFormData} from "@/app/users/AppUserForm";


export default function UserCreatePage() {
    const router = useRouter();
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);

    const handleSubmit = async (payload: UserFormData) => {
        setLoading(true);
        setError(null);
        setSuccess(null);
        try {
            const response = await fetch("http://localhost:8080/app/users", {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify(payload),
            });
            const responseData = await response.json();
            if (response.ok) {
                setSuccess("User created successfully!");
                setTimeout(() => router.push("/users"), 1500);
            } else {
                setError(responseData.message || "Failed to create user");
            }
        } catch (err: any) {
            setError(err.message || "An unexpected error occurred");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div>
            <AppTitle title="Users Management" subTitle="Create New User"/>
            <main className="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8 bg-white mt-6">
                {/* Success/Error Messages */}
                {success && <p className="text-green-500 bg-green-800 p-3 rounded mb-4">{success}</p>}
                {error && <p className="text-red-500 bg-red-800 p-3 rounded mb-4">{error}</p>}

                {!loading && (
                    <AppUserForm
                        state="NEW"
                        loading={loading}
                        data={null}
                        onSave={(data) => handleSubmit(data)}
                        onCancel={() => router.push("/users")}
                    />
                )}
            </main>
        </div>
    );
}
