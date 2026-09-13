import { useEffect, useRef, useState } from 'react';
import { useForm } from 'react-hook-form';
import { useAuthStore } from '../store/authStore';
import { user } from '../api/services';
import toast from 'react-hot-toast';
import Field from '../components/Field';

export default function Profile(){
  const {me,setMe}=useAuthStore();
  const {register,reset,handleSubmit}=useForm();
  const [image,setImage]=useState(null);
  const [preview,setPreview]=useState('');
  const [saving,setSaving]=useState(false);
  const fileRef=useRef(null);

  useEffect(()=>{reset(me||{});setPreview(me?.profileImageUrl||'')},[me,reset]);
  useEffect(()=>()=>{if(preview?.startsWith('blob:'))URL.revokeObjectURL(preview)},[preview]);

  const pickImage=e=>{
    const file=e.target.files?.[0];
    if(!file)return;
    if(!['image/jpeg','image/png','image/webp'].includes(file.type)){toast.error('Only JPG, PNG or WEBP images are allowed');return}
    if(file.size>5*1024*1024){toast.error('Profile image must be 5 MB or smaller');return}
    setImage(file);
    setPreview(URL.createObjectURL(file));
  };

  const save=async d=>{
    setSaving(true);
    try{
      const r=await user.update(d,image);
      setMe(r.data);setImage(null);toast.success('Profile updated successfully');
    }catch(e){toast.error(e.response?.data?.message||e.response?.data?.error||'Profile update failed')}
    finally{setSaving(false)}
  };

  return <section><div className="panel narrow"><h3>Profile</h3>
    <div className="profile-photo-wrap">
      <div className="profile-photo">{preview?<img src={preview} alt="Profile"/>:<span>{me?.name?.[0]?.toUpperCase()||'U'}</span>}</div>
      <button type="button" className="secondary" onClick={()=>fileRef.current?.click()}>Choose profile image</button>
      <input ref={fileRef} hidden type="file" accept="image/jpeg,image/png,image/webp" onChange={pickImage}/>
      <small>JPG, PNG or WEBP · max 5 MB · stored securely in Cloudinary</small>
    </div>
    <form className="gridform" onSubmit={handleSubmit(save)}>
      <Field label="Name"><input {...register('name')}/></Field><Field label="Email"><input value={me?.email||''} disabled/></Field>
      <Field label="Phone"><input {...register('phoneNumber')}/></Field><Field label="Age"><input type="number" {...register('age',{valueAsNumber:true})}/></Field>
      <Field label="Experience"><input type="number" step="0.1" {...register('experience',{valueAsNumber:true})}/></Field><Field label="Qualification"><input {...register('qualification')}/></Field>
      <Field label="Address"><input {...register('address')}/></Field><button disabled={saving} className="primary span2">{saving?'Saving...':'Save changes'}</button>
    </form></div></section>
}
