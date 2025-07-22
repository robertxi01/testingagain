// TOP SELLERS COMPONENT
"use client";
import React, { useEffect, useState } from "react";
import Link from 'next/link';

export default function TopSellers() {
    const [topSellers, setTopSellers] = useState([]);

    useEffect(() => {
        fetch("http://localhost:8080/books")
            .then((res) => res.json())
            .then((data) => {
                const topSellers = data.filter((book) => [11, 12, 13].includes(book.id));
                console.log("Filtered top sellers:", topSellers);
                console.log("ALL books from backend:", data);
                setTopSellers(topSellers);
            })
            .catch((error) => {
                console.error("Error fetching books:", error);
            });
    }, []);

    return (
        <section className="w-full bg-[rgb(78,23,26)] py-12">
            <div className="max-w-6xl mx-auto px-4">
                <h2 className="text-3xl font-bold text-white mb-8 text-center">
                    Top Sellers
                </h2>
                <div className="grid grid-cols-1 sm:grid-cols-3 gap-8">
                    {topSellers.map((book) => (
                        <Link href={`/book/${book.id}`} key={book.id}>
                            <div
                                key={book.id}
                                className="rounded-lg overflow-hidden shadow hover:shadow-lg transition-shadow"
                            >
                                <div className="bg-[rgb(27,28,30)] h-48 flex items-center justify-center">
                                    <img
                                        src={
                                            book.coverImageUrl
                                                ? book.coverImageUrl
                                                : "https://via.placeholder.com/120x180?text=No+Image"
                                        }
                                        alt={book.title}
                                        className="max-h-full max-w-full object-contain"
                                    />
                                </div>
                                <div className="bg-[rgb(112,31,30)] p-4 text-center shadow-2xl">
                                    <h3 className="text-white text-lg font-semibold mb-1">
                                        {book.title}
                                    </h3>
                                    <p className="text-gray-300 text-sm">{book.author}</p>
                                    <p className="text-red-300 text-sm mt-2">
                                        ${book.price?.toFixed(2)}
                                    </p>
                                </div>
                            </div>
                        </Link>
                    ))}
                </div>
            </div>
        </section>
    );
}