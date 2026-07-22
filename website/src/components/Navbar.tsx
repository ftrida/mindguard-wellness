'use client';

import React from 'react';
import Link from 'next/link';
import { useAuth } from '@/context/AuthContext';
import { Shield, User, LogOut } from 'lucide-react';

export const Navbar: React.FC = () => {
  const { user, logout } = useAuth();

  return (
    <nav className="bg-slate-900 border-b border-slate-800 text-white px-6 py-4 flex items-center justify-between">
      <Link href="/" className="flex items-center gap-2 text-xl font-bold text-teal-400">
        <Shield className="w-6 h-6 text-teal-400" />
        MindGuard AI
      </Link>

      <div className="flex items-center gap-6 text-sm">
        <Link href="/dashboard" className="hover:text-teal-400 transition">Dashboard</Link>
        <Link href="/twin" className="hover:text-teal-400 transition">Digital Twin</Link>
        <Link href="/coach" className="hover:text-teal-400 transition">AI Coach</Link>
        <Link href="/admin" className="hover:text-teal-400 transition">Admin Portal</Link>

        {user ? (
          <div className="flex items-center gap-4 border-l border-slate-700 pl-4">
            <span className="text-slate-300 flex items-center gap-1">
              <User className="w-4 h-4" /> {user.username}
            </span>
            <button
              onClick={logout}
              className="bg-red-600/20 text-red-400 hover:bg-red-600/30 px-3 py-1.5 rounded-md flex items-center gap-1 transition"
            >
              <LogOut className="w-4 h-4" /> Logout
            </button>
          </div>
        ) : (
          <div className="flex items-center gap-3 border-l border-slate-700 pl-4">
            <Link href="/login" className="hover:text-teal-400">Login</Link>
            <Link href="/register" className="bg-teal-500 hover:bg-teal-600 text-slate-900 font-semibold px-4 py-1.5 rounded-md transition">
              Register
            </Link>
          </div>
        )}
      </div>
    </nav>
  );
};
