import React from 'react';
import { Shield } from 'lucide-react';

export const Footer: React.FC = () => {
  return (
    <footer className="bg-slate-950 border-t border-slate-800 text-slate-400 py-12 px-6">
      <div className="max-w-6xl mx-auto flex flex-col md:flex-row justify-between items-center gap-6">
        <div className="flex items-center gap-2 text-white font-bold text-lg">
          <Shield className="w-5 h-5 text-teal-400" />
          MindGuard AI Enterprise Platform
        </div>
        <div className="text-sm">
          Backend API: <span className="text-teal-400 font-mono">https://mindguard-api-gz19.onrender.com</span>
        </div>
        <div className="text-sm">
          © {new Date().getFullYear()} MindGuard AI. All rights reserved.
        </div>
      </div>
    </footer>
  );
};
