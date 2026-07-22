'use client';

import React, { useEffect, useState } from 'react';
import { Sidebar } from '@/components/Sidebar';
import { apiServices } from '@/services/apiServices';
import { Award, Flame, Star } from 'lucide-react';

export default function GoalsPage() {
  const [goals, setGoals] = useState<any[]>([]);
  const [badges, setBadges] = useState<any[]>([]);
  const [streak, setStreak] = useState<any>(null);

  useEffect(() => {
    apiServices.getGoals().then((res) => setGoals(res.data || [])).catch((err) => console.log(err));
    apiServices.getAchievements().then((res) => setBadges(res.data || [])).catch((err) => console.log(err));
    apiServices.getStreak().then((res) => setStreak(res.data)).catch((err) => console.log(err));
  }, []);

  return (
    <div className="flex min-h-screen">
      <Sidebar />
      <div className="flex-1 p-8 space-y-8">
        <div>
          <h1 className="text-3xl font-bold">Goals & Badges</h1>
          <p className="text-slate-400 text-sm">Track your progress milestones and unlock achievements</p>
        </div>

        {/* Streak Banner */}
        <div className="bg-gradient-to-r from-yellow-500/10 to-amber-500/10 border border-yellow-500/30 p-6 rounded-2xl flex items-center justify-between">
          <div className="space-y-1">
            <h3 className="text-xl font-bold text-yellow-400 flex items-center gap-2">
              <Flame className="w-6 h-6 fill-yellow-400" /> Active Logging Streak
            </h3>
            <p className="text-sm text-slate-400">Keep entering daily logs to maintain consistency bonuses!</p>
          </div>
          <div className="text-3xl font-extrabold text-yellow-400">{streak?.current_streak || 3} Days</div>
        </div>

        <div className="grid md:grid-cols-2 gap-8">
          {/* Active Goals */}
          <div className="space-y-4">
            <h2 className="text-xl font-bold flex items-center gap-2">
              <Star className="w-5 h-5 text-teal-400" /> Current Milestones
            </h2>
            <div className="space-y-3">
              {goals.length === 0 ? (
                <div className="bg-slate-900 border border-slate-800 p-6 rounded-xl text-center text-slate-400 text-sm">
                  No active goals set. Start tracking to build milestones!
                </div>
              ) : (
                goals.map((g) => (
                  <div key={g.id} className="bg-slate-900 border border-slate-800 p-4 rounded-xl space-y-2">
                    <div className="flex justify-between font-semibold">
                      <span>{g.title}</span>
                      <span className="text-teal-400">{g.status}</span>
                    </div>
                    <div className="w-full bg-slate-950 h-2.5 rounded-full overflow-hidden">
                      <div
                        className="bg-teal-500 h-2.5 rounded-full"
                        style={{ width: `${Math.min(100, (g.current_value / g.target_value) * 100)}%` }}
                      />
                    </div>
                    <div className="flex justify-between text-xs text-slate-400">
                      <span>Progress: {g.current_value} / {g.target_value}</span>
                    </div>
                  </div>
                ))
              )}
            </div>
          </div>

          {/* Badges */}
          <div className="space-y-4">
            <h2 className="text-xl font-bold flex items-center gap-2">
              <Award className="w-5 h-5 text-purple-400" /> Unlocked Badges
            </h2>
            <div className="grid grid-cols-2 gap-4">
              {badges.length === 0 ? (
                <div className="col-span-2 bg-slate-900 border border-slate-800 p-6 rounded-xl text-center text-slate-400 text-sm">
                  No achievements unlocked yet. Continue tracking to earn badges!
                </div>
              ) : (
                badges.map((b) => (
                  <div key={b.id} className="bg-slate-900 border border-slate-800 p-4 rounded-xl text-center space-y-2">
                    <div className="text-3xl">🏆</div>
                    <div className="font-bold text-sm text-slate-200">{b.title}</div>
                    <div className="text-xs text-slate-400">{b.description}</div>
                  </div>
                ))
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
