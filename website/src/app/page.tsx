import React from 'react';
import Link from 'next/link';
import { Shield, Brain, Activity, HeartPulse, Sparkles, CheckCircle2, ArrowRight } from 'lucide-react';

export default function LandingPage() {
  return (
    <div className="space-y-24 pb-16">
      {/* Hero Section */}
      <section className="relative pt-20 pb-16 px-6 text-center max-w-5xl mx-auto space-y-8">
        <div className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-teal-500/10 border border-teal-500/30 text-teal-400 text-sm font-medium">
          <Sparkles className="w-4 h-4" /> Next-Generation AI Wellness Architecture
        </div>

        <h1 className="text-5xl md:text-6xl font-extrabold tracking-tight leading-tight">
          Enterprise Security Meets <br />
          <span className="bg-gradient-to-r from-teal-400 via-cyan-400 to-emerald-400 bg-clip-text text-transparent">
            AI-Driven Mental Wellness
          </span>
        </h1>

        <p className="text-lg md:text-xl text-slate-400 max-w-3xl mx-auto leading-relaxed">
          MindGuard AI empowers individuals and organizations with an autonomous Digital Lifestyle Twin, real-time Behavior Drift Indexing, and non-diagnostic Stress Likelihood estimation.
        </p>

        <div className="flex flex-wrap justify-center gap-4 pt-4">
          <Link
            href="/register"
            className="bg-teal-500 hover:bg-teal-600 text-slate-900 font-bold px-8 py-3.5 rounded-lg flex items-center gap-2 transition text-lg shadow-lg shadow-teal-500/25"
          >
            Get Started Free <ArrowRight className="w-5 h-5" />
          </Link>
          <Link
            href="/dashboard"
            className="bg-slate-800 hover:bg-slate-700 text-white font-medium px-8 py-3.5 rounded-lg transition text-lg border border-slate-700"
          >
            Launch Web App
          </Link>
        </div>
      </section>

      {/* Feature Showcase Grid */}
      <section className="max-w-6xl mx-auto px-6 grid md:grid-cols-3 gap-8">
        <div className="bg-slate-900/60 border border-slate-800 p-8 rounded-2xl space-y-4 hover:border-teal-500/50 transition">
          <div className="w-12 h-12 rounded-xl bg-teal-500/10 flex items-center justify-center text-teal-400">
            <Brain className="w-6 h-6" />
          </div>
          <h3 className="text-xl font-bold">Digital Lifestyle Twin</h3>
          <p className="text-slate-400 text-sm leading-relaxed">
            Constructs a personalized 30-day baseline of sleep, screen time, and exercise parameters to track physical and mental equilibrium.
          </p>
        </div>

        <div className="bg-slate-900/60 border border-slate-800 p-8 rounded-2xl space-y-4 hover:border-cyan-500/50 transition">
          <div className="w-12 h-12 rounded-xl bg-cyan-500/10 flex items-center justify-center text-cyan-400">
            <Activity className="w-6 h-6" />
          </div>
          <h3 className="text-xl font-bold">Behavior Drift Engine</h3>
          <p className="text-slate-400 text-sm leading-relaxed">
            Calculates Euclidean distance deviations and standard deviation metrics to detect routine anomalies before burn-out occurs.
          </p>
        </div>

        <div className="bg-slate-900/60 border border-slate-800 p-8 rounded-2xl space-y-4 hover:border-emerald-500/50 transition">
          <div className="w-12 h-12 rounded-xl bg-emerald-500/10 flex items-center justify-center text-emerald-400">
            <HeartPulse className="w-6 h-6" />
          </div>
          <h3 className="text-xl font-bold">Stress Likelihood Engine</h3>
          <p className="text-slate-400 text-sm leading-relaxed">
            Combines multi-factor indicators (sleep deficit, screen excess, active time, journal sentiment) into non-diagnostic risk scores.
          </p>
        </div>
      </section>

      {/* Security & Compliance Callout */}
      <section className="bg-slate-900 border-y border-slate-800 py-16 px-6">
        <div className="max-w-5xl mx-auto flex flex-col md:flex-row items-center justify-between gap-8">
          <div className="space-y-4 max-w-xl">
            <div className="flex items-center gap-2 text-teal-400 font-bold">
              <Shield className="w-5 h-5" /> Zero-Trust Enterprise Security
            </div>
            <h2 className="text-3xl font-bold">Protected by Advanced Security Architecture</h2>
            <p className="text-slate-400 text-sm leading-relaxed">
              Equipped with JWT Token Rotation, bcrypt password hashing, encrypted DataStore, rate limiting, and structured JSON security audit logging.
            </p>
          </div>
          <div className="space-y-3">
            <div className="flex items-center gap-3 text-slate-300">
              <CheckCircle2 className="w-5 h-5 text-teal-400" /> OAuth2 / JWT Token Refresh Flow
            </div>
            <div className="flex items-center gap-3 text-slate-300">
              <CheckCircle2 className="w-5 h-5 text-teal-400" /> Multi-Layer Threat Rate Limiting
            </div>
            <div className="flex items-center gap-3 text-slate-300">
              <CheckCircle2 className="w-5 h-5 text-teal-400" /> Render Production Infrastructure
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}
