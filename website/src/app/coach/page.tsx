'use client';

import React, { useState, useEffect, useRef } from 'react';
import { Sidebar } from '@/components/Sidebar';
import { apiServices } from '@/services/apiServices';
import { MessageSquare, Send } from 'lucide-react';

interface ChatMessage {
  role: string;
  content: string;
}

export default function CoachPage() {
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [input, setInput] = useState('');
  const [loading, setLoading] = useState(false);
  const scrollRef = useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    // Load historical memory on startup
    apiServices
      .getMemory()
      .then((res) => {
        if (res.data) setMessages(res.data);
      })
      .catch((err) => console.log(err));
  }, []);

  useEffect(() => {
    scrollRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages]);

  const handleSend = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!input.trim() || loading) return;

    const userMsg = input.trim();
    setInput('');
    setMessages((prev) => [...prev, { role: 'user', content: userMsg }]);
    setLoading(true);

    try {
      const res = await apiServices.sendCoachMessage(userMsg);
      if (res.data?.memory) {
        setMessages(res.data.memory);
      }
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex min-h-screen">
      <Sidebar />
      <div className="flex-1 flex flex-col h-screen">
        <div className="p-6 border-b border-slate-800 bg-slate-900/40">
          <h1 className="text-2xl font-bold flex items-center gap-2">
            <MessageSquare className="w-6 h-6 text-teal-400" /> AI Wellness Coach
          </h1>
          <p className="text-slate-400 text-xs mt-1">Converse with your Digital Twin integrated coach advisor</p>
        </div>

        {/* Chat Messages */}
        <div className="flex-1 overflow-y-auto p-6 space-y-4">
          {messages.map((msg, i) => {
            const isUser = msg.role === 'user';
            return (
              <div key={i} className={`flex ${isUser ? 'justify-end' : 'justify-start'}`}>
                <div
                  className={`max-w-md p-4 rounded-2xl text-sm leading-relaxed ${
                    isUser ? 'bg-teal-500 text-slate-950 font-medium' : 'bg-slate-900 text-slate-100 border border-slate-800'
                  }`}
                >
                  {msg.content}
                </div>
              </div>
            );
          })}
          {loading && (
            <div className="flex justify-start">
              <div className="bg-slate-950 text-slate-400 text-xs p-3 rounded-lg border border-slate-900 italic">
                Coach is typing...
              </div>
            </div>
          )}
          <div ref={scrollRef} />
        </div>

        {/* Send Box */}
        <form onSubmit={handleSend} className="p-6 border-t border-slate-800 bg-slate-900/40 flex gap-3">
          <input
            type="text"
            required
            value={input}
            onChange={(e) => setInput(e.target.value)}
            placeholder="Type your message here..."
            className="flex-1 bg-slate-950 border border-slate-850 rounded-xl px-4 py-3 text-slate-100 focus:outline-none focus:border-teal-500"
          />
          <button
            type="submit"
            disabled={loading}
            className="bg-teal-500 hover:bg-teal-600 text-slate-900 font-bold p-3.5 rounded-xl transition flex items-center justify-center disabled:opacity-50"
          >
            <Send className="w-5 h-5" />
          </button>
        </form>
      </div>
    </div>
  );
}
