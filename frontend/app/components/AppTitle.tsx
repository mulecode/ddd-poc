"use client"

interface AppTitleProps {
    title: string;
    subTitle?: string;
    actionButton?: React.ReactNode;
}

const AppTitle: React.FC<AppTitleProps> = ({title, subTitle, actionButton}) => {
    return (
        <header className="bg-white shadow-sm">
            <div className="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8 flex justify-between items-center">
                <div>
                    <h1 className="text-3xl font-bold tracking-tight text-gray-900">{title}</h1>
                    {subTitle && <h2 className="text text-gray-700">{subTitle}</h2>}
                </div>
                <div>
                    {actionButton}
                </div>
            </div>
        </header>
    );
};

export default AppTitle;
