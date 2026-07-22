'use client';

import React, { useEffect, useState } from 'react';
import { Sidebar } from '@/components/Sidebar';
import { apiServices } from '@/services/apiServices';
import { ShieldAlert, Compass } from 'lucide-react';

export default function BehaviorPage() {
  const [drift, setDrift] = useState<any>(null);

  useEffect(() => {
    apiServices.getBehaviorDrift().then((res) => setDrift(res.data)).catch((err) => console.log(err));
  }, []);

  return (
    <div className="flex min-h-screen">
      <Sidebar />
      <div className="flex-1 p-8 space-y-6">
        <div>
          <h1 className="text-3xl font-bold">Behavior Analysis Engine</h1>
          <p className="text-slate-400 text-sm">Monitors routine stability indexes using standard deviations</p>
        </div>

        <div className="grid md:grid-cols-2 gap-6">
          <div className="bg-slate-900 border border-slate-800 p-6 rounded-2xl space-y-4">
            <h2 className="text-lg font-bold text-teal-400 flex items-center gap-2">
              <Compass className="w-5 h-5" /> Current Habit Index
            </h2>
            <div className="space-y-4">
              <div>
                <p className="text-sm text-slate-400">Drift Index (Anomalous Deviation)</p>
                <div className="text-4xl font-extrabold mt-1 text-cyan-400">{drift?.drift_score || 0.0}%</div>
              </div>
              <div>
                <p className="text-sm text-slate-400">Habit Consistency Target</p>
                <div className="text-2xl font-bold text-slate-200">{drift?.consistency_score || 100.0}%</div>
              </div>
            </div>
          </div>

          <div className="bg-slate-900 border border-slate-800 p-6 rounded-2xl space-y-4">
            <h2 className="text-lg font-bold text-yellow-400">Engine Explanation</h2>
            <p className="text-slate-300 text-sm leading-relaxed">
              {drift?.explanation || "No lifestyle drift detected. Active minutes, sleep cycles, and screen schedules are currently matching baseline snapshot thresholds."}
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}
