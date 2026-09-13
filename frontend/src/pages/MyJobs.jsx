import { useCallback, useEffect, useState } from 'react';
import { Link } from 'react-router';
import { RefreshCw, MessageCircle, Send, UserRound } from 'lucide-react';
import { recruiter } from '../api/services';
import toast from 'react-hot-toast';
import Modal from '../components/Modal';
import JobForm from './NewJob';

export default function MyJobs() {
  const [items, setItems] = useState([]);
  const [edit, setEdit] = useState(null);
  const [commentJob, setCommentJob] = useState(null);
  const [comments, setComments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);

  const load = useCallback(async (silent = false) => {
    silent ? setRefreshing(true) : setLoading(true);
    try {
      const response = await recruiter.jobs();
      setItems(Array.isArray(response.data) ? response.data : []);
    } catch (e) {
      toast.error(e.response?.data?.message || 'Could not load your jobs');
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  }, []);

  useEffect(() => { load(); }, [load]);

  const openComments = async (job) => {
    setCommentJob(job);
    try { const r = await recruiter.jobComments(job.id); setComments(r.data || []); }
    catch (e) { toast.error(e.response?.data?.message || 'Could not load comments'); }
  };

  const del = async (id) => {
    if (!window.confirm('Delete this job? This cannot be undone.')) return;
    try {
      await recruiter.deleteJob(id);
      toast.success('Job deleted');
      await load(true);
    } catch (e) {
      toast.error(e.response?.data?.message || 'Delete failed');
    }
  };

  return <section>
    <div className="toolbar">
      <div><h2 className="sectiontitle">My Jobs</h2><p className="muted">Manage every opportunity you have posted.</p></div>
      <div className="actions">
        <button className="secondary" onClick={() => load(true)} disabled={refreshing}><RefreshCw size={16} className={refreshing ? 'spin' : ''}/>Refresh</button>
        <Link className="primary" to="/my-jobs/new">Post new job</Link>
      </div>
    </div>
    <div className="panel">
      {loading ? <div className="empty">Loading your jobs…</div> : items.map((j) => <div className="row" key={j.id}>
        <div><b>{j.title}</b><span>{j.company} · {j.location} · {j.employmentType || 'Full-time'}</span></div>
        <div className="actions"><span className="status">{j.premium ? 'PREMIUM' : 'STANDARD'}</span><button className="secondary" onClick={() => openComments(j)}><MessageCircle size={15}/>Comments</button><button className="secondary" onClick={() => setEdit(j)}>Edit</button><button className="danger" onClick={() => del(j.id)}>Delete</button></div>
      </div>)}
      {!loading && !items.length && <div className="empty">No jobs posted yet. Create your first listing.</div>}
    </div>
    <Modal open={!!edit} onClose={() => setEdit(null)} title="Edit job"><JobForm initial={edit} onDone={async () => { setEdit(null); await load(true); }} /></Modal>
    <Modal open={!!commentJob} onClose={() => setCommentJob(null)} title={commentJob ? `Comments · ${commentJob.title}` : 'Comments'}>
      <div className="comments recruiter-comments">
        {comments.map(c => <div className="comment" key={c.id}><div className="commentavatar">{c.userProfileImageUrl?<img src={c.userProfileImageUrl} alt=""/>:<UserRound size={15}/>}</div><div><b>{c.userName}</b><span>{c.content}</span><small>{c.createdAt ? new Date(c.createdAt).toLocaleString() : ''}</small></div></div>)}
        {!comments.length && <div className="empty">No comments on this job yet.</div>}
      </div>
    </Modal>
  </section>;
}
