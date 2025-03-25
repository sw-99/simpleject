export const Header = () => {
    return (
        <header className="w-full border-b px-4 py-3">
            <div className="max-w-6xl mx-auto flex items-center justify-between">
                {/* 왼쪽: 로고 + 메뉴 */}
                <div className="flex items-center gap-6">
                    <div className="flex items-center gap-2 text-xl font-bold">
                        <span>🚀</span>
                        <span className="text-lg font-semibold">simpleject</span>
                    </div>
                    <nav className="flex gap-4 text-sm text-gray-700">
                        <a href="#">메뉴1</a>
                        <a href="#">메뉴2</a>
                        <a href="#">메뉴3</a>
                        <a href="#">메뉴4</a>
                    </nav>
                </div>

                {/* 오른쪽: 로그인/회원가입 */}
                <div className="flex gap-2">
                    <button className="text-sm border px-3 py-1 rounded hover:bg-gray-100">로그인</button>
                    <button className="text-sm bg-black text-white px-3 py-1 rounded hover:bg-gray-800">회원가입</button>
                </div>
            </div>
        </header>
    )
}
