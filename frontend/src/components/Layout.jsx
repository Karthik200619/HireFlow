import { NavLink, useLocation } from 'react-router';
import { BriefcaseBusiness, LayoutDashboard, Search, FileText, Users, PlusCircle, LogOut, Crown, ShieldCheck, Menu, X } from 'lucide-react';
import { useState } from 'react';
import { useAuthStore } from '../store/authStore'; import Footer from './Footer';

export default function Layout({ children }) {
  const { me, logout } = useAuthStore();
  const location = useLocation();
  const [open, setOpen] = useState(false);

  const nav = me?.role === 'USER'
    ? [['/dashboard','Dashboard',LayoutDashboard],['/jobs','Find Jobs',Search],['/applications','Applications',FileText],['/premium','Premium',Crown],['/profile','Profile',Users],['/companies','Companies',BriefcaseBusiness]]
    : me?.role === 'RECRUITER'
      ? [['/dashboard','Dashboard',LayoutDashboard],['/my-jobs','My Jobs',BriefcaseBusiness],['/recruiter-applications','Applications',FileText],['/my-jobs/new','Post Job',PlusCircle],['/profile','Profile',Users],['/companies','Companies',BriefcaseBusiness]]
      : [['/dashboard','Dashboard',LayoutDashboard],['/admin-users','Users',Users],['/admin-jobs','Jobs',BriefcaseBusiness],['/admin-applications','Applications',FileText],['/admin-comments','Comments',FileText],['/profile','Profile',Users],['/companies','Companies',BriefcaseBusiness]];

  const title = nav.find(([path]) => location.pathname === path || (path !== '/dashboard' && location.pathname.startsWith(path + '/')))?.[1] || 'Workspace';

  const close = () => setOpen(false);
  const signOut = () => { close(); logout(); };

  return <div className="app-shell">
    <header className="topnav">
      <div className="nav-inner">
        <NavLink to="/dashboard" className="brand" onClick={close}>
          <span className="logo"><BriefcaseBusiness size={20}/></span><span>HireFlow</span>
        </NavLink>
        <nav className={`mainnav ${open ? 'open' : ''}`}>
          {nav.map(([path,label,Icon]) => <NavLink key={path} to={path} onClick={close} className={({isActive}) => isActive ? 'active' : ''}><Icon size={17}/>{label}</NavLink>)}
          <button className="navlogout" onClick={signOut}><LogOut size={17}/>Sign out</button>
        </nav>
        <div className="nav-user">
          <span className="role-badge"><ShieldCheck size={13}/>{me?.role}</span>
          <span className="avatar nav-avatar">{me?.profileImageUrl ? <img src={me.profileImageUrl} alt="Profile"/> : me?.name?.[0]}</span>
          <span className="nav-name">{me?.name}</span>
          <button className="menu-btn" onClick={() => setOpen(v => !v)} aria-label="Toggle navigation">{open ? <X/> : <Menu/>}</button>
        </div>
      </div>
    </header>
    <main className="page-main">
      <div className="page-heading"><div><div className="eyebrow">{me?.role} WORKSPACE</div><h1>{title}</h1></div></div>
      {children}
    </main>
    <Footer />
  </div>;
}
