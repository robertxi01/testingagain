// SPLASH PAGE
'use client';
import React, {useState, useEffect} from 'react';
import { useRouter } from 'next/navigation';
import TopSellers from '@/components/TopSellers';
import ComingSoon from '@/components/ComingSoon';

export default function Home() {
    const [searchTerm, setSearchTerm] = useState('');
    const [topSellers, setTopSellers] = useState([]);

    const [searchType, setSearchType] = useState('title');

    const router = useRouter();

    useEffect(() => {
        fetch('http://localhost:8080/books')
            .then(response => response.json())
            .then(data => setTopSellers(data))
            .catch(error => console.error('Error fetching books:', error));
    }, []);

    return (
        <>
            <main className="bg-[rgb(26,27,29)] min-h-screen pt-24 pb-12">
                <div className="max-w-6xl mx-auto px-4">
                    <section className="text-center mb-12">
                        <h1 className="text-4xl sm:text-5xl font-bold text-white mb-4">
                            Welcome to Team 17 Bookstore
                        </h1>
                        <p className="text-gray-300 text-lg mb-8">
                            Find your next great read
                        </p>

                        {/* search bar */}
                        <form
                            onSubmit={(e) => {
                                e.preventDefault();
                                router.push(`/search?type=${encodeURIComponent(searchType)}&term=${encodeURIComponent(searchTerm)}`);
                            }}
                            className="flex justify-center flex-wrap"
                        >
                            <div className="flex">
                                <select
                                    value={searchType}
                                    onChange={(e) => setSearchType(e.target.value)}
                                    className="px-4 py-2 rounded-l-md border border-gray-600 focus:outline-none focus:ring-2 focus:ring-[oklch(44.4%_0.177_26.899)] bg-gray-800 text-white"
                                >
                                    <option value="author">Author</option>
                                    <option value="genre">Genre</option>
                                    <option value="title">Title</option>
                                </select>

                                <input
                                    type="text"
                                    placeholder="Search books..."
                                    value={searchTerm}
                                    onChange={(e) => setSearchTerm(e.target.value)}
                                    className="px-4 py-2 border border-gray-600 rounded-r-md focus:outline-none focus:ring-2 focus:ring-[oklch(44.4%_0.177_26.899)] bg-gray-800 text-white w-72"
                                />
                            </div>

                            <button
                                type="submit"
                                className="ml-4 px-4 py-2 rounded-md bg-red-900 text-white hover:bg-[oklch(60%_0.177_26.899)] transition-colors"
                            >
                                Search
                            </button>
                        </form>
                    </section>
                </div>

                <TopSellers/>
                <ComingSoon/>
            </main>
        </>
    );
}
