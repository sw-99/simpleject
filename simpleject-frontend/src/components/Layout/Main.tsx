// import React from 'react';
//
// interface MainProps {
//     setShowLogin: React.Dispatch<React.SetStateAction<boolean>>;
//     setShowSignup: React.Dispatch<React.SetStateAction<boolean>>;
// }
//
// export const Main = ({ setShowLogin, setShowSignup }: MainProps) => {
//     return (
//         <main className="flex flex-col items-center justify-center w-full py-16 space-y-8">
//             <h1 className="text-3xl font-bold">일로 연결되는 사람들, 로켓펀치</h1>
//             <a href="#" onClick={() => setShowLogin(true)} className="text-blue-500">이미 회원이신가요? 로그인</a>
//             <div className="space-x-4">
//                 <button onClick={() => window.location.href = '/'} className="bg-blue-500 text-white py-2 px-4 rounded">구글로 회원가입</button>
//                 <button onClick={() => setShowSignup(true)} className="bg-green-500 text-white py-2 px-4 rounded">이메일로 회원가입</button>
//             </div>
//         </main>
//     );
// };
