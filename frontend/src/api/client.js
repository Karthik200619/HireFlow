import axios from 'axios';
const baseURL=import.meta.env.VITE_API_URL||'http://localhost:8089';
const api=axios.create({baseURL,withCredentials:true});
let refreshPromise=null;
api.interceptors.response.use(r=>r,async error=>{const original=error.config;const status=error.response?.status;const isAuth=original?.url?.includes('/api/auth/');if(status!==401||!original||original._retry||isAuth)return Promise.reject(error);original._retry=true;try{refreshPromise??=api.post('/api/auth/refresh');await refreshPromise;refreshPromise=null;return api(original);}catch(e){refreshPromise=null;import('../store/authStore').then(({useAuthStore})=>useAuthStore.getState().clearAuth());return Promise.reject(e);}});
export default api;
