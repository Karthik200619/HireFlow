import { useCallback, useEffect, useState } from 'react';
import { RefreshCw, UserRound, BriefcaseBusiness } from 'lucide-react';
import { user, recruiter } from '../api/services';
import toast from 'react-hot-toast';

const STATUSES = ['APPLIED', 'REVIEWING', 'SHORTLISTED', 'REJECTED', 'HIRED'];

export default function Applications({ mode }) {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);

  const load = useCallback(async (silent = false) => {
    silent ? setRefreshing(true) : setLoading(true);
    try {
      const response = mode === 'recruiter' ? await recruiter.applications() : await user.applications();
      setItems(Array.isArray(response.data) ? response.data : []);
    } catch (e) {
      toast.error(e.response?.data?.message || 'Unable to load applications');
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  }, [mode]);

  useEffect(() => { load(); }, [load]);

  if (loading) return <section><div className="panel"><div className="empty">Loading applications…</div></div></section>;

  if (mode === 'recruiter') {
    return <RecruiterApps items={items} reload={() => load(true)} refreshing={refreshing} />;
  }

  return <section>
    <div className="toolbar">
      <div><h2 className="sectiontitle">My Applications</h2><p className="muted">Track every job you have applied for.</p></div>
      <button className="secondary" onClick={() => load(true)} disabled={refreshing}><RefreshCw size={16} className={refreshing ? 'spin' : ''}/>Refresh</button>
    </div>
    <div className="panel">
      {items.map((a) => <div className="row" key={a.id}>
        <div><b>{a.jobTitle || 'Job application'}</b><span>{a.company || 'Company'} · {a.location || 'Location'}</span></div>
        <span className="status">{a.status}</span>
      </div>)}
      {!items.length && <div className="empty">You haven't applied to any jobs yet.</div>}
    </div>
  </section>;
}

function RecruiterApps({ items, reload, refreshing }) {
  const change = async (id, status) => {
    try {
      await recruiter.status(id, status);
      toast.success('Application status updated');
      await reload();
    } catch (e) {
      toast.error(e.response?.data?.message || 'Could not update application status');
    }
  };

  return <section>
    <div className="toolbar">
      <div><h2 className="sectiontitle">Candidate Applications</h2><p className="muted">Review candidates and move them through your hiring pipeline.</p></div>
      <button className="secondary" onClick={reload} disabled={refreshing}><RefreshCw size={16} className={refreshing ? 'spin' : ''}/>Refresh</button>
    </div>
    <div className="panel">
      {items.map((a) => <div className="candidate" key={a.id}>
        <div className="candidateInfo">
          <div className="avatar small">{a.userProfileImageUrl?<img src={a.userProfileImageUrl} alt="Candidate"/>:<UserRound size={16}/>}</div>
          <div><b>{a.userName || 'Candidate'}</b><span><BriefcaseBusiness size={14}/>{a.jobTitle || 'Job'} · {a.company || 'Company'}</span></div>
        </div>
        <select value={a.status} onChange={(e) => change(a.id, e.target.value)} aria-label="Application status">
          {STATUSES.map((status) => <option value={status} key={status}>{status}</option>)}
        </select>
      </div>)}
      {!items.length && <div className="empty">No applications have been received yet.</div>}
    </div>
  </section>;
}
