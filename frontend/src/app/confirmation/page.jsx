// ORDER CONFIRMATION PAGE
'use client';
import Link from 'next/link';

export default function ConfirmationPage() {
    return (
        <div className="min-h-screen flex flex-col items-center justify-center  bg-[rgb(27,28,30)] text-white p-8">
            <h1 className="text-3xl font-bold mb-4">Thank you for your purchase!</h1>
            <p className="text-lg mb-6">
                Your purchase information has been emailed to you.
            </p>
            <Link
                href="/"
                className="px-4 py-2 bg-red-900 rounded hover:bg-red-700 transition-colors"
            >
                Return to Home
            </Link>
        </div>
    );
}