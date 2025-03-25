import React from "react";

interface MainProps {
    children: React.ReactNode
}

export const Main = ({ children }: MainProps) => {
    return (
        <main className="flex justify-center px-4 py-10 grow">
            <div className="w-full max-w-6xl text-center">{children}</div>
        </main>
    )
}
