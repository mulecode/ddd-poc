"use client"

import Image from "next/image";
import Link from "next/link";
import clsx from "clsx";

interface Props {
    title: string;
    subTitle: string;
    routeLink: string;
    imagePath: string;
}

const AppBentoItem: React.FC<Props> = ({title, subTitle, routeLink, imagePath}) => {
    return (
        <div className="relative h-40 bg-white rounded-xl shadow-md text-center overflow-hidden group">
            <div className="overflow-hidden">
                <Image
                    src={imagePath}
                    alt={title}
                    width={400}
                    height={150}
                    className={clsx(
                        "object-cover",
                        "transition-transform duration-300",
                        "group-hover:scale-130 group-hover:-translate-y-6"
                    )}
                />
            </div>
            <Link
                href={routeLink}
                className={clsx(
                    "absolute inset-0 z-10 flex flex-col justify-center items-center text-white",
                    "bg-gradient-to-t from-black/50 to-transparent hover:from-black/70 transition duration-300"
                )}
            >
                <h2 className={clsx(
                    "text-2xl font-bold drop-shadow-lg",
                    "transition-transform duration-300",
                    "group-hover:scale-110",
                    "group-hover:drop-shadow-2xl"
                )}>
                    {title}
                </h2>
                <p className={clsx(
                    "text-sm mt-2 drop-shadow-lg",
                    "transition-transform duration-300 group-hover:scale-110",
                    "group-hover:drop-shadow-2xl"
                )}>
                    {subTitle}
                </p>
            </Link>
        </div>
    );
};

export default AppBentoItem;
