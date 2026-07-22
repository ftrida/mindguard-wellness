'use client';

import React from 'react';
import Link from 'next/link';
import { usePathname } from 'next/navigation';
import {
  LayoutDashboard,
  Activity,
  Smile,
  BookOpen,
  Brain,
  MessageSquare,
  Award,
  FileText,
  ShieldAlert,
} from 'lucide-react';

export const Sidebar: React.FC = () => {
  const pathname = usePathname();

  const links = [
    { name: 'Dashboard', href: '/dashboard', icon: LayoutDashboard },
    { name: 'Lifestyle Tracker', href: '/lifestyle', icon: Activity },
    { name: 'Mood Tracker', href: '/mood', icon: Smile },
    { name: 'Mindfulness Journal', href: '/journal', icon: BookOpen },
    { name: 'Digital Twin', href: '/twin', icon: Brain },
    { name: 'AI Coach Chat', href: '/coach', icon: MessageSquare },
    { name: 'Goals & Badges', href: '/goals', icon: Award },
    { name: 'Reports & Export', href: '/reports', icon: FileText },
    { name: 'Admin Portal', href: '/admin', icon: ShieldAlert },
  ];

  return (
    <aside className="w-64 bg-slate-900 border-r border-slate-800 text-slate-300 min-h-screen p-4">
      <div className="space-y-1">
        {links.map((link) => {
          const Icon = link.icon;
          const isActive = pathname === link.href;
          return (
            <Link
              key={link.href}
              href={link.href}
              className={`flex items-center gap-3 px-4 py-3 rounded-lg font-medium transition ${
                isActive
                  ? 'bg-teal-500/10 text-teal-400 border-l-4 border-teal-400'
                  : 'hover:bg-slate-800 hover:text-white'
              }`}
            >
              <Icon className="w-5 h-5" />
              {link.name}
            </Link>
          );
        })}
      </div>
    </aside>
  );
};
