'use client';
import { useState } from 'react';
import { useRouter } from 'next/navigation';

export default function LoginPage() {
  const router = useRouter();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [remember, setRemember] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const res = await fetch('http://localhost:8080/users/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({ email, password })
      });
      if (!res.ok) throw new Error('Login failed');
      const data = await res.json();
      if (data.status !== 'ACTIVE') {
        setError('Account inactive');
        return;
      }
      const storage = remember ? localStorage : sessionStorage;
      storage.setItem('token', data.token);
      storage.setItem('userId', data.userId);
      storage.setItem('role', data.role);
      storage.setItem('status', data.status);
      if (data.role === 'ADMIN') {
        router.push('/admin');
      } else {
        router.push('/');
      }
    } catch (err) {
      setError('Invalid credentials');
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-[rgb(26,27,29)]">
      <form onSubmit={handleSubmit} className="bg-[rgb(112,31,30)] p-8 rounded text-white flex flex-col gap-4 w-80">
        <h1 className="text-xl font-bold">Login</h1>
        {error && <p className="text-red-300">{error}</p>}
        <input className="text-black p-2" type="email" placeholder="Email" value={email} onChange={e => setEmail(e.target.value)} />
        <input className="text-black p-2" type="password" placeholder="Password" value={password} onChange={e => setPassword(e.target.value)} />
        <label className="flex gap-2 items-center">
          <input type="checkbox" checked={remember} onChange={e => setRemember(e.target.checked)} />
          Remember me
        </label>
        <button className="bg-red-900 p-2" type="submit">Login</button>
        <div className="flex justify-between text-sm">
          <a href="/forgot-password" className="underline">Forgot password?</a>
          <a href="/register" className="underline">Sign up</a>
        </div>
      </form>
    </div>
  );
}
