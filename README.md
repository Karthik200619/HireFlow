# HireFlow – Next Improved Version

## Next-version improvements
- React Router v7 using the `react-router` package (no `react-router-dom`).
- Responsive role-based top navbar for User, Recruiter and Admin.
- Recruiter can open and view comments for each of their own jobs.
- Admin now has dedicated Users, Jobs & Approvals, Applications, and Comments screens.
- Admin job approval/rejection is enforced: users can apply only to approved jobs.
- Admin approval re-opens the listing; rejection closes it.
- Public job search remains filtered to approved jobs.
- Admin registration endpoint is protected; only an authenticated ADMIN can create another admin.
- Lazy-loading protections remain in place for applications/comments/job relationships.
- Profile images continue to use their stored Cloudinary/CDN URL.

## Frontend
```bash
cd frontend
npm install
npm run dev
```

## Backend
Run from the backend folder with your IDE or Maven:
```bash
mvn clean spring-boot:run
```

Backend: `http://localhost:8089`
Frontend: `http://localhost:5173`

Default predefined admin:
- Email: `admin@hireflow.com`
- Password: `Admin@123`
