<template>
  <div class="login-overlay">
    <div class="login-container">
      <button class="close-button" @click="$emit('close')">✖</button>

      <!-- Form POSTs directly to /login on the Spring Boot backend -->
      <form class="login-form"  @submit.prevent="login">
        <div class="form-group">
          <label for="username">Email</label>
          <div class="input-container">
            <input
                type="email"
                id="username"
            name="username"
            v-model="email"
            placeholder="Email"
            required
            />
            <i class="icon-envelope"></i>
          </div>
        </div>
        <div class="form-group">
          <label for="password">Password</label>
          <div class="input-container">
            <input
                type="password"
                id="password"
                name="password"
                v-model="password"
                placeholder="Password"
                required
            />
            <i class="icon-eye"></i>
          </div>
        </div>
        <div class="form-group form-group-remember">
          <div class="remember-me-container">
            <input
                type="checkbox"
                id="remember"
                v-model="rememberMe"
            />
            <label for="remember">Remember me</label>
          </div>
          <a href="#" class="forgot-password">Forgot Password?</a>
        </div>

        <button type="submit" class="btn-login">Login</button>
        <p class="signup-link">Don't have an account? <a href="#">Sign up</a></p>
      </form>
    </div>
  </div>
</template>
<script>
import axios from "axios";

export default {
  name: "LoginPage",
  data() {
    return {
      email: "",
      password: "",
      rememberMe: false

    };
  },
  methods: {
    async login() {
      const url = 'http://localhost:8080/StudentHomeBas/auth/login';
      console.log(this.email);
      try {
        const response = await axios.post(url, {
          email: this.email,
          password: this.password
        }, {
          headers: {
            'Content-Type': 'application/json',
          }
        });

        if (response.status === 200) {
          const token = response.data.token;
          const redirectUrl = response.data.redirectUrl;
          localStorage.setItem('authToken', token);
          this.$emit('authenticated');
          await this.$router.push(redirectUrl);
        //  await this.$router.push('/dashboard'); // Redirect to a general dashboard
        } else {
          alert('Login failed: ' + response.statusText);
        }
      } catch (error) {
        // Display an alert with the error message received from the backend
        alert('Error during login: ' + (error.response?.data || error.message));
      }
    },

    logout() {
      localStorage.removeItem('authToken');
      this.$router.push('/loginPage');
    }
  }
  ,
  async mounted() {

     }
};

</script>

<style scoped>
.login-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5); /* Darkened background */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.login-container {
  background-color: white;
  padding: 40px;
  border-radius: 8px;
  width: 100%;
  max-width: 400px; /* Adjust as needed */
  position: relative;
}

.close-button {
  position: absolute;
  top: 10px;
  right: 10px;
  background: transparent;
  border: none;
  font-size: 20px;
  cursor: pointer;
}

.login-form {
  display: flex;
  flex-direction: column;
}

.form-group {
  margin-bottom: 20px;
}

.input-container {
  position: relative;
}

.input-container input {
  width: 100%;
  padding: 15px;
  padding-right: 30px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.input-container i {
  position: absolute;
  top: 10px;
  right: 10px;
  color: #888;
}

.btn-login {
  width: 100%;
  padding: 15px;
  background-color: #009688;
  border: none;
  color: white;
  border-radius: 4px;
  cursor: pointer;
}

.btn-login:hover {
  background-color: #00796b;
}

.signup-link {
  margin-top: 20px;
  text-align: center;
  color: black;
}

.signup-link a {
  color: lightgreen;
}

.forgot-password {
  color: darkgreen;
  text-align: left;
}
</style>
