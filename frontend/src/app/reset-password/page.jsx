'use client';
import { useState } from 'react';
import { useSearchParams, useRouter } from 'next/navigation';

export default function ResetPasswordPage() {
  const params = useSearchParams();
  const router = useRouter();
  const token = params.get('token') || '';
  const [password, setPassword] = useState('');
  const [done, setDone] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const res = await fetch('http://localhost:8080/users/reset-password', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({ token, password })
      });
      if (!res.ok) throw new Error('Failed');
      setDone(true);
      setTimeout(() => router.push('/login'), 2000);
    } catch (err) {
      setError('Failed to reset password');
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-[rgb(26,27,29)]">
      <form onSubmit={handleSubmit} className="bg-[rgb(112,31,30)] p-8 rounded text-white flex flex-col gap-4 w-80">
        <h1 className="text-xl font-bold">Reset Password</h1>
        {done ? (
          <p>Password updated</p>
        ) : (
          <>
            {error && <p className="text-red-300">{error}</p>}
            <input className="text-black p-2" type="password" placeholder="New Password" value={password} onChange={e => setPassword(e.target.value)} />
            <button className="bg-red-900 p-2" type="submit">Reset</button>
          </>
        )}
      </form>
    </div>
  );
}
