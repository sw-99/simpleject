import { Layout } from "@/components/Layout/Layout"

export const Home = () => {
    return (
        <Layout>
            <section>
                <h1>홈 콘텐츠</h1>
                <div className="text-red-500 text-2xl font-bold text-center">
                    Tailwind 적용됨 🎉
                </div>
            </section>
        </Layout>
    )
}
