// SEARCH DISPLAY PAGE
'use client';
import React, { useState, useEffect } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import Link from 'next/link';

export default function SearchPage() {
    const [searchTerm, setSearchTerm] = useState('');
    const [searchType, setSearchType] = useState('title');
    const [books, setBooks] = useState([]);

    const router = useRouter();
    const searchParams = useSearchParams();

    useEffect(() => {
        const term = searchParams.get('term') || '';
        const type = searchParams.get('type') || 'title';

        setSearchTerm(term);
        setSearchType(type);

        fetch('http://localhost:8080/books')
            .then((response) => response.json())
            .then((data) => {
                let filtered = data;
                if (term) {
                    filtered = data.filter((book) => {
                        const value = (book[type] || '').toLowerCase();
                        return value.includes(term.toLowerCase());
                    });
                }
                setBooks(filtered);
            })
            .catch((error) => console.error('Error fetching books:', error));
    }, [searchParams]);

    return (
        <>
            <main className='bg-[rgb(26,27,29)] min-h-screen pt-24 pb-12'>
                <div className='max-w-6xl mx-auto px-4'>
                    <section className='text-center mb-12'>
                        <p className='text-gray-300 text-lg mb-8'>
                            Find your next great read
                        </p>

                        {/* search bar */}
                        <form
                            onSubmit={(e) => {
                                e.preventDefault();
                                router.push(
                                    `/search?type=${encodeURIComponent(searchType)}&term=${encodeURIComponent(searchTerm)}`
                                );
                            }}
                            className='flex justify-center flex-wrap'
                        >
                            <div className='flex'>
                                <select
                                    value={searchType}
                                    onChange={(e) => setSearchType(e.target.value)}
                                    className='px-4 py-2 rounded-l-md border border-gray-600 focus:outline-none focus:ring-2 focus:ring-[oklch(44.4%_0.177_26.899)] bg-gray-800 text-white'
                                >
                                    <option value='author'>Author</option>
                                    <option value='genre'>Genre</option>
                                    <option value='title'>Title</option>
                                </select>

                                <input
                                    type='text'
                                    placeholder='Search books...'
                                    value={searchTerm}
                                    onChange={(e) => setSearchTerm(e.target.value)}
                                    className='px-4 py-2 border border-gray-600 rounded-r-md focus:outline-none focus:ring-2 focus:ring-[oklch(44.4%_0.177_26.899)] bg-gray-800 text-white w-72'
                                />
                            </div>

                            <button
                                type='submit'
                                className='ml-4 px-4 py-2 rounded-md bg-red-900 text-white hover:bg-[oklch(60%_0.177_26.899)] transition-colors'
                            >
                                Search
                            </button>
                        </form>
                    </section>

                    {/* results */}
                    <section className='bg-[rgb(78,23,26)] w-screen min-h-screen py-12 relative left-1/2 right-1/2 -ml-[50vw] -mr-[50vw]'>
                        <div className='grid grid-cols-1 sm:grid-cols-3 gap-8 px-8'>
                                {books.length > 0 ? (
                                    books.map((book) => (
                                        <Link href={`/book/${book.id}`} key={book.id}>
                                            <div className='rounded-lg overflow-hidden shadow hover:shadow-lg transition-shadow cursor-pointer'>
                                                <div className='bg-[rgb(27,28,30)] h-48 flex items-center justify-center'>
                                                    <img
                                                        src={
                                                            book.coverImageUrl
                                                                ? book.coverImageUrl
                                                                : 'https://via.placeholder.com/120x180?text=No+Image'
                                                        }
                                                        alt={book.title}
                                                        className='max-h-full max-w-full object-contain'
                                                    />
                                                </div>
                                                <div className='bg-[rgb(112,31,30)] p-4 text-center shadow-2xl'>
                                                    <h3 className='text-white text-lg font-semibold mb-1'>
                                                        {book.title}
                                                    </h3>
                                                    <p className='text-gray-300 text-sm'>{book.author}</p>
                                                    <p className='text-red-300 text-sm mt-2'>
                                                        ${book.buyingPrice?.toFixed(2)}
                                                    </p>
                                                </div>
                                            </div>
                                        </Link>
                                    ))
                                ) : (
                                    <p className='text-white text-center col-span-3'>
                                        No books found.
                                    </p>
                                )}
                            </div>
                    </section>
                </div>
            </main>
        </>
    );
}