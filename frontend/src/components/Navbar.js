// NAVBAR COMPONENT
'use client';
import React, { useState, useEffect, useRef } from 'react';
import Link from 'next/link';
import LoginModal from './LoginModal';
import SignupModal from './SignupModal';

export default function Navbar() {
    const [showLogin, setShowLogin] = useState(false);
    const [showSignup, setShowSignup] = useState(false);
    const [authenticated, setAuthenticated] = useState(false);
    const [userProfile, setUserProfile] = useState(null);
    const [dropdownOpen, setDropdownOpen] = useState(false);
    const dropdownRef = useRef(null);

    {/* global thing for cart tracking */}
    const [cartItems, setCartItems] = useState([]);

    // dropdown logic
    useEffect(() => {
        const handleClickOutside = (event) => {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
                setDropdownOpen(false);
            }
        };

        if (dropdownOpen) {
            document.addEventListener('mousedown', handleClickOutside);
        } else {
            document.removeEventListener('mousedown', handleClickOutside);
        }

        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, [dropdownOpen]);

    return (
        <>
            <header
                className="fixed top-0 left-0 w-full shadow-lg z-40 p-2 sm:p-4 flex items-center justify-between"
                style={{ backgroundColor: 'rgb(26,27,29)' }}
            >
                {/* Brand */}
                <Link href="/" className="flex items-center gap-2 flex-shrink-0">
            <span className="text-lg sm:text-2xl font-semibold text-white">
              Online Bookstore
            </span>
                </Link>

                {/* buttons conditional on auth status */}
                <div className="ml-auto flex gap-4">
                    {authenticated ? (
                        <>
                            <Link
                                href="/cart"
                                className="px-4 py-2 bg-red-900 text-white rounded-md hover:bg-[oklch(60%_0.177_26.899)]"
                            >
                                Cart
                            </Link>

                            <div
                                onClick={() => setDropdownOpen((prev) => !prev)}
                                className="relative z-50"
                                ref={dropdownRef}
                            >
                                {/* profile */}
                                <button
                                    type="button"
                                    className="px-4 py-2 bg-red-900 text-white rounded-md hover:bg-[oklch(60%_0.177_26.899)]"
                                >
                                    Profile ▾
                                </button>

                                {dropdownOpen && (
                                    <div className="absolute right-0 mt-2 w-48 bg-zinc-500 shadow-lg rounded-lg py-2">
                                        <Link
                                            href="/account"
                                            className="block px-4 py-2 text-black hover:bg-gray-200"
                                        >
                                            Profile
                                        </Link>

                                        <Link
                                            href="/history"
                                            className="block w-full text-left px-4 py-2 text-black hover:bg-gray-200"
                                        >
                                            Order History
                                        </Link>
                                        
                                        <button
                                            onClick={() => {
                                                setAuthenticated(false);
                                                setUserProfile(null);
                                                setDropdownOpen(false);
                                            }}
                                            className="block w-full text-left px-4 py-2 text-black hover:bg-gray-200"
                                        >
                                            Sign Out
                                        </button>
                                    </div>
                                )}
                            </div>
                        </>

                    ) : (
                        <>
                            <button
                                onClick={() => setShowLogin(true)}
                                className="px-4 py-2 bg-zinc-700 text-white rounded-md hover:bg-zinc-900 hover:text-white focus:outline-none"
                            >
                                Login
                            </button>
                            <button
                                onClick={() => setShowSignup(true)}
                                className="px-4 py-2 bg-red-900 text-white rounded-md hover:bg-[oklch(60%_0.177_26.899)]"
                            >
                                Sign Up
                            </button>
                        </>
                    )}
                </div>
            </header>

            <LoginModal
                show={showLogin}
                onClose={() => setShowLogin(false)}
                setAuthenticated={setAuthenticated}
                setUserProfile={setUserProfile}
            />
            <SignupModal show={showSignup} onClose={() => setShowSignup(false)} />
        </>
    );
}