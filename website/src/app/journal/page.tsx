'use client';

import React, { useState } from 'react';
import { Sidebar } from '@/components/Sidebar';
import { apiServices } from '@/services/apiServices';
import { BookOpen, Check } from 'lucide-react';

export default function JournalPage() {
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [success, setSuccess] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSuccess(false);
    try {
      await apiServices.createJournal({ title, content });
      setSuccess(true);
      setTitle('');
      setContent('');
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="flex min-h-screen">
      <Sidebar />
      <div className="flex-1 p-8 space-y-6">
        <div>
          <h1 className="text-3xl font-bold">Mindfulness Journal</h1>
          <p className="text-slate-400 text-sm">Write down your thoughts and analyze sentiment values</p>
        </div>

        <div className="bg-slate-900 border border-slate-800 p-6 rounded-2xl max-w-2xl space-y-6">
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-slate-300 mb-2">Title</label>
              <input
                type="text"
                required
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                placeholder="Journal entry title"
                className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2.5 text-slate-100 focus:outline-none focus:border-teal-500"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-slate-300 mb-2">Content</label>
              <textarea
                required
                value={content}
                onChange={(e) => setContent(e.target.value)}
                placeholder="Write your mindfulness thoughts..."
                className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-3 text-slate-100 focus:outline-none focus:border-teal-500 h-64"
              />
            </div>

            <button
              type="submit"
              className="w-full bg-teal-500 hover:bg-teal-600 text-slate-900 font-bold py-3 rounded-lg transition"
            >
              Save Journal Entry
            </button>
          </form>

          {success && (
            <div className="bg-teal-500/10 border border-teal-500/30 text-teal-400 p-3 rounded-lg flex items-center gap-2">
              <Check className="w-5 h-5" /> Journal entry saved and sentiment score calculated!
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
