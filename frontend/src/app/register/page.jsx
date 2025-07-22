'use client';
import { useState } from 'react';
import { useRouter } from 'next/navigation';

export default function RegisterPage() {
  const router = useRouter();
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [phone, setPhone] = useState('');
  const [password, setPassword] = useState('');
  const [promotions, setPromotions] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const res = await fetch('http://localhost:8080/users/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name, email, phone, password, promotions })
      });
      if (!res.ok) throw new Error('Registration failed');
      router.push('/login');
    } catch (err) {
      setError('Registration failed');
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-[rgb(26,27,29)]">
      <form onSubmit={handleSubmit} className="bg-[rgb(112,31,30)] p-8 rounded text-white flex flex-col gap-4 w-96">
        <h1 className="text-xl font-bold">Register</h1>
        {error && <p className="text-red-300">{error}</p>}
        <input className="text-black p-2" type="text" placeholder="Name" value={name} onChange={e => setName(e.target.value)} />
        <input className="text-black p-2" type="email" placeholder="Email" value={email} onChange={e => setEmail(e.target.value)} />
        <input className="text-black p-2" type="text" placeholder="Phone" value={phone} onChange={e => setPhone(e.target.value)} />
        <input className="text-black p-2" type="password" placeholder="Password" value={password} onChange={e => setPassword(e.target.value)} />
        <label className="flex gap-2 items-center">
          <input type="checkbox" checked={promotions} onChange={e => setPromotions(e.target.checked)} />
          Sign up for promotions
        </label>
        <button className="bg-red-900 p-2" type="submit">Register</button>
        <p className="text-sm">Already have an account? <a href="/login" className="underline">Login</a></p>
      </form>
    </div>
  );
}
