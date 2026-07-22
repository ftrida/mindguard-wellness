'use client';

import React, { useState } from 'react';
import { Sidebar } from '@/components/Sidebar';
import { apiServices } from '@/services/apiServices';
import { Activity, Check } from 'lucide-react';

export default function LifestylePage() {
  const [sleep, setSleep] = useState(7.5);
  const [screen, setScreen] = useState(4.0);
  const [active, setActive] = useState(30);
  const [success, setSuccess] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSuccess(false);
    try {
      await apiServices.logLifestyle({
        sleep_hours: sleep,
        screen_time_hours: screen,
        active_minutes: active,
      });
      setSuccess(true);
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="flex min-h-screen">
      <Sidebar />
      <div className="flex-1 p-8 space-y-6">
        <div>
          <h1 className="text-3xl font-bold">Log Daily Lifestyle Metrics</h1>
          <p className="text-slate-400 text-sm">Save your daily sleep, screen time, and physical activity</p>
        </div>

        <div className="bg-slate-900 border border-slate-800 p-6 rounded-2xl max-w-xl space-y-6">
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-slate-300 mb-2">Sleep Duration (Hours): {sleep}h</label>
              <input
                type="range"
                min="0"
                max="24"
                step="0.5"
                value={sleep}
                onChange={(e) => setSleep(parseFloat(e.target.value))}
                className="w-full h-2 bg-slate-850 rounded-lg appearance-none cursor-pointer accent-teal-400"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-slate-300 mb-2">Screen Time (Hours): {screen}h</label>
              <input
                type="range"
                min="0"
                max="24"
                step="0.5"
                value={screen}
                onChange={(e) => setScreen(parseFloat(e.target.value))}
                className="w-full h-2 bg-slate-850 rounded-lg appearance-none cursor-pointer accent-teal-400"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-slate-300 mb-2">Active Exercise (Minutes): {active}m</label>
              <input
                type="range"
                min="0"
                max="300"
                step="5"
                value={active}
                onChange={(e) => setActive(parseInt(e.target.value))}
                className="w-full h-2 bg-slate-850 rounded-lg appearance-none cursor-pointer accent-teal-400"
              />
            </div>

            <button
              type="submit"
              className="w-full bg-teal-500 hover:bg-teal-600 text-slate-900 font-bold py-3 rounded-lg transition"
            >
              Save Metrics
            </button>
          </form>

          {success && (
            <div className="bg-teal-500/10 border border-teal-500/30 text-teal-400 p-3 rounded-lg flex items-center gap-2">
              <Check className="w-5 h-5" /> Lifestyle metrics logged successfully!
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
