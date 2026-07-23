'use client';

import React, { useState } from 'react';
import { Sidebar } from '@/components/Sidebar';
import { apiServices } from '@/services/apiServices';
import { Smile, Check } from 'lucide-react';

export default function MoodPage() {
  const [mood, setMood] = useState(5);
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
          <h1 className="text-3xl font-bold">Track Your Mood</h1>
          <p className="text-slate-400 text-sm">Reflect on your current emotional state</p>
        </div>

        <div className="bg-slate-900 border border-slate-800 p-6 rounded-2xl max-w-xl space-y-6">
          <form onSubmit={handleSubmit} className="space-y-6">
            <div>
              <label className="block text-sm font-medium text-slate-300 mb-2">Mood Score (1 - 10): {mood}</label>
              <input
                type="range"
                min="1"
                max="10"
                step="1"
                value={mood}
                onChange={(e) => setMood(parseInt(e.target.value))}
                className="w-full h-2 bg-slate-850 rounded-lg appearance-none cursor-pointer accent-teal-400"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-slate-300 mb-2">Select Primary Category</label>
              <div className="grid grid-cols-4 gap-2">
                {categories.map((cat) => (
                  <button
                    key={cat}
                    type="button"
                    onClick={() => setCategory(cat)}
                    className={`py-2 px-3 rounded-lg border text-sm transition font-medium ${
                      category === cat
                        ? 'bg-teal-500 border-teal-500 text-slate-900 shadow-md'
                        : 'bg-slate-950 border-slate-800 text-slate-400 hover:border-slate-700 hover:text-slate-300'
                    }`}
                  >
                    {cat}
                  </button>
                ))}
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium text-slate-300 mb-2">Reflections / Notes (Optional)</label>
              <textarea
                value={notes}
                onChange={(e) => setNotes(e.target.value)}
                placeholder="How are you feeling today?"
                className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-3 text-slate-100 focus:outline-none focus:border-teal-500 h-32"
              />
            </div>

            <button
              type="submit"
              className="w-full bg-teal-500 hover:bg-teal-600 text-slate-900 font-bold py-3 rounded-lg transition"
            >
              Log Mood Entry
            </button>
          </form>

          {success && (
            <div className="bg-teal-500/10 border border-teal-500/30 text-teal-400 p-3 rounded-lg flex items-center gap-2">
              <Check className="w-5 h-5" /> Mood entry saved successfully!
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
