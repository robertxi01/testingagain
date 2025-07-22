// ACCOUNT PROFILE PAGE
'use client';
import { useState } from 'react';
import Head from 'next/head';

export default function Page() {
    // dummy profile info
    const [fullName, setFullName] = useState('Jen Chen');
    const [email, setEmail] = useState('jdc0226@gmail.com');
    const [confirmEmail, setConfirmEmail] = useState('');
    const [password, setPassword] = useState('');
    const [profilePic, setProfilePic] = useState('https://t4.ftcdn.net/jpg/02/15/84/43/360_F_215844325_ttX9YiIIyeaR7Ne6EaLLjMAmy4GvPC69.jpg');

    const [uploading, setUploading] = useState(false);

    const [confirmEmailError, setConfirmEmailError] = useState(false);
    const [passwordError, setPasswordError] = useState(false);

    const [cardNumber, setCardNumber] = useState('');
    const [address, setAddress] = useState('');

    // dummy displayed info (left card)
    const [displayFullName, setDisplayFullName] = useState(fullName);

    const handleUpdate = (event) => {
        event.preventDefault();

        let hasError = false;

        if (confirmEmail !== email) {
            setConfirmEmailError(true);
            hasError = true;
        } else {
            setConfirmEmailError(false);
        }

        if (password.trim() === '') {
            setPasswordError(true);
            hasError = true;
        } else {
            setPasswordError(false);
        }

        if (hasError) return;

        // mimic successful update
        setDisplayFullName(fullName);
        alert('Profile updated (dummy save)!');
        setPassword('');
        setConfirmEmail('');
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
                        onChange={(e) => setEmail(e.target.value)}
                        className="text-black"
                        style={inputStyle}
                    />
                    <input
                        type="email"
                        placeholder="Confirm Email"
                        value={confirmEmail}
                        onChange={(e) => setConfirmEmail(e.target.value)}
                        className="text-black"
                        style={{
                            ...inputStyle,
                            borderColor: confirmEmailError ? 'red' : inputStyle.borderColor,
                        }}
                    />
                    {confirmEmailError && (
                        <div style={errorTextStyle}>Emails must match</div>
                    )}
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
                    <button onClick={handleUpdate} style={buttonStyle}>
                        Update
                    </button>
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