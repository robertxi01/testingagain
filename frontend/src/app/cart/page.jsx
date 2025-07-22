// USER CART PAGE
'use client';

import React, {useEffect, useState} from 'react';
import CheckoutModal from '@/components/CheckoutModal'
import Link from 'next/link';

export default function CartPage() {
    const [cartItems, setCartItems] = useState([]);
    const [showCheckoutModal, setShowCheckoutModal] = useState(false);

    const email = typeof window !== 'undefined' ? localStorage.getItem('token') : null;

    const loadCart = () => {
        if (!email) return;
        fetch(`http://localhost:8080/cart/${email}`)
            .then(res => res.json())
            .then(cart => setCartItems(cart.items || []));
    };

    useEffect(() => { loadCart(); }, []);

    const updateQuantity = async (bookId, qty) => {
        await fetch(`http://localhost:8080/cart/${email}/update?bookId=${bookId}&quantity=${qty}`, {method: 'POST'});
        loadCart();
    };

    const handleRemove = async (bookId) => {
        await fetch(`http://localhost:8080/cart/${email}/remove?bookId=${bookId}`, {method: 'POST'});
        loadCart();
    };

    return (
        <>
            return (
            <>
                {cartItems.length === 0 ? ( // for if nothing in cart
                    <div style={pageStyle}>
                        <div style={cardStyle}>
                            <h1 style={titleStyle}>Your Cart is Empty</h1>
                            <p style={messageStyle}>
                                Looks like you haven’t added any books yet. Start exploring to find your next favorite read!
                            </p>
                            <Link href="/">
                                <button style={buttonStyle}>
                                    Browse Books
                                </button>
                            </Link>
                        </div>
                    </div>
                ) : (  // display starts here
                    <div className="pt-30">
                        <h1 className="text-4xl sm:text-5xl font-bold text-white mb-4 pb-10">My Cart</h1>
                        <div className="grid grid-cols-3 gap-4">
                            {cartItems.map((item) => (
                                <div
                                    className='rounded-lg overflow-hidden shadow hover:shadow-lg transition-shadow cursor-pointer'
                                    key={item.bookId}
                                >
                                    <div className='bg-[rgb(27,28,30)] h-48 flex items-center justify-center'>
                                        <img
                                            src={item.coverImageUrl
                                                ? item.coverImageUrl
                                                : 'https://via.placeholder.com/120x180?text=No+Image'}
                                            alt={item.title}
                                            className='max-h-full max-w-full object-contain'
                                        />
                                    </div>
                                    <div className='bg-[rgb(112,31,30)] p-4 text-center shadow-2xl'>
                                        <h3 className='text-white text-lg font-semibold mb-1'>
                                            {item.title}
                                        </h3>
                                        <p className='text-gray-300 text-sm'>{item.author}</p>
                                        <p className='text-red-300 text-sm mt-2'>
                                            ${item.unitPrice?.toFixed(2)}
                                        </p>
                                    </div>
                                    <div style={quantityContainerStyle}>
                                        <button
                                            style={adjustButtonStyle}
                                            onClick={() => updateQuantity(item.bookId, Math.max(item.quantity - 1, 0))}
                                        >-</button>
                                        <span style={quantityDisplayStyle}>{item.quantity}</span>
                                        <button
                                            style={adjustButtonStyle}
                                            onClick={() => updateQuantity(item.bookId, item.quantity + 1)}
                                        >+</button>
                                        <button
                                            style={adjustButtonStyle}
                                            onClick={() => handleRemove(item.bookId)}
                                        >Remove</button>
                                    </div>
                                </div>
                            ))}
                        </div>

                        <div className="mt-8 flex justify-end pr-5">
                            <button
                                className="px-6 py-3 bg-red-900 text-white rounded hover:bg-red-700 transition-colors"
                                onClick={() => {
                                    if (!email) {
                                        alert('Please log in to checkout');
                                    } else {
                                        setShowCheckoutModal(true);
                                    }
                                }}
                            >
                                Checkout
                            </button>
                        </div>

                        <CheckoutModal
                            show={showCheckoutModal}
                            onClose={() => setShowCheckoutModal(false)}
                        />
                    </div>
                )}
            </>
            );
        </>
    );
}

const pageStyle = {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    height: '100vh',
    backgroundColor: '#1a1b1d',
    padding: '40px',
};

const cardStyle = {
    border: '1px solid #ccc',
    borderRadius: '16px',
    padding: '40px',
    boxShadow: '0 2px 5px rgba(0,0,0,0.1)',
    backgroundColor: 'rgb(48,53,53)',
    textAlign: 'center',
    maxWidth: '500px',
    color: 'white',
};

const titleStyle = {
    fontSize: '28px',
    marginBottom: '16px',
};

const messageStyle = {
    fontSize: '18px',
    marginBottom: '24px',
    lineHeight: '1.5',
};

const buttonStyle = {
    backgroundColor: '#b91c1c',
    color: 'white',
    border: 'none',
    padding: '10px 20px',
    borderRadius: '5px',
    cursor: 'pointer',
    fontSize: '16px',
};

const quantityContainerStyle = {
    display: "flex",
    alignItems: "center",
    backgroundColor: "#333",
    borderRadius: "5px",
    padding: "5px 10px",
};

const adjustButtonStyle = {
    background: "none",
    border: "none",
    color: "white",
    fontSize: "20px",
    cursor: "pointer",
    padding: "0 8px",
};

const quantityDisplayStyle = {
    minWidth: "30px",
    textAlign: "center",
    fontSize: "18px",
};
