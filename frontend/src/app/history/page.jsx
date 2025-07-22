// ORDER HISTORY PAGE
'use client';

const purchaseHistory = [ // not dynamic
    {
        title: 'Empire of Silence',
        author: 'Christopher Ruocchio',
        price: 19.99,
        imageUrl: 'https://m.media-amazon.com/images/I/81IIc433V7L.jpg',
    },
    {
        title: 'The Fifth Season',
        author: 'N.K. Jemisin',
        price: 14.50,
        imageUrl: 'https://m.media-amazon.com/images/I/915orvpMXZL.jpg',
    },
    {
        title: 'Babel',
        author: 'R.F. Kuang',
        price: 21.75,
        imageUrl: 'https://m.media-amazon.com/images/I/A1lv97-jJoL.jpg',
    },
];

export default function PurchaseHistoryPage() {
    return (  // TODO: make card component
        <main className='flex flex-col items-center p-8'>
            <h1 className='text-3xl font-bold mb-8'>Purchase History</h1>
            <div className='grid grid-cols-1 md:grid-cols-3 gap-8 w-full max-w-6xl'>
                {purchaseHistory.map((book, index) => (
                    <div
                        key={index}
                        className='border rounded-lg p-4 flex flex-col items-center shadow hover:shadow-lg transition'
                    >
                        <img
                            src={book.imageUrl}
                            alt={book.title}
                            width={200}
                            height={300}
                            className='rounded mb-4 object-cover'
                        />
                        <h2 className='text-xl font-semibold text-center'>{book.title}</h2>
                        <p className='text-gray-600 mb-2'>by {book.author}</p>
                        <p className='text-green-700 font-bold mb-4'>
                            ${book.price.toFixed(2)}
                        </p>
                        <button className='bg-red-800 hover:bg-[oklch(60%_0.177_26.899)] text-white px-4 py-2 rounded'>
                            Purchase Again
                        </button>
                    </div>
                ))}
            </div>
        </main>
    );
}