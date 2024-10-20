//import axiosInstance from './axiosInstance'; // Path to the axiosInstance file
import axiosInstance from 'axios';
class PropertyService {
    constructor() {
        this.apiUrl = '/api/StudentHomeBas/Property';
    }

    async deleteProperty(propertyID) {
        const url = `${this.apiUrl}/delete/${propertyID}`;
        try {
            await axiosInstance.delete(url);
            return propertyID;
        } catch (error) {
            console.error('Error deleting property:', error);
            throw error;
        }
    }

    async readProperty(propertyID) {
        const url = `${this.apiUrl}/read/${propertyID}`;
        try {
            const response = await axiosInstance.get(url);
            return response.data;
        } catch (error) {
            console.error('Error reading property:', error);
            throw error;
        }
    }

    async saveProperty(property) {
        const url = `${this.apiUrl}/create`;
        try {
            const response = await axiosInstance.post(url, property);
            return response.data;
        } catch (error) {
            console.error('Error saving property:', error);
            throw error;
        }
    }

    async updateProperty(property) {
        const url = `${this.apiUrl}/update`;
        try {
            const response = await axiosInstance.post(url, property);
            return response.data;
        } catch (error) {
            console.error('Error updating property:', error);
            throw error;
        }
    }

    async fetchAllProperties() {
        const url = `${this.apiUrl}/getAll`;
        try {
            const response = await axiosInstance.get(url);
            return response.data;
        } catch (error) {
            console.error('Error fetching properties:', error);
            throw error;
        }
    }
}

export default new PropertyService();
