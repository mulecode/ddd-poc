"use client";
import AppTitle from "@/app/components/AppTitle";
import {useRouter} from "next/navigation";
import AppBentoItem from "@/app/components/AppBentoItem";

export default function Home() {
    const router = useRouter();
    return (
        <div>
            <AppTitle title="Dashboard" subTitle="Quick Access"/>
            <main className="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8 bg-white mt-6">

                <div className="grid gap-4 grid-cols-2 md:grid-cols-3">
                    <AppBentoItem title="Users" subTitle="Users Management"
                                  routeLink="/users"
                                  imagePath="/dashboard/users.png"/>

                    <AppBentoItem title="Orders" subTitle="Track and process customer orders."
                                  routeLink="/orders"
                                  imagePath="/dashboard/orders.png"/>

                    <AppBentoItem title="Products" subTitle="Manage product listings and inventory."
                                  routeLink="/products"
                                  imagePath="/dashboard/products.png"/>

                    <AppBentoItem title="Inventory" subTitle="Monitor stock levels and adjustments."
                                  routeLink="/orders"
                                  imagePath="/dashboard/inventory.png"/>

                    <AppBentoItem title="Ledger" subTitle="Review financial transactions and records."
                                  routeLink="/ledger"
                                  imagePath="/dashboard/ledger.png"/>

                    <AppBentoItem title="Shipping" subTitle="Manage shipping and delivery services."
                                  routeLink="/shipping"
                                  imagePath="/dashboard/shipping.png"/>

                    <AppBentoItem title="Settings" subTitle="Adjust system settings and preferences."
                                  routeLink="/settings"
                                  imagePath="/dashboard/settings.png"/>

                </div>

            </main>
        </div>

    );
}

