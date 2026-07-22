'use client';

import React from 'react';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js';
import { Line, Bar } from 'react-chartjs-2';

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  Title,
  Tooltip,
  Legend
);

export const MoodTrendChart: React.FC = () => {
  const data = {
    labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
    datasets: [
      {
        label: 'Mood Score (1-10)',
        data: [7, 6, 8, 5, 9, 8, 9],
        borderColor: '#02C39A',
        backgroundColor: 'rgba(2, 195, 154, 0.2)',
        tension: 0.3,
      },
    ],
  };

  const options = {
    responsive: true,
    plugins: {
      legend: { labels: { color: '#94A3B8' } },
    },
    scales: {
      x: { ticks: { color: '#94A3B8' }, grid: { color: '#334155' } },
      y: { ticks: { color: '#94A3B8' }, grid: { color: '#334155' }, min: 1, max: 10 },
    },
  };

  return <Line data={data} options={options} />;
};

export const SleepTrendChart: React.FC = () => {
  const data = {
    labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
    datasets: [
      {
        label: 'Sleep Hours',
        data: [7.5, 6.8, 8.0, 7.2, 6.5, 8.5, 8.2],
        backgroundColor: '#00A896',
        borderRadius: 6,
      },
    ],
  };

  const options = {
    responsive: true,
    plugins: {
      legend: { labels: { color: '#94A3B8' } },
    },
    scales: {
      x: { ticks: { color: '#94A3B8' }, grid: { color: '#334155' } },
      y: { ticks: { color: '#94A3B8' }, grid: { color: '#334155' }, min: 0, max: 12 },
    },
  };

  return <Bar data={data} options={options} />;
};
