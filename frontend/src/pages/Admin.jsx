import { useEffect, useState } from 'react';
import { admin } from '../api/services';
import toast from 'react-hot-toast';
import { RefreshCw, Trash2, Check, X, UserRound, BriefcaseBusiness } from 'lucide-react';

const errorMessage = (e, fallback) => e.response?.data?.message || e.response?.data?.error || fallback;

function Table({ title, headers, rows, empty = 'Nothing to show.' }) {
  return <section><div className="panel"><div className="panel-title"><h3>{title}</h3></div><div className="tablewrap"><table><thead><tr>{headers.map(h=><th key={h}>{h}</th>)}</tr></thead><tbody>{rows.map((r,i)=><tr key={i}>{r.map((c,j)=><td key={j}>{c}</td>)}</tr>)}</tbody></table>{!rows.length&&<div className="empty">{empty}</div>}</div></div></section>;
}

export function AdminUsers() {
  const [rows,setRows]=useState([]), [loading,setLoading]=useState(true);
  const load=async()=>{setLoading(true);try{const r=await admin.users();setRows(r.data||[])}catch(e){toast.error(errorMessage(e,'Could not load users'))}finally{setLoading(false)}};
  useEffect(()=>{load()},[]);
  const del=async(id)=>{if(!window.confirm('Delete this user?'))return;try{await admin.deleteUser(id);toast.success('User deleted');load()}catch(e){toast.error(errorMessage(e,'Delete failed'))}};
  return <Table title={`Users (${rows.length})`} headers={['User','Email','Role','Premium','Actions']} rows={loading?[]:rows.map(u=>[<div className="admin-user"><div className="avatar small">{u.profileImageUrl?<img src={u.profileImageUrl} alt=""/>:<UserRound size={15}/>}</div><span>{u.name}</span></div>,u.email,u.role,u.premium?'YES':'NO',u.role==='ADMIN'?<span className="muted">Protected</span>:<button className="danger" onClick={()=>del(u.id)}><Trash2 size={14}/>Delete</button>])} empty={loading?'Loading users…':'No users found.'}/>;
}

export function AdminJobs() {
  const [rows,setRows]=useState([]), [loading,setLoading]=useState(true);
  const load=async()=>{setLoading(true);try{const r=await admin.jobs();setRows(r.data||[])}catch(e){toast.error(errorMessage(e,'Could not load jobs'))}finally{setLoading(false)}};
  useEffect(()=>{load()},[]);
  const approve=async(id)=>{try{await admin.approveJob(id);toast.success('Job approved');load()}catch(e){toast.error(errorMessage(e,'Approval failed'))}};
  const reject=async(id)=>{const reason=window.prompt('Rejection reason:','Please update the job details.');if(reason===null)return;try{await admin.rejectJob(id,reason);toast.success('Job rejected');load()}catch(e){toast.error(errorMessage(e,'Rejection failed'))}};
  const del=async(id)=>{if(!window.confirm('Delete this job permanently?'))return;try{await admin.deleteJob(id);toast.success('Job deleted');load()}catch(e){toast.error(errorMessage(e,'Delete failed'))}};
  return <Table title={`Jobs & approvals (${rows.length})`} headers={['Job','Company','Recruiter','Approval','Actions']} rows={loading?[]:rows.map(j=>[<div><b>{j.title}</b><span>{j.location}</span></div>,j.company,<div className="admin-user"><div className="avatar small">{j.recruiterProfileImageUrl?<img src={j.recruiterProfileImageUrl} alt=""/>:j.recruiterName?.[0]}</div><span>{j.recruiterName}</span></div>,<span className={`status status-${String(j.approvalStatus||'PENDING').toLowerCase()}`}>{j.approvalStatus||'PENDING'}</span>,<div className="actions">{j.approvalStatus!=='APPROVED'&&<button className="primary" onClick={()=>approve(j.id)}><Check size={14}/>Approve</button>}{j.approvalStatus!=='REJECTED'&&<button className="secondary" onClick={()=>reject(j.id)}><X size={14}/>Reject</button>}<button className="danger" onClick={()=>del(j.id)}><Trash2 size={14}/>Delete</button></div>])} empty={loading?'Loading jobs…':'No jobs found.'}/>;
}

export function AdminApplications() {
  const [rows,setRows]=useState([]), [loading,setLoading]=useState(true);
  const load=async()=>{setLoading(true);try{const r=await admin.applications();setRows(r.data||[])}catch(e){toast.error(errorMessage(e,'Could not load applications'))}finally{setLoading(false)}};
  useEffect(()=>{load()},[]);
  return <Table title={`All applications (${rows.length})`} headers={['Candidate','Job','Company','Status','Applied']} rows={loading?[]:rows.map(a=>[<div className="admin-user"><div className="avatar small">{a.userProfileImageUrl?<img src={a.userProfileImageUrl} alt=""/>:<UserRound size={15}/>}</div><span>{a.userName}</span></div>,<div><b>{a.jobTitle}</b><span>{a.location}</span></div>,a.company,<span className="status">{a.status}</span>,a.appliedAt?new Date(a.appliedAt).toLocaleString():'—'])} empty={loading?'Loading applications…':'No applications found.'}/>;
}

export function AdminComments() {
  const [rows,setRows]=useState([]), [loading,setLoading]=useState(true);
  const load=async()=>{setLoading(true);try{const r=await admin.comments();setRows(r.data||[])}catch(e){toast.error(errorMessage(e,'Could not load comments'))}finally{setLoading(false)}};
  useEffect(()=>{load()},[]);
  const del=async(id)=>{if(!window.confirm('Delete this comment?'))return;try{await admin.deleteComment(id);toast.success('Comment deleted');load()}catch(e){toast.error(errorMessage(e,'Delete failed'))}};
  return <Table title={`Job comments (${rows.length})`} headers={['User','Job','Comment','Created','Actions']} rows={loading?[]:rows.map(c=>[<div className="admin-user"><div className="avatar small">{c.userProfileImageUrl?<img src={c.userProfileImageUrl} alt=""/>:c.userName?.[0]}</div><span>{c.userName}</span></div>,c.jobId,<span className="comment-cell">{c.content}</span>,c.createdAt?new Date(c.createdAt).toLocaleString():'—',<button className="danger" onClick={()=>del(c.id)}><Trash2 size={14}/>Delete</button>])} empty={loading?'Loading comments…':'No comments found.'}/>;
}
