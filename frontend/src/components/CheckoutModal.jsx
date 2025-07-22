// CHECKOUT POPUP
'use client';
import React, { useState } from 'react';
import Link from 'next/link';

export default function CheckoutModal({ show, onClose }) {
    const [selectedCard, setSelectedCard] = useState('x1234');
    const [selectedAddress, setSelectedAddress] = useState('default');

    const bookPrice = 18.00; // hard-coded Red Rising price for now

    if (!show) return null;

    return (
        <div className="fixed inset-0 flex items-center justify-center z-50 backdrop-blur-sm">
            <div className="bg-[rgb(27,28,30)] p-8 rounded-2xl shadow-xl text-white w-full max-w-md">
                <h2 className="text-2xl font-bold mb-4">Checkout</h2>

                <p className="text-lg mb-6">
                    Red Rising — <span className="font-semibold">${bookPrice.toFixed(2)}</span>
                </p>

                {/* payment dropdown */}
                <div className="mb-4">
                    <label className="block mb-2">Select Payment Method:</label>
                    <select
                        value={selectedCard}
                        onChange={(e) => setSelectedCard(e.target.value)}
                        className="w-full p-2 rounded text-black bg-zinc-700"
                    >
                        <option value="x1234">Card ending in x1234</option>
                        <option value="new">Enter new card</option>
                    </select>
                </div>

                {/* address dropdown */}
                <div className="mb-6">
                    <label className="block mb-2">Shipping Address:</label>
                    <select
                        value={selectedAddress}
                        onChange={(e) => setSelectedAddress(e.target.value)}
                        className="w-full p-2 rounded text-black bg-zinc-700"
                    >
                        <option value="default">Default Home Address</option>
                        <option value="new">Enter new shipping address</option>
                    </select>
                </div>

                <div className="flex justify-end gap-4">
                    <button
                        onClick={onClose}
                        className="px-4 py-2 bg-zinc-700 rounded hover:bg-zinc-500 transition-colors"
                    >
                        Exit
                    </button>
                    <Link
                        href="/confirmation"
                        className="px-4 py-2 bg-red-900 rounded hover:bg-red-700 transition-colors"
                    >
                        Confirm
                    </Link>
                </div>
            </div>
        </div>
    );
}