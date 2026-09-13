// Kept as a compatibility bridge for any older imports. New code should use useAuthStore directly.
import { useAuthStore } from '../store/authStore';
export const useAuth = () => useAuthStore();
export function AuthProvider({ children }) { return children; }
