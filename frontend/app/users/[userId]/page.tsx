"use client";
import {useParams, useRouter} from "next/navigation";
import AppButton from "@/app/components/AppButton";
import AppActionMenu from "@/app/components/AppActionMenu";
import AppTitle from "@/app/components/AppTitle";
import {useEffect, useState} from "react";

interface User {
    id: string;
    name: string;
    email: string;
    status: string;
}

export default function UserViewPage() {
    const router = useRouter();
    const {userId} = useParams();
    const [user, setUser] = useState<User | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (!userId) return;

        fetch(`http://localhost:8080/app/users/${userId}`)
            .then((response) => {
                if (!response.ok) {
                    throw new Error(`User not found (ID: ${userId})`);
                }
                return response.json();
            })
            .then((data) => {
                setUser(data);
                setLoading(false);
            })
            .catch((error) => {
                setError(error.message);
                setLoading(false);
            });
    }, [userId]);

    return (
        <div>
            <AppTitle
                title="Users Management"
                subTitle="User details"
            />
            <main className="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8 bg-white mt-6">
                {loading && <p className="text-gray-400">Loading user data...</p>}
                {error && <p className="text-red-500">{error}</p>}

                {user && (
                    <div className="grid grid-cols-1 sm:grid-cols-2 gap-x-8 gap-y-4 ">
                        <div className="flex flex-col">
                            <span className="text-gray-800 text-sm">User ID</span>
                            <span className="font-medium">{user.id}</span>
                        </div>

                        <div className="flex flex-col">
                            <span className="text-gray-800 text-sm">Name</span>
                            <span className="font-medium">{user.name}</span>
                        </div>

                        <div className="flex flex-col">
                            <span className="text-gray-800 text-sm">Email</span>
                            <span className="font-medium">{user.email}</span>
                        </div>

                        <div className="flex flex-col">
                            <span className="text-gray-800 text-sm">Status</span>
                            <span className="font-medium">{user.status}</span>
                        </div>
                    </div>
                )}
                <AppActionMenu>
                    <AppButton
                        variant="secondary"
                        onClick={() => router.push("/users")}
                    >
                        Cancel
                    </AppButton>
                    <AppButton
                        variant="primary"
                        onClick={() => router.push(`/users/${userId}/edit`)}
                    >
                        Edit
                    </AppButton>
                </AppActionMenu>
            </main>
        </div>
    );
}
