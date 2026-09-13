import React, { useEffect } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router';
import { Toaster } from 'react-hot-toast';
import { useAuthStore } from './store/authStore';
import Protected from './components/Protected';
import Layout from './components/Layout';
import Login from './pages/Login';
import Register from './pages/Register';
import Dashboard from './pages/Dashboard';
import Jobs from './pages/Jobs'; import JobDetails from './pages/JobDetails'; import Companies from './pages/Companies';
import Applications from './pages/Applications';
import Profile from './pages/Profile';
import Premium from './pages/Premium';
import MyJobs from './pages/MyJobs';
import NewJob from './pages/NewJob';
import { AdminUsers, AdminJobs, AdminApplications, AdminComments } from './pages/Admin';
import './styles.css';

function Wrap({ children, role }) {
  return <Protected role={role}><Layout>{children}</Layout></Protected>;
}

export default function App() {
  const initialize = useAuthStore((s) => s.initialize);

  useEffect(() => {
    initialize();
  }, [initialize]);

  return (
    <BrowserRouter>
      <Toaster position="top-right" toastOptions={{ duration: 3500 }} />
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/dashboard" element={<Wrap><Dashboard /></Wrap>} />
        <Route path="/jobs" element={<Wrap role="USER"><Jobs /></Wrap>} />
        <Route path="/jobs/:id" element={<Wrap role="USER"><JobDetails /></Wrap>} />
        <Route path="/companies" element={<Wrap><Companies /></Wrap>} />
        <Route path="/applications" element={<Wrap role="USER"><Applications mode="user" /></Wrap>} />
        <Route path="/profile" element={<Wrap><Profile /></Wrap>} />
        <Route path="/premium" element={<Wrap role="USER"><Premium /></Wrap>} />
        <Route path="/my-jobs" element={<Wrap role="RECRUITER"><MyJobs /></Wrap>} />
        <Route path="/my-jobs/new" element={<Wrap role="RECRUITER"><NewJob /></Wrap>} />
        <Route path="/recruiter-applications" element={<Wrap role="RECRUITER"><Applications mode="recruiter" /></Wrap>} />
        <Route path="/admin-users" element={<Wrap role="ADMIN"><AdminUsers /></Wrap>} />
        <Route path="/admin-jobs" element={<Wrap role="ADMIN"><AdminJobs /></Wrap>} />
        <Route path="/admin-applications" element={<Wrap role="ADMIN"><AdminApplications /></Wrap>} />
        <Route path="/admin-comments" element={<Wrap role="ADMIN"><AdminComments /></Wrap>} />
        <Route path="*" element={<Navigate to="/dashboard" replace />} />
      </Routes>
    </BrowserRouter>
  );
}
