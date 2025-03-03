"use client"
import React from "react";
import clsx from "clsx";

interface AppTableNavProps {
    currentPage: number;
    totalPages: number;
    previousPageAction: () => void;
    nextPageAction: () => void;
    indexPageAction: (index: number) => void;
}

const AppTableNav: React.FC<AppTableNavProps> = ({
                                                     currentPage = 0,
                                                     totalPages = 0,
                                                     previousPageAction,
                                                     nextPageAction,
                                                     indexPageAction
                                                 }) => {
    return (
        <nav
            className="isolate inline-flex gap-2 shadow-xs"
            // className="isolate inline-flex -space-x-px shadow-xs"
            aria-label="Pagination">

            {/* Previous Button */}
            <button
                onClick={previousPageAction}
                disabled={currentPage === 0}
                className={clsx(
                    "relative inline-flex items-center px-2 py-2 text-sm font-medium border border-gray-300 transition",
                    currentPage === 0
                        ? "text-gray-400 cursor-not-allowed"
                        : "text-gray-300 hover:bg-gray-400"
                )}
            >
                <span className="sr-only">Prev</span>
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
                     fill="none"
                     stroke={currentPage === 0 ? "gray" : "black"}
                     strokeWidth="1.5" strokeLinecap="round"
                     strokeLinejoin="round">
                    <path d="m12 19-7-7 7-7"></path>
                    <path d="M19 12H5"></path>
                </svg>
            </button>

            {/* Page Numbers */}
            {Array.from({length: totalPages}, (_, index) => (
                <button
                    key={index}
                    onClick={() => indexPageAction(index)}
                    className={clsx(
                        "relative inline-flex items-center px-4 py-2 text-sm font-semibold border border-gray-300 transition",
                        currentPage === index
                            ? "z-10 bg-gray-500 text-white"
                            : "text-gray-800 hover:bg-gray-400"
                    )}
                >
                    {index + 1}
                </button>
            ))}

            {/* Next Button */}
            <button
                onClick={nextPageAction}
                disabled={currentPage >= totalPages - 1}
                className={clsx(
                    "relative inline-flex items-center px-3 py-2 text-sm font-medium border border-gray-300 transition",
                    currentPage >= totalPages - 1
                        ? "text-gray-400 cursor-not-allowed"
                        : "text-gray-300 hover:bg-gray-400"
                )}
            >
                <span className="sr-only">Next</span>
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
                     fill="none"
                     stroke={currentPage >= totalPages - 1 ? "gray" : "black"}
                     strokeWidth="1.5" strokeLinecap="round"
                     strokeLinejoin="round">
                    <path d="M5 12h14"></path>
                    <path d="m12 5 7 7-7 7"></path>
                </svg>
            </button>

        </nav>
    );
};

export default AppTableNav;
