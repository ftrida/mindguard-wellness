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

  const downloadPdf = () => {
    if (!report) return;

    // Build beautifully styled layout for printing
    const element = document.createElement('div');
    element.innerHTML = `
      <div style="padding: 40px; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; color: #0f172a; background: white; max-width: 800px; margin: 0 auto;">
        <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 3px solid #0d9488; padding-bottom: 20px; margin-bottom: 30px;">
          <div>
            <h1 style="color: #0f766e; margin: 0; font-size: 28px; font-weight: 800; letter-spacing: -0.5px;">MindGuard AI</h1>
            <p style="color: #64748b; margin: 5px 0 0 0; font-size: 14px; font-weight: 500;">Mental Wellness & Threat Monitoring Report</p>
          </div>
          <div style="text-align: right;">
            <span style="background: #ccfbf1; color: #0f766e; padding: 6px 12px; border-radius: 9999px; font-size: 12px; font-weight: 700; text-transform: uppercase;">
              ${report.report_type || 'Daily Report'}
            </span>
            <p style="margin: 8px 0 0 0; font-size: 12px; color: #64748b; font-weight: 500;">Generated: ${report.date || new Date().toLocaleDateString()}</p>
          </div>
        </div>

        <div style="background: #f8fafc; border: 1px solid #e2e8f0; border-radius: 16px; padding: 24px; margin-bottom: 32px; display: flex; align-items: center; gap: 20px;">
          <div style="background: #0d9488; color: white; width: 64px; height: 64px; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 20px; font-weight: 800; box-shadow: 0 4px 6px -1px rgba(13, 148, 136, 0.2);">
            ${report.wellness_score || 'N/A'}
          </div>
          <div>
            <h2 style="margin: 0; font-size: 18px; font-weight: 700; color: #0f172a;">Daily Wellness Score</h2>
            <p style="margin: 4px 0 0 0; font-size: 13px; color: #64748b;">Calculated using sleep baselines, exercise levels, and stress biomarkers.</p>
          </div>
        </div>

        <div style="margin-bottom: 32px;">
          <h3 style="color: #0f766e; border-bottom: 2px solid #f1f5f9; padding-bottom: 8px; margin-top: 0; font-size: 16px; font-weight: 700; text-transform: uppercase; letter-spacing: 0.5px;">Activity & Sleep Metrics</h3>
          <table style="width: 100%; border-collapse: collapse; margin-top: 12px;">
            <thead>
              <tr style="background: #f1f5f9; text-align: left;">
                <th style="padding: 10px 14px; font-size: 12px; text-transform: uppercase; color: #475569; font-weight: 700;">Metric Name</th>
                <th style="padding: 10px 14px; font-size: 12px; text-transform: uppercase; color: #475569; font-weight: 700; text-align: right;">Logged Value</th>
                <th style="padding: 10px 14px; font-size: 12px; text-transform: uppercase; color: #475569; font-weight: 700; text-align: right;">Baseline target</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td style="padding: 12px 14px; border-bottom: 1px solid #f1f5f9; font-size: 14px; color: #334155; font-weight: 600;">Sleep Duration</td>
                <td style="padding: 12px 14px; border-bottom: 1px solid #f1f5f9; font-size: 14px; color: #0f172a; font-weight: 700; text-align: right;">${report.metrics_summary?.sleep_hours ?? 'N/A'} hrs</td>
                <td style="padding: 12px 14px; border-bottom: 1px solid #f1f5f9; font-size: 14px; color: #64748b; text-align: right;">${report.baselines?.sleep_hours ?? '7.5'} hrs</td>
              </tr>
              <tr>
                <td style="padding: 12px 14px; border-bottom: 1px solid #f1f5f9; font-size: 14px; color: #334155; font-weight: 600;">Exercise Duration</td>
                <td style="padding: 12px 14px; border-bottom: 1px solid #f1f5f9; font-size: 14px; color: #0f172a; font-weight: 700; text-align: right;">${report.metrics_summary?.exercise_minutes ?? 'N/A'} mins</td>
                <td style="padding: 12px 14px; border-bottom: 1px solid #f1f5f9; font-size: 14px; color: #64748b; text-align: right;">${report.baselines?.exercise_minutes ?? '30'} mins</td>
              </tr>
              <tr>
                <td style="padding: 12px 14px; border-bottom: 1px solid #f1f5f9; font-size: 14px; color: #334155; font-weight: 600;">Screen Time</td>
                <td style="padding: 12px 14px; border-bottom: 1px solid #f1f5f9; font-size: 14px; color: #0f172a; font-weight: 700; text-align: right;">${report.metrics_summary?.screen_time ?? 'N/A'} hrs</td>
                <td style="padding: 12px 14px; border-bottom: 1px solid #f1f5f9; font-size: 14px; color: #64748b; text-align: right;">${report.baselines?.screen_time ?? '4.0'} hrs</td>
              </tr>
              <tr>
                <td style="padding: 12px 14px; border-bottom: 1px solid #f1f5f9; font-size: 14px; color: #334155; font-weight: 600;">Mood State</td>
                <td style="padding: 12px 14px; border-bottom: 1px solid #f1f5f9; font-size: 14px; color: #0f172a; font-weight: 700; text-align: right;">${report.metrics_summary?.mood_score ?? 'N/A'}/10</td>
                <td style="padding: 12px 14px; border-bottom: 1px solid #f1f5f9; font-size: 14px; color: #64748b; text-align: right;">${report.baselines?.mood_score ?? '7.0'}/10</td>
              </tr>
            </tbody>
          </table>
        </div>

        <div style="margin-bottom: 32px; background: #faf5ff; border: 1px solid #f3e8ff; border-radius: 12px; padding: 20px;">
          <h3 style="color: #7e22ce; margin-top: 0; margin-bottom: 10px; font-size: 15px; font-weight: 700; text-transform: uppercase; letter-spacing: 0.5px;">Cognitive Stress & Anxiety Likelihood</h3>
          <div style="display: flex; gap: 30px; margin-bottom: 12px;">
            <div>
              <span style="font-size: 12px; color: #6b21a8; font-weight: 600;">Stress Score</span>
              <p style="margin: 2px 0 0 0; font-size: 20px; font-weight: 800; color: #581c87;">${report.stress_likelihood?.score ?? 'N/A'}/1.0</p>
            </div>
            <div>
              <span style="font-size: 12px; color: #6b21a8; font-weight: 600;">Confidence index</span>
              <p style="margin: 2px 0 0 0; font-size: 20px; font-weight: 800; color: #581c87;">${report.stress_likelihood?.confidence ?? 'N/A'}/1.0</p>
            </div>
          </div>
          <p style="margin: 0; font-size: 13.5px; color: #581c87; line-height: 1.5; font-weight: 500;">
            <strong>Analysis:</strong> ${report.stress_likelihood?.factors?.general ?? 'Cognitive and sleep patterns fall within healthy standards.'}
          </p>
        </div>

        <div style="margin-top: 60px; text-align: center; font-size: 11px; color: #94a3b8; border-top: 1px solid #e2e8f0; padding-top: 24px;">
          <strong>MindGuard AI Platform Security Audit Notice</strong><br/>
          This document is generated automatically under cryptographic user token auth policies. All logged metrics are private and protected.
        </div>
      </div>
    `;

    const opt = {
      margin: 10,
      filename: `MindGuard_Wellness_Report_${report.date || 'daily'}.pdf`,
      image: { type: 'jpeg', quality: 0.98 },
      html2canvas: { scale: 2 },
      jsPDF: { unit: 'mm', format: 'a4', orientation: 'portrait' }
    };

    // Load html2pdf from CDN dynamically
    const script = document.createElement('script');
    script.src = 'https://cdnjs.cloudflare.com/ajax/libs/html2pdf.js/0.10.1/html2pdf.bundle.min.js';
    script.onload = () => {
      // @ts-ignore
      window.html2pdf().from(element).set(opt).save();
    };
    document.body.appendChild(script);
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
            <div className="bg-slate-900 border border-slate-800 p-6 rounded-2xl space-y-4 flex flex-col justify-between">
              <div className="space-y-4">
                <div className="flex justify-between items-center">
                  <h3 className="text-lg font-bold text-teal-400">{report.report_type || 'Report Details'}</h3>
                  <button
                    onClick={downloadPdf}
                    className="bg-teal-500 hover:bg-teal-600 text-slate-900 font-bold px-4 py-2 rounded-lg transition text-xs flex items-center gap-1.5"
                  >
                    <Download className="w-3.5 h-3.5" /> Download PDF Report
                  </button>
                </div>
                <div className="space-y-2 text-sm text-slate-300 font-mono bg-slate-950 p-4 rounded-lg overflow-x-auto max-h-[300px]">
                  <pre>{JSON.stringify(report, null, 2)}</pre>
                </div>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
