// INDIVIDUAL BOOK PAGE
'use client';

import { useEffect, useState } from 'react';
import { useParams } from 'next/navigation';
import ItemAdded from '@/components/ItemAdded';

export default function BookPage() {
    const { id } = useParams();
    const [book, setBook] = useState(null);
    const [loading, setLoading] = useState(true);
    const [showCartModal, setShowCartModal] = useState(false);

    const [quantity, setQuantity] = useState(1);

    const handleAddToCart = () => {
        console.log(`Adding ${quantity} copies of ${book.title} to cart.`);
        // TODO: replace with API call to POST /cart/{userId}/{bookId}
        setShowCartModal(true);

        {/* dummy code ui demo */}
        const item = {
            id,
            title: "Red Rising",
            author: "Pierce Brown",
            buyingPrice: 18.0,
            description: "A sci-fi novel about rebellion.",
            coverImageUrl: "https://m.media-amazon.com/images/I/81wGzzxqHSL.jpg"
        };

        let cart = JSON.parse(localStorage.getItem('cart')) || [];

        cart.push(item);

        localStorage.setItem('cart', JSON.stringify(cart));
    };

    useEffect(() => {
        async function fetchBook() {
            const book = {
                id,
                title: "Red Rising",
                author: "Pierce Brown",
                buyingPrice: 18.0,
                description: "A sci-fi novel about rebellion.",
                coverImageUrl: "https://m.media-amazon.com/images/I/81wGzzxqHSL.jpg"
            };


            setBook(book);
            setLoading(false);
        }

        if (id) {
            fetchBook();
        }
    }, [id]);

    if (loading) return <p className="text-white">Loading...</p>;

    if (!book) return <p className="text-white">Book not found.</p>;

    return (
        <div style={pageStyle}>
            {/* left side*/}
            <div style={imageContainerStyle}>
                <img
                    src={book.coverImageUrl}
                    alt={book.title}
                    style={imageStyle}
                />
            </div>

            {/* right side */}
            <div style={detailsCardStyle}>
                <h1 style={{ fontSize: "28px", marginBottom: "20px" }}>{book.title}</h1>
                <p><strong>Author:</strong> {book.author}</p>
                <p><strong>Price:</strong> ${book.buyingPrice}</p>
                <p style={{ marginTop: "20px" }}>{book.description}</p>

                {/* buttons */}
                <div style={controlsContainerStyle}>
                    <div style={quantityContainerStyle}>
                        <button
                            style={adjustButtonStyle}
                            onClick={() => setQuantity(Math.max(quantity - 1, 1))}
                        >-</button>
                        <span style={quantityDisplayStyle}>{quantity}</span>
                        <button
                            style={adjustButtonStyle}
                            onClick={() => setQuantity(quantity + 1)}
                        >+</button>
                    </div>

                    <button
                        className="px-4 py-2 bg-red-800 text-white rounded-md hover:bg-[oklch(60%_0.177_26.899)]"
                        onClick={handleAddToCart}
                    >
                        Add to Cart
                    </button>
                </div>
            </div>
            <ItemAdded
                show={showCartModal}
                onClose={() => setShowCartModal(false)}
            />

        </div>
    );
}

const pageStyle = {
    display: "flex",
    justifyContent: "center",
    alignItems: "flex-start",
    gap: "40px",
    padding: "60px",
    paddingTop: "200px",
};

const imageContainerStyle = {
    flex: "0 0 300px",
};

const imageStyle = {
    width: "100%",
    height: "auto",
    borderRadius: "8px",
    boxShadow: "0 2px 8px rgba(0, 0, 0, 0.3)",
};

const detailsCardStyle = {
    backgroundColor: "#581313",
    color: "white",
    padding: "30px",
    borderRadius: "8px",
    width: "400px",
    boxShadow: "0 2px 8px rgba(0, 0, 0, 0.3)",
    fontSize: "18px",
    lineHeight: "1.5",
};

const controlsContainerStyle = {
    marginTop: "30px",
    display: "flex",
    alignItems: "center",
    gap: "20px",
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
