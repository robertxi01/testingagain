// ACCOUNT PROFILE PAGE
'use client';
import { useState, useEffect } from 'react';
import Head from 'next/head';

export default function Page() {
    const [fullName, setFullName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [profilePic, setProfilePic] = useState('https://t4.ftcdn.net/jpg/02/15/84/43/360_F_215844325_ttX9YiIIyeaR7Ne6EaLLjMAmy4GvPC69.jpg');

    const [uploading, setUploading] = useState(false);

    const [passwordError, setPasswordError] = useState(false);

    const [cardNumber, setCardNumber] = useState('');
    const [address, setAddress] = useState('');
    const [cards, setCards] = useState([]);
    const [promotions, setPromotions] = useState(false);
    const [updateMsg, setUpdateMsg] = useState('');

    // dummy displayed info (left card)
    const [displayFullName, setDisplayFullName] = useState(fullName);

    // load user info on mount
    useEffect(() => {
        const uid = localStorage.getItem('userId');
        if (!uid) return;
        fetch(`http://localhost:8080/users/${uid}`)
            .then(res => res.json())
            .then(data => {
                setFullName(data.name);
                setEmail(data.email);
                setAddress(data.address || '');
                setPromotions(data.promotions);
                setDisplayFullName(data.name);
            })
            .catch(() => {});

        fetch(`http://localhost:8080/users/${uid}/cards`)
            .then(res => res.json())
            .then(setCards)
            .catch(() => {});
    }, []);

    const handleUpdate = async (event) => {
        event.preventDefault();

        let hasError = false;

        if (password.trim() === '') {
            setPasswordError(true);
            hasError = true;
        } else {
            setPasswordError(false);
        }

        if (hasError) return;

        const uid = localStorage.getItem('userId');
        if (!uid) return;
        await fetch(`http://localhost:8080/users/${uid}`,{
            method:'PUT',
            headers:{'Content-Type':'application/json'},
            body: JSON.stringify({ id: uid, name: fullName, email, phone: '', password, address, promotions })
        });

        if (cardNumber.trim()) {
            await fetch(`http://localhost:8080/users/${uid}/cards`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: new URLSearchParams({ cardNumber })
            });
            setCardNumber('');
            fetch(`http://localhost:8080/users/${uid}/cards`).then(res=>res.json()).then(setCards);
        }
        setDisplayFullName(fullName);
        setPassword('');
        setUpdateMsg('Profile updated');
    };

    const handleImageChange = (event) => {
        if (event.target.files && event.target.files[0]) {
            setUploading(true);
            // fake upload delay
            setTimeout(() => {
                const url = URL.createObjectURL(event.target.files[0]);
                setProfilePic(url);
                setUploading(false);
            }, 1000);
        }
    };

    return (
        <>
            <Head>
                <title>Account</title>
            </Head>
            <div style={pageStyle}>
                {/* LEFT CARD */}
                <div style={cardStyle} className="card1 welcome">
                    <h1 style={{ fontSize: '24px' }}>Welcome back!</h1>
                    <img
                        src={profilePic}
                        alt="Profile"
                        style={{
                            height: '200px',
                            width: '200px',
                            objectFit: 'cover',
                            borderRadius: '50%',
                            margin: '20px',
                            border: '2px solid black',
                        }}
                    />

                    {/* LOADING THINGY */}
                    {uploading && (
                        <div className="fixed inset-0 flex items-center justify-center backdrop-blur-sm z-50">
                            <div className="p-8 rounded-2xl shadow-xl flex flex-col items-center" style={{ backgroundColor: 'rgba(27,28,30, 1)' }}>
                                <div style={loaderStyle}></div>
                                <p className="text-lg font-semibold text-white" style={{ marginTop: '17px' }}>
                                    Uploading...
                                </p>
                            </div>
                        </div>
                    )}

                    <input type="file" onChange={handleImageChange} style={{ display: 'none' }} />
                    <button style={buttonStyle} onClick={() => document.querySelector('input[type="file"]').click()}>
                        Upload Image
                    </button>
                    <p style={{ fontSize: '20px', padding: '10px' }}><strong>{displayFullName}</strong></p>
                </div>

                {/* RIGHT CARD */}
                <div style={{ ...cardStyle, ...card2Style }} className="card2 edit-profile">
                    <h1 style={{ fontSize: '24px' }}>Edit Profile</h1>
                    <input
                        type="text"
                        placeholder="Full Name"
                        value={fullName}
                        onChange={(e) => setFullName(e.target.value)}
                        className="text-black"
                        style={inputStyle}
                    />
                    <input
                        type="email"
                        placeholder="Email"
                        value={email}
                        disabled
                        className="text-black opacity-50"
                        style={inputStyle}
                    />
                    <input
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        className="text-black"
                        style={{
                            ...inputStyle,
                            borderColor: passwordError ? 'red' : inputStyle.borderColor,
                        }}
                    />
                    {passwordError && (
                        <div style={errorTextStyle}>Password required</div>
                    )}
                    <input
                        type="card info"
                        placeholder="Card Info"
                        value={cardNumber}
                        onChange={(e) => setCardNumber(e.target.value)}
                        className="text-black"
                        style={{
                            ...inputStyle,
                            borderColor: passwordError ? 'red' : inputStyle.borderColor,
                        }}
                    />
                    {cards.length > 0 && (
                        <ul className="text-sm text-white">
                            {cards.map(c => (
                                <li key={c.id}>****{c.lastFour} <button className="ml-2 underline" onClick={async()=>{await fetch(`http://localhost:8080/users/${localStorage.getItem('userId')}/cards/${c.id}`,{method:'DELETE'}); setCards(cards.filter(x=>x.id!==c.id));}}>Remove</button></li>
                            ))}
                        </ul>
                    )}
                    <input
                        type="address"
                        placeholder="Address"
                        value={address}
                        onChange={(e) => setAddress(e.target.value)}
                        className="text-black"
                        style={{
                            ...inputStyle,
                            borderColor: passwordError ? 'red' : inputStyle.borderColor,
                        }}
                    />
                    <label className="flex gap-2 items-center mt-2">
                        <input type="checkbox" checked={promotions} onChange={e => setPromotions(e.target.checked)} />
                        Promotions
                    </label>
                    <button onClick={handleUpdate} style={buttonStyle}>
                        Update
                    </button>
                    {updateMsg && <div className="text-green-300 mt-2">{updateMsg}</div>}
                </div>
            </div>
        </>
    );
}

/* Styles preserved from your original file */

const pageStyle = {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    height: '100vh',
    padding: '40px',
    gap: '20px',
    backgroundColor: 'rgba(26,27,29,1)'
};

const cardStyle = {
    border: '1px solid #ccc',
    borderRadius: '16px',
    padding: '20px',
    boxShadow: '0 2px 5px rgba(0,0,0,0.1)',
    backgroundColor: 'rgba(112,31,30,1)',
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    borderColor: 'black',
    color: 'white',
};

const card2Style = {
    width: '500px',
    alignItems: 'center',
    transition: 'height 0.3s ease',
};

const buttonStyle = {
    width: '50%',
    padding: '10px',
    marginTop: '10px',
    backgroundColor: 'oklch(0.241 0.003 196.958)',
    color: 'white',
    border: 'none',
    borderRadius: '5px',
    cursor: 'pointer',
};

const inputStyle = {
    backgroundColor: 'white',
    width: '75%',
    padding: '10px',
    marginTop: '10px',
    borderWidth: '1px',
    borderStyle: 'solid',
    borderColor: '#ccc',
    borderRadius: '5px',
};

const errorTextStyle = {
    color: 'red',
    fontSize: '12px',
    marginTop: '5px',
};

const loaderStyle = {
    width: '45px',
    aspectRatio: '1',
    '--c': 'no-repeat linear-gradient(#fff 0 0)',
    background: `
    var(--c) 0%   50%,
    var(--c) 50%  50%,
    var(--c) 100% 50%
  `,
    backgroundSize: '20% 100%',
    animation: 'l1 1s infinite linear',
};