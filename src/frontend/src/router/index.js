import { createRouter, createWebHistory } from 'vue-router';
//import axios from 'axios';

// Import your components
import RegisterProperty from '@/components/RegisterProperty.vue';
import AdminDashboard from "@/components/AdminDashboard.vue";
import StudentList from "@/components/StudentList.vue";
import LandlordList from "@/components/LandlordList.vue";
import Communication from "@/components/Communication.vue";
import AdminLayout from "@/components/AdminLayout.vue";
import HomePage from "@/views/HomePage.vue";
import PropertyInfoPage from "@/views/PropertyInfoPage.vue";
import LoginPage from "@/components/LoginPage.vue";
import StudentInbox from "@/components/StudentInbox.vue";
import MainLayout from "@/components/MainLayout.vue";
import LandLordInbox from "@/components/LandLordInbox.vue";
import axios from "axios";

// Define your routes
const routes = [
    {
        path: '/',
        redirect: '/loginPage',
    },
    { path: '/loginPage', name: 'LoginPage', component: LoginPage },
    { path: '/home', name: 'HomePage', component: HomePage },
    { path: '/propertyInfo', name: 'PropertyInfoPage', component: PropertyInfoPage },

    // Admin routes
    {
        path: '/admin-layout',
        name: 'AdminLayout',
        component: AdminLayout,
        meta: { requiresAuth: true, role: 'ROLE_ADMIN' }, // Protect with role
        children: [
            {
                path: '',
                name: 'AdminDashboard',
                component: AdminDashboard,
            },
            {
                path: '/admin-dashboard',
                name: 'AdminDashboard',
                component: AdminDashboard,
            },
            {
                path: '/students',
                name: 'StudentList',
                component: StudentList,
            },
            {
                path: '/landlords',
                name: 'LandlordList',
                component: LandlordList,
            },
            {
                path: '/communication',
                name: 'Communication',
                component: Communication,
            },
        ],
    },

    // Landlord routes
    {
        path: '/landlord-layout',
        name: 'MainLayout',
        component: MainLayout,
        meta: { requiresAuth: true, role: 'ROLE_LANDLORD' }, // Protect with role
        children: [
            { path: '', redirect: '/register-property' },
            {
                path: '/register-property',
                name: 'RegisterProperty',
                component: RegisterProperty,
            },
            {
                path: '/landlord-inbox',
                name: 'LandLordInbox',
                component: LandLordInbox,
            },
            {
                path: '/student-inbox',
                name: 'StudentInbox',
                component: StudentInbox,
            },
        ],
    },
];

// Create the router instance
const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes,
});

// Helper function to check if a token is expired
function isTokenExpired(token) {
    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.exp * 1000 < Date.now();
}

// Global before guard to check authentication and role-based access
router.beforeEach(async (to, from, next) => {
    const requiresAuth = to.meta.requiresAuth;
    const requiredRole = to.meta.role; // Role required by the route
    const token = localStorage.getItem('authToken');

    // If the route requires authentication

    if (requiresAuth) {
        if (!token || isTokenExpired(token)) {
            // Redirect to login if no valid token
            return next({ name: 'LoginPage' });
        }

        // Decode the token and extract the user's role
        const payload = JSON.parse(atob(token.split('.')[1]));
        const userRole = payload.role;

        // Check if the user has the correct role to access the page
        if (requiredRole && userRole !== requiredRole) {
            // If the user does not have the required role, redirect to home or another page
            return next({ name: 'HomePage' });
        }
    }

    next(); // Allow the navigation
});

// Export the router instance
export default router;

//Axios global configuration to include JWT token in every request
// axios.interceptors.request.use((config) => {
//     const token = localStorage.getItem('authToken');
//     if (token) {
//         config.headers['Authorization'] = `Bearer ${token}`;
//     }
//     return config;
// }, (error) => {
//     return Promise.reject(error);
// });
