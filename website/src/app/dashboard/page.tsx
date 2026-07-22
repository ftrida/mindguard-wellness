'use client';

import React, { useEffect, useState } from 'react';
import Link from 'next/link';
import { Sidebar } from '@/components/Sidebar';
import { MoodTrendChart, SleepTrendChart } from '@/components/Charts';
import { apiServices } from '@/services/apiServices';
import { Heart, Activity, Brain, ShieldAlert, Sparkles, MessageSquare, Award } from 'lucide-react';

export default function DashboardPage() {
  const [twin, setTwin] = useState<any>(null);
  const [stress, setStress] = useState<any>(null);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const twinRes = await apiServices.getTwin();
      setTwin(twinRes.data);
      const stressRes = await apiServices.getStressAssessment();
      setStress(stressRes.data);
    } catch (err) {
      console.log('Error loading dashboard metrics:', err);
    }
  };

  const wellnessScore = twin?.wellness_score || 85;

  return (
    <div className="flex min-h-screen">
      <Sidebar />
      <div className="flex-1 p-8 space-y-8">
        <div>
          <h1 className="text-3xl font-bold">Personal Wellness Dashboard</h1>
          <p className="text-slate-400 text-sm">Real-time Digital Twin & AI Intelligence Metrics</p>
        </div>

        {/* Top Summary Cards */}
        <div className="grid md:grid-cols-3 gap-6">
          <div className="bg-slate-900 border border-slate-800 p-6 rounded-2xl flex items-center justify-between">
            <div>
              <p className="text-xs uppercase font-semibold text-slate-400">Wellness Score</p>
              <h2 className="text-4xl font-extrabold text-teal-400 mt-1">{Math.round(wellnessScore)}/100</h2>
              <p className="text-xs text-slate-400 mt-1">Optimal Baseline Range</p>
            </div>
            <div className="p-3 bg-teal-500/10 text-teal-400 rounded-xl">
              <Heart className="w-8 h-8" />
            </div>
          </div>

          <div className="bg-slate-900 border border-slate-800 p-6 rounded-2xl flex items-center justify-between">
            <div>
              <p className="text-xs uppercase font-semibold text-slate-400">Estimated Stress Score</p>
              <h2 className="text-4xl font-extrabold text-cyan-400 mt-1">{stress?.stress_score || 15}/100</h2>
              <p className="text-xs text-slate-400 mt-1">Confidence: {stress?.confidence_score || 90}%</p>
            </div>
            <div className="p-3 bg-cyan-500/10 text-cyan-400 rounded-xl">
              <Activity className="w-8 h-8" />
            </div>
          </div>

          <div className="bg-slate-900 border border-slate-800 p-6 rounded-2xl flex items-center justify-between">
            <div>
              <p className="text-xs uppercase font-semibold text-slate-400">Digital Twin Baseline</p>
              <h2 className="text-2xl font-bold text-slate-100 mt-1">30-Day Active</h2>
              <p className="text-xs text-slate-400 mt-1">Sleep Target: {twin?.sleep_baseline || 7.5}h</p>
            </div>
            <div className="p-3 bg-purple-500/10 text-purple-400 rounded-xl">
              <Brain className="w-8 h-8" />
            </div>
          </div>
        </div>

        {/* Charts Section */}
        <div className="grid md:grid-cols-2 gap-6">
          <div className="bg-slate-900 border border-slate-800 p-6 rounded-2xl space-y-4">
            <h3 className="text-lg font-bold">7-Day Mood Score Trends</h3>
            <MoodTrendChart />
          </div>
          <div className="bg-slate-900 border border-slate-800 p-6 rounded-2xl space-y-4">
            <h3 className="text-lg font-bold">7-Day Sleep Duration Trends</h3>
            <SleepTrendChart />
          </div>
        </div>

        {/* Quick Module Grid */}
        <div className="space-y-4">
          <h3 className="text-lg font-bold">Core Intelligence Modules</h3>
          <div className="grid md:grid-cols-4 gap-4">
            <Link href="/twin" className="bg-slate-900 border border-slate-800 p-4 rounded-xl hover:border-teal-500 transition block text-center space-y-2">
              <Brain className="w-6 h-6 text-teal-400 mx-auto" />
              <div className="font-semibold text-sm">Digital Twin</div>
            </Link>
            <Link href="/coach" className="bg-slate-900 border border-slate-800 p-4 rounded-xl hover:border-cyan-500 transition block text-center space-y-2">
              <MessageSquare className="w-6 h-6 text-cyan-400 mx-auto" />
              <div className="font-semibold text-sm">AI Coach Chat</div>
            </Link>
            <Link href="/goals" className="bg-slate-900 border border-slate-800 p-4 rounded-xl hover:border-yellow-500 transition block text-center space-y-2">
              <Award className="w-6 h-6 text-yellow-400 mx-auto" />
              <div className="font-semibold text-sm">Goals & Streaks</div>
            </Link>
            <Link href="/admin" className="bg-slate-900 border border-slate-800 p-4 rounded-xl hover:border-red-500 transition block text-center space-y-2">
              <ShieldAlert className="w-6 h-6 text-red-400 mx-auto" />
              <div className="font-semibold text-sm">Admin Portal</div>
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
}
