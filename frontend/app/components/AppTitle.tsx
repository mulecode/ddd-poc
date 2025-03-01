"use client"

interface AppTitleProps {
    title: string;
    subTitle?: string;
    actionButton?: React.ReactNode;
    backgroundImage?: string;
}

const AppTitle: React.FC<AppTitleProps> = ({title, subTitle, actionButton, backgroundImage}) => {
    return (
        <header className="bg-white shadow-sm"
                style={{
                    backgroundImage: backgroundImage
                        ? `linear-gradient(to right, rgba(255, 255, 255, 0.2), rgba(255, 255, 255, 1)), url(${backgroundImage})`
                        : "none",
                    backgroundPosition: "left top",
                    backgroundSize: "20%",
                    backgroundRepeat: "no-repeat",
                }}>
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
