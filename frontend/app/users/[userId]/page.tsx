"use client";
import {useParams, useRouter} from "next/navigation";
import AppButton from "@/app/components/AppButton";
import AppActionMenu from "@/app/components/AppActionMenu";
import AppTitle from "@/app/components/AppTitle";
import {useEffect, useState} from "react";
import AppDataView from "@/app/components/AppDataView";
import AppDataViewItem from "@/app/components/AppDataViewItem";
import AppBadge from "@/app/components/AppBadge";
import {badgerStatusConfig} from "@/app/data/BadgerConfig";

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
                backgroundImage="/dashboard/users.png"
            />
            <main className="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8 bg-white mt-6">
                {loading && <p className="text-gray-400">Loading user data...</p>}
                {error && <p className="text-red-500">{error}</p>}
                {user && (
                    <AppDataView>
                        <AppDataViewItem title="User ID">
                            {user.id}
                        </AppDataViewItem>
                        <AppDataViewItem title="Name">
                            {user.name}
                        </AppDataViewItem>
                        <AppDataViewItem title="Email">
                            {user.email}
                        </AppDataViewItem>
                        <AppDataViewItem title="Status">
                            <AppBadge text={user.status}
                                      colourConfig={badgerStatusConfig}
                            />
                        </AppDataViewItem>
                    </AppDataView>
                )}
                <AppActionMenu>
                    <AppButton variant="secondary" onClick={() => router.push("/users")}>
                        Cancel
                    </AppButton>
                    <AppButton variant="primary" onClick={() => router.push(`/users/${userId}/edit`)}>
                        Edit
                    </AppButton>
                </AppActionMenu>
            </main>
        </div>
    );
}
