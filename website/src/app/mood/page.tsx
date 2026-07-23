'use client';

import React, { useState } from 'react';
import { Sidebar } from '@/components/Sidebar';
import { apiServices } from '@/services/apiServices';
import { Smile, Check } from 'lucide-react';

export default function MoodPage() {
  const [mood, setMood] = useState(7);
  const [category, setCategory] = useState('Calm');
  const [notes, setNotes] = useState('');
  const [success, setSuccess] = useState(false);

  const categories = ["Calm", "Happy", "Energetic", "Sad", "Anxious", "Angry", "Tired", "Stressed"];

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSuccess(false);
    try {
      await apiServices.logMood({
        mood_score: mood,
        category: category,
        notes: notes.trim() ? notes : undefined,
      });
      setSuccess(true);
      setNotes('');
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="flex min-h-screen">
      <Sidebar />
      <div className="flex-1 p-8 space-y-6">
        <div>
          <h1 className="text-3xl font-bold">Track Mood State</h1>
          <p className="text-slate-400 text-sm">Reflect on your emotional balance.</p>
        </div>

        <div className="bg-slate-900 border border-slate-800 p-8 rounded-2xl max-w-xl space-y-8">
          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="space-y-4">
              <label className="block text-xl font-bold text-blue-450">
                Mood Balance Score: <span className="text-blue-400">{mood} / 10</span>
              </label>
              <input
                type="range"
                min="1"
                max="10"
                step="1"
                value={mood}
                onChange={(e) => setMood(parseInt(e.target.value))}
                className="w-full h-1.5 bg-slate-800 rounded-lg appearance-none cursor-pointer accent-blue-500"
              />
            </div>

            <div className="space-y-3">
              <label className="block text-sm font-bold text-slate-300">Select Primary Category</label>
              <div className="flex flex-wrap gap-2.5">
                {categories.map((cat) => (
                  <button
                    key={cat}
                    type="button"
                    onClick={() => setCategory(cat)}
                    className={`px-4 py-1.5 rounded-full text-sm font-medium border transition ${
                      category === cat
                        ? 'bg-blue-600 border-blue-600 text-white'
                        : 'bg-transparent border-slate-700 text-slate-300 hover:border-slate-500'
                    }`}
                  >
                    {cat}
                  </button>
                ))}
              </div>
            </div>

            <div className="space-y-2">
              <textarea
                value={notes}
                onChange={(e) => setNotes(e.target.value)}
                placeholder="Notes & Reflections"
                className="w-full bg-transparent border border-slate-700 rounded-lg px-4 py-3 text-slate-100 placeholder-slate-500 focus:outline-none focus:border-blue-500 h-28 text-sm"
              />
            </div>

            <button
              type="submit"
              className="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-3.5 rounded-xl transition shadow-lg shadow-blue-500/10 text-sm"
            >
              Sync Mood Entry
            </button>
          </form>

          {success && (
            <div className="bg-blue-500/10 border border-blue-500/30 text-blue-400 p-3 rounded-lg flex items-center gap-2 text-sm justify-center">
              <Check className="w-4 h-4" /> Mood entry synced successfully!
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
