'use client';

import React, { useState } from 'react';
import { Sidebar } from '@/components/Sidebar';
import { apiServices } from '@/services/apiServices';
import { FileText, Download } from 'lucide-react';

export default function ReportsPage() {
  const [report, setReport] = useState<any>(null);
  const [loading, setLoading] = useState(false);

  const generateReport = async () => {
    setLoading(true);
    try {
      const res = await apiServices.getDailyReport();
      setReport(res.data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex min-h-screen">
      <Sidebar />
      <div className="flex-1 p-8 space-y-6">
        <div>
          <h1 className="text-3xl font-bold">Wellness Reports & Exports</h1>
          <p className="text-slate-400 text-sm">Download aggregates and historical summaries</p>
        </div>

        <div className="grid md:grid-cols-2 gap-6">
          <div className="bg-slate-900 border border-slate-800 p-6 rounded-2xl space-y-6 flex flex-col justify-between">
            <div className="space-y-2">
              <h3 className="text-lg font-bold flex items-center gap-2">
                <FileText className="w-5 h-5 text-teal-400" /> Daily Snapshot Summary
              </h3>
              <p className="text-sm text-slate-400">
                Compiles today&apos;s logged mood, sleep hours, exercise duration, and stress likelihood scores.
              </p>
            </div>

            <button
              onClick={generateReport}
              disabled={loading}
              className="bg-teal-500 hover:bg-teal-600 text-slate-900 font-bold py-3 rounded-lg transition flex items-center justify-center gap-2"
            >
              <Download className="w-5 h-5" /> {loading ? 'Generating...' : 'Generate Today\'s Report'}
            </button>
          </div>

          {report && (
            <div className="bg-slate-900 border border-slate-800 p-6 rounded-2xl space-y-4">
              <h3 className="text-lg font-bold text-teal-400">{report.report_type || 'Report Details'}</h3>
              <div className="space-y-2 text-sm text-slate-300 font-mono bg-slate-950 p-4 rounded-lg overflow-x-auto">
                <pre>{JSON.stringify(report, null, 2)}</pre>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
