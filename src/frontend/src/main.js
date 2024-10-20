import { createApp } from 'vue'
import App from './App.vue';
import router from '@/router/index.js';

import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import { library } from '@fortawesome/fontawesome-svg-core';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import {faHeart, faHome, faUsers, faBed, faRestroom,faWifi,faCalendar,faClock,faInfo ,faCouch,faSearch } from '@fortawesome/free-solid-svg-icons';

const app = createApp(App);
library.add(faHeart, faHome, faUsers, faBed, faRestroom ,faWifi,faCalendar,faClock,faInfo,faCouch,faSearch)
app.use(router);
app.component('font-awesome-icon', FontAwesomeIcon).mount('#app')
