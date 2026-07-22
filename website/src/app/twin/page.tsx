'use client';

import React, { useEffect, useState } from 'react';
import { Sidebar } from '@/components/Sidebar';
import { apiServices } from '@/services/apiServices';
import { Brain, Moon, Monitor, Smile, Flame } from 'lucide-react';

export default function TwinPage() {
  const [twin, setTwin] = useState<any>(null);

  useEffect(() => {
    apiServices.getTwin().then((res) => setTwin(res.data)).catch((err) => console.log(err));
  }, []);

  return (
    <div className="flex min-h-screen">
      <Sidebar />
      <div className="flex-1 p-8 space-y-6">
        <div>
          <h1 className="text-3xl font-bold">Digital Lifestyle Twin</h1>
          <p className="text-slate-400 text-sm">30-Day Automated Baseline Snapshot Metrics</p>
        </div>

        <div className="grid md:grid-cols-2 gap-6">
          <div className="bg-slate-900 border border-slate-800 p-6 rounded-2xl space-y-4">
            <h2 className="text-lg font-bold text-teal-400 flex items-center gap-2">
              <Brain className="w-5 h-5" /> Baseline Profile Summary
            </h2>
            <div className="space-y-3">
              <div className="flex items-center justify-between p-3 bg-slate-950 rounded-lg">
                <span className="flex items-center gap-2 text-slate-300"><Moon className="w-4 h-4 text-blue-400" /> Sleep Hours Baseline</span>
                <span className="font-bold">{twin?.sleep_baseline || 7.5} hrs</span>
              </div>
              <div className="flex items-center justify-between p-3 bg-slate-950 rounded-lg">
                <span className="flex items-center gap-2 text-slate-300"><Monitor className="w-4 h-4 text-purple-400" /> Screen Time Baseline</span>
                <span className="font-bold">{twin?.screen_time_baseline || 4.0} hrs</span>
              </div>
              <div className="flex items-center justify-between p-3 bg-slate-950 rounded-lg">
                <span className="flex items-center gap-2 text-slate-300"><Smile className="w-4 h-4 text-yellow-400" /> Mood Score Baseline</span>
                <span className="font-bold">{twin?.mood_baseline || 7.0} / 10</span>
              </div>
              <div className="flex items-center justify-between p-3 bg-slate-950 rounded-lg">
                <span className="flex items-center gap-2 text-slate-300"><Flame className="w-4 h-4 text-orange-400" /> Active Minutes Baseline</span>
                <span className="font-bold">{twin?.active_minutes_baseline || 30} mins</span>
              </div>
            </div>
          </div>

          <div className="bg-slate-900 border border-slate-800 p-6 rounded-2xl space-y-4">
            <h2 className="text-lg font-bold text-cyan-400">Personal Wellness Score</h2>
            <div className="p-8 text-center bg-slate-950 rounded-xl space-y-2">
              <div className="text-5xl font-extrabold text-teal-400">{Math.round(twin?.wellness_score || 85)}</div>
              <p className="text-sm text-slate-400">Target Range: 75.0 - 100.0</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
