// LOGIN POPUP
'use client';

import { useEffect } from 'react';

export default function LoginModal({ show, onClose, setAuthenticated, setUserProfile }) {
    useEffect(() => {
        if (show) document.body.style.overflow = 'hidden';
        else document.body.style.overflow = 'auto';
    }, [show]);

    if (!show) return null;

    return (
        <div
            className="fixed inset-0 z-50 flex items-center justify-center backdrop-blur-sm bg-black/20"
            onClick={onClose}
        >
            <div
                className="bg-[rgb(26,27,29)] p-6 rounded-md shadow-lg min-w-[300px] relative"
                onClick={(e) => e.stopPropagation()}
            >
                <h2 className="text-lg font-semibold mb-4 text-white">Login</h2>
                <form
                    className="flex flex-col gap-3"
                    onSubmit={(e) => {
                        e.preventDefault();
                        setAuthenticated(true);
                        setUserProfile({
                            fullName: e.target.email.value,
                            _id: "dummy-id",
                        });
                        onClose();
                    }}
                >
                    <div>
                        <label className="text-white">Email</label>
                        <input
                            name="email"
                            type="email"
                            className="w-full border px-2 py-1 rounded text-white"
                        />
                    </div>
                    <div>
                        <label className="text-white">Password</label>
                        <input
                            name="password"
                            type="password"
                            className="w-full border px-2 py-1 rounded text-white"
                        />
                    </div>
                    <button
                        type="submit"
                        className="bg-red-900 text-white px-4 py-2 rounded hover:bg-[oklch(60%_0.177_26.899)]"
                    >
                        Submit
                    </button>
                </form>
                <button
                    onClick={onClose}
                    className="absolute top-2 right-2 text-gray-500 hover:text-black"
                >
                    ✕
                </button>
            </div>
        </div>
    );
}