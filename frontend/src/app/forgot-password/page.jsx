'use client';
import { useState } from 'react';

export default function ForgotPasswordPage() {
  const [email, setEmail] = useState('');
  const [sent, setSent] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const res = await fetch('http://localhost:8080/users/forgot-password', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({ email })
      });
      if (!res.ok) throw new Error('Failed');
      setSent(true);
    } catch (err) {
      setError('Failed to send email');
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-[rgb(26,27,29)]">
      <form onSubmit={handleSubmit} className="bg-[rgb(112,31,30)] p-8 rounded text-white flex flex-col gap-4 w-80">
        <h1 className="text-xl font-bold">Forgot Password</h1>
        {sent ? (
          <p>Check console for reset token.</p>
        ) : (
          <>
            {error && <p className="text-red-300">{error}</p>}
            <input className="text-black p-2" type="email" placeholder="Email" value={email} onChange={e => setEmail(e.target.value)} />
            <button className="bg-red-900 p-2" type="submit">Send Reset Link</button>
          </>
        )}
      </form>
    </div>
  );
}
