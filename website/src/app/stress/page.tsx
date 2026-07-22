'use client';

import React, { useEffect, useState } from 'react';
import { Sidebar } from '@/components/Sidebar';
import { apiServices } from '@/services/apiServices';
import { HeartPulse, CheckCircle } from 'lucide-react';

export default function StressPage() {
  const [stress, setStress] = useState<any>(null);

  useEffect(() => {
    apiServices.getStressAssessment().then((res) => setStress(res.data)).catch((err) => console.log(err));
  }, []);

  return (
    <div className="flex min-h-screen">
      <Sidebar />
      <div className="flex-1 p-8 space-y-6">
        <div>
          <h1 className="text-3xl font-bold">Stress Likelihood Engine</h1>
          <p className="text-slate-400 text-sm">Calculates daily risk metrics based on sleep limits, screens, and active minutes</p>
        </div>

        <div className="grid md:grid-cols-2 gap-6">
          <div className="bg-slate-900 border border-slate-800 p-6 rounded-2xl space-y-4">
            <h2 className="text-lg font-bold text-teal-400 flex items-center gap-2">
              <HeartPulse className="w-5 h-5" /> Stress Metrics
            </h2>
            <div className="space-y-4">
              <div>
                <p className="text-sm text-slate-400">Stress Likelihood Risk</p>
                <div className="text-4xl font-extrabold mt-1 text-red-400">
                  {stress?.stress_score !== undefined ? Math.round(stress.stress_score) : 15}%
                </div>
              </div>
              <div>
                <p className="text-sm text-slate-400">Calculated Confidence Level</p>
                <div className="text-2xl font-bold text-slate-200">
                  {stress?.confidence_score !== undefined ? Math.round(stress.confidence_score) : 90}%
                </div>
              </div>
            </div>
          </div>

          <div className="bg-slate-900 border border-slate-800 p-6 rounded-2xl space-y-4">
            <h2 className="text-lg font-bold text-teal-400">Contributing Factors</h2>
            <div className="space-y-3 text-sm text-slate-300">
              <div className="flex items-center gap-2">
                <CheckCircle className="w-5 h-5 text-teal-400" /> Normal Focus Load
              </div>
              <div className="flex items-center gap-2">
                <CheckCircle className="w-5 h-5 text-teal-400" /> Balanced Sleep Duration
              </div>
              <div className="flex items-center gap-2">
                <CheckCircle className="w-5 h-5 text-teal-400" /> Active minutes above baseline
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
