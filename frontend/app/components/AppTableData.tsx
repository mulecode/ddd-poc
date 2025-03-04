"use client"

import React from "react";
import clsx from "clsx";
import Link from "next/link";


interface Props {
    headers: string[];
    columnWidths: string[];
    columnCss: string[];
    data: { [key: string]: any }[];
    linkBaseUrl?: string;
}

const AppTableData: React.FC<Props> = ({headers, columnWidths, columnCss, data, linkBaseUrl}) => {
    return (
        <div>
            <table className="min-w-full bg-white">
                <thead className="text-gray-600 border-b-2 border-gray-400">
                <tr>
                    {headers.map((header, index) => (
                        <th key={index} className={clsx(
                            "py-2 px-4 text-left",
                            columnWidths[index]
                        )}>
                            {header}
                        </th>
                    ))}
                </tr>
                </thead>
                <tbody>
                {data.map((row, rowIndex) => (
                    <tr key={rowIndex} className="hover:bg-gray-300 border-b-1 border-gray-300">
                        {headers.map((header, colIndex) => (
                            <td key={colIndex} className={clsx(
                                "py-2 px-4",
                                columnWidths[colIndex],
                                columnCss[colIndex]
                            )}>

                                {header === "ID" && linkBaseUrl ? (
                                    <Link href={{
                                        pathname: `${linkBaseUrl}/${row[header.toLowerCase()]}`
                                    }}>
                                        {row[header.toLowerCase()]}
                                    </Link>
                                ) : (
                                    row[header.toLowerCase()]
                                )}

                            </td>
                        ))}
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    )
}

export default AppTableData;
