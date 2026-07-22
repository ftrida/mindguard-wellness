'use client';

import React, { useState } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { apiServices } from '@/services/apiServices';
import { api } from '@/lib/api';
import { Shield } from 'lucide-react';

export default function RegisterPage() {
  const [email, setEmail] = useState('');
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [fullName, setFullName] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const router = useRouter();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      await api.post('/api/v1/auth/register', {
        email,
        username,
        password,
        full_name: fullName,
      });
      router.push('/login');
    } catch (err: any) {
      setError(err.response?.data?.detail || 'Registration failed.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-[80vh] flex items-center justify-center px-6">
      <div className="bg-slate-900 border border-slate-800 p-8 rounded-2xl w-full max-w-md space-y-6">
        <div className="text-center space-y-2">
          <div className="inline-flex p-3 rounded-xl bg-teal-500/10 text-teal-400">
            <Shield className="w-8 h-8" />
          </div>
          <h1 className="text-2xl font-bold">Create Account</h1>
          <p className="text-sm text-slate-400">Join MindGuard AI Enterprise Platform</p>
        </div>

        {error && <div className="bg-red-500/10 border border-red-500/30 text-red-400 text-sm p-3 rounded-lg">{error}</div>}

        <form onSubmit={handleSubmit} className="space-y-3">
          <div>
            <label className="block text-xs font-semibold uppercase text-slate-400 mb-1">Full Name</label>
            <input type="text" required value={fullName} onChange={(e) => setFullName(e.target.value)} className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-slate-100 focus:outline-none focus:border-teal-500" />
          </div>
          <div>
            <label className="block text-xs font-semibold uppercase text-slate-400 mb-1">Email Address</label>
            <input type="email" required value={email} onChange={(e) => setEmail(e.target.value)} className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-slate-100 focus:outline-none focus:border-teal-500" />
          </div>
          <div>
            <label className="block text-xs font-semibold uppercase text-slate-400 mb-1">Username</label>
            <input type="text" required value={username} onChange={(e) => setUsername(e.target.value)} className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-slate-100 focus:outline-none focus:border-teal-500" />
          </div>
          <div>
            <label className="block text-xs font-semibold uppercase text-slate-400 mb-1">Password</label>
            <input type="password" required value={password} onChange={(e) => setPassword(e.target.value)} className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-slate-100 focus:outline-none focus:border-teal-500" />
          </div>

          <button type="submit" disabled={loading} className="w-full bg-teal-500 hover:bg-teal-600 text-slate-900 font-bold py-3 rounded-lg transition mt-4">
            {loading ? 'Creating...' : 'Register'}
          </button>
        </form>

        <p className="text-center text-xs text-slate-400">
          Already have an account?{' '}
          <Link href="/login" className="text-teal-400 hover:underline">
            Sign in here
          </Link>
        </p>
      </div>
    </div>
  );
}
