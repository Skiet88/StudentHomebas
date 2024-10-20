//import axiosInstance from './axiosInstance'; // Path to the axiosInstance file
import axiosInstance from 'axios';

class StudentService {
    constructor() {
        this.apiUrl = '/api/StudentHomeBas/student';
    }

    async saveStudent(student) {
        const url = `${this.apiUrl}/save`;
        try {
            const response = await axiosInstance.post(url, student);
            return response.data;
        } catch (error) {
            console.error('Error saving student:', error);
            throw error;
        }
    }

    async readStudent(studentId) {
        const url = `${this.apiUrl}/read/${studentId}`;
        try {
            const response = await axiosInstance.get(url);
            return response.data;
        } catch (error) {
            console.error('Error reading student:', error);
            throw error;
        }
    }

    async updateStudent(student) {
        const url = `${this.apiUrl}/update`;
        try {
            const response = await axiosInstance.put(url, student);
            return response.data;
        } catch (error) {
            console.error('Error updating student:', error);
            throw error;
        }
    }

    async deleteStudent(studentId) {
        const url = `${this.apiUrl}/delete/${studentId}`;
        try {
            await axiosInstance.delete(url);
            return studentId; // Return the student ID to update state
        } catch (error) {
            console.error('Error deleting student:', error);
            throw error;
        }
    }

    async fetchAllStudents() {
        const url = `${this.apiUrl}/getall`;
        try {
            const response = await axiosInstance.get(url);
            return response.data;
        } catch (error) {
            console.error('Error fetching all students:', error);
            throw error;
        }
    }
}

export default new StudentService();
