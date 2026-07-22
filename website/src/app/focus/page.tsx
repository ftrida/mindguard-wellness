'use client';

import React, { useState, useEffect } from 'react';
import { Sidebar } from '@/components/Sidebar';
import { Play, Pause, RotateCcw, Timer } from 'lucide-react';

export default function FocusPage() {
  const [secondsLeft, setSecondsLeft] = useState(1500); // 25 minutes Pomodoro
  const [isActive, setIsActive] = useState(false);

  useEffect(() => {
    let interval: NodeJS.Timeout | null = null;
    if (isActive && secondsLeft > 0) {
      interval = setInterval(() => {
        setSecondsLeft((s) => s - 1);
      }, 1000);
    } else if (secondsLeft === 0) {
      setIsActive(false);
    }
    return () => {
      if (interval) clearInterval(interval);
    };
  }, [isActive, secondsLeft]);

  const toggleTimer = () => setIsActive(!isActive);
  const resetTimer = () => {
    setIsActive(false);
    setSecondsLeft(1500);
  };

  const formatTime = (totalSecs: number) => {
    const mins = Math.floor(totalSecs / 60);
    const secs = totalSecs % 60;
    return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
  };

  return (
    <div className="flex min-h-screen">
      <Sidebar />
      <div className="flex-1 p-8 space-y-6">
        <div>
          <h1 className="text-3xl font-bold">Focus Pomodoro Clock</h1>
          <p className="text-slate-400 text-sm">Boost your productivity using structured time cycles</p>
        </div>

        <div className="bg-slate-900 border border-slate-800 p-12 rounded-2xl max-w-xl text-center space-y-8 flex flex-col items-center">
          <div className="w-48 h-48 rounded-full border-4 border-cyan-500/30 flex items-center justify-center relative">
            <span className="text-4xl font-extrabold font-mono text-cyan-400">{formatTime(secondsLeft)}</span>
          </div>

          <div className="space-y-2">
            <h3 className="text-xl font-semibold">25-Minute Work Cycle</h3>
            <p className="text-slate-400 text-sm max-w-xs">Minimize interruptions and remain fully focused.</p>
          </div>

          <div className="flex gap-4">
            <button
              onClick={toggleTimer}
              className="bg-cyan-500 hover:bg-cyan-600 text-slate-900 font-bold p-4 rounded-full transition flex items-center justify-center"
            >
              {isActive ? <Pause className="w-6 h-6" /> : <Play className="w-6 h-6" />}
            </button>
            <button
              onClick={resetTimer}
              className="bg-slate-800 hover:bg-slate-700 text-white font-medium p-4 rounded-full transition border border-slate-700 flex items-center justify-center"
            >
              <RotateCcw className="w-6 h-6" />
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
