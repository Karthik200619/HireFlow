import { Navigate, useLocation } from 'react-router';
import { useAuthStore } from '../store/authStore';

export default function Protected({ children, role }) {
  const me = useAuthStore((s) => s.me);
  const loading = useAuthStore((s) => s.loading);
  const location = useLocation();

  // Never redirect while JWT session restoration is in progress. This is the
  // key to keeping the user on the exact same URL after a normal browser F5.
  if (loading) {
    return <div className="center">Restoring your session…</div>;
  }

  if (!me) {
    return <Navigate to="/login" replace state={{ from: location.pathname }} />;
  }

  if (role && me.role !== role) {
    return <Navigate to="/dashboard" replace />;
  }

  return children;
}
